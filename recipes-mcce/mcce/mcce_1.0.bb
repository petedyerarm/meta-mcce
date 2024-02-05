DESCRIPTION = "MCCE"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit pkgconfig systemd gitpkgv


RT_SERVICE_FILE = "mcce.service"
SRC_URI = " \
file://mcce.service \
"
SRCREV = "v1.1.0"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "${RT_SERVICE_FILE}"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

PR = "r1"
PV = "r1"

RDEPENDS:${PN} = " libstdc++"


FILES:${PN} += "\
    ${systemd_system_unitdir}/${RT_SERVICE_FILE}\
    "

do_install() {

    install -d ${D}${systemd_system_unitdir}

    install -m 0644 ${S}/../${RT_SERVICE_FILE} ${D}${systemd_system_unitdir}/${RT_SERVICE_FILE}
}
