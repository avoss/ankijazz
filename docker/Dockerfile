FROM ubuntu:20.04

ENV DEBIAN_FRONTEND=noninteractive

RUN set -xe \
	&& apt-get update \
	&& apt-get install -y vim curl wget git openjdk-11-jdk dos2unix ffmpeg fluidsynth normalize-audio lame timidity lilypond openssh-server;

RUN mkdir /var/run/sshd
COPY .ssh /root/.ssh
RUN chmod 700 /root/.ssh && chmod 600 /root/.ssh/authorized_keys
COPY lily.sh /root/.
RUN chmod 700 /root/lily.sh

EXPOSE 22
CMD ["/usr/sbin/sshd", "-D"]

