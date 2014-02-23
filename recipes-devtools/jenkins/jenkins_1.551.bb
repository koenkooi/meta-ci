SUMMARY = "Continuous Integration and Job Scheduling Server"

LICENSE = "MIT & Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58"

SRC_URI = "http://mirrors.jenkins-ci.org/war/${PV}/jenkins.war \
           file://jenkins-server.service \
          "
SRC_URI[md5sum] = "47900d167b1579bc1fa70d88dc529aa8"
SRC_URI[sha256sum] = "d3719d0614b924ad6037fc24155c19580df80c099cec66cf41827f5c324bca96"

inherit allarch systemd useradd

do_install() {
    install -d ${D}${datadir}/jenkins
    install -m0644 ${WORKDIR}/jenkins.war ${D}${datadir}/jenkins/

    install -d ${D}${systemd_unitdir}/system
    install -m0644 ${WORKDIR}/jenkins-server.service ${D}${systemd_unitdir}/system
    sed -i -e s:/usr/share:${datadir}: ${D}${systemd_unitdir}/system/jenkins-server.service
}

PACKAGES =+ "jenkins-common"
FILES_jenkins-common = "${datadir}/jenkins/jenkins.war"

PACKAGES =+ "jenkins-server" 
FILES_jenkins-server = "${systemd_unitdir}"

USERADD_PACKAGES = "${PN}-server"
USERADD_PARAM_${PN}-server = "--system --create-home --home-dir ${localstatedir}/lib/jenkins jenkins"

SYSTEMD_PACKAGES = "${PN}-server"
SYSTEMD_SERVICE_jenkins-server = "jenkins-server.service"

# From the debian packages, not sure about psmisc.
RDEPENDS_jenkins-server = "openjdk-7-jre procps file psmisc jenkins-common"
