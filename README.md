Meta-mcce

The meta-mcce layer provides a small framework for adding the mbed-cloud-client-example into a Yocto LmP based build.

It will be used instead of the meta-edge layer.

The files contained in this layer also provide some modifications to allow imx8mmevk to boot from SD card.

The MCCE recipe provides a systemd service that will attempt to start the MCCE in /home/fio. 

So far it has not been possible to compile the MCCE directly inside a Yocto recipe, so this layer DOES NOT compile the MCCE. That will ned to be done using an aarch64 cross compiler.

