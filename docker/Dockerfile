FROM ubuntu:20.04

ENV DEBIAN_FRONTEND=noninteractive

RUN set -xe \
	&& apt-get update \
	&& apt-get install -y vim curl wget git openjdk-11-jdk dos2unix ffmpeg fluidsynth normalize-audio lame timidity lilypond imagemagick openssh-server gradle;

RUN mkdir /var/run/sshd
COPY .ssh /root/.ssh
RUN chmod 700 /root/.ssh && chmod 600 /root/.ssh/authorized_keys
COPY lily.sh /root/.
COPY imagemagick-policy.xml /etc/ImageMagick-6/policy.xml
RUN chmod 755 /root/lily.sh

EXPOSE 22
CMD ["/usr/sbin/sshd", "-D"]

