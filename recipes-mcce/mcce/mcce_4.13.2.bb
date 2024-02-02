DESCRIPTION = "MCCE"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/LICENSE;md5=4336ad26bb93846e47581adc44c4514d"

MBED_EDGE_CORE_CONFIG_PARSEC_TPM_SE_SUPPORT ?= "OFF"
MBED_EDGE_CMAKE_BUILD_TYPE ?= "Debug"

DEPENDS = "python3-native python3-pip-native python3 python3-setuptools-native python3-setuptools-scm cmake-native mercurial-native python3-pyusb-native"
DEPENDS += "python3-mbed-cli-native"

RDEPENDS:${PN} += "bash python3 python3-core"

inherit cmake pkgconfig gitpkgv setuptools3 python3native

SRCREV_FORMAT = "mcce"

SRC_URI = " \
git://git@github.com/PelionIoT/mbed-cloud-client-example.git;protocol=https;name=mcce;branch=master \
"

SRCREV:pn-mcce = "${AUTOREV}"
PV="${SRCREV:pn-mcce}+git${SRCPV}"

S = "${WORKDIR}/git"
FILES:${PN} = "${bindir}/mbed-cloud-client-example"

lcl_maybe_fortify = '-D_FORTIFY_SOURCE=0'

do_compile[network] = "1"
do_compile() {

    cd ${S}

    export HTTP_PROXY=${HTTP_PROXY}
    export HTTPS_PROXY=${HTTPS_PROXY}

    mbedpath=$(which mbed-cli);
    # Don't deploy Mbed OS, that's waste in Linux
    if [ -f "mbed-os.lib" ]; then
        rm "mbed-os.lib"
    fi
    python3 $mbedpath deploy

    python3 pal-platform/pal-platform.py -v deploy --target=Yocto_Generic_YoctoLinux_mbedtls generate

    cd ${S}/__Yocto_Generic_YoctoLinux_mbedtls/

    export ARMGCC_DIR=$(realpath $(pwd)/../../recipe-sysroot-native/usr/)

    cmake -G 'Unix Makefiles' -DCMAKE_BUILD_TYPE="${MBED_EDGE_CMAKE_BUILD_TYPE}" -DCMAKE_TOOLCHAIN_FILE=../pal-platform/Toolchain/POKY-GLIBC/POKY-GLIBC.cmake -DEXTARNAL_DEFINE_FILE=../linux-config.cmake

    make mbedCloudClientExample.elf

}

do_install() {
  install -d ${D}/${bindir}
  install -m 755 ${S}/__Yocto_Generic_YoctoLinux_mbedtls/Debug/mbedCloudClientExample.elf ${D}${bindir}/mbed-cloud-client-example
}
