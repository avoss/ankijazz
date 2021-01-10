FROM ubuntu:20.04

ENV DEBIAN_FRONTEND=noninteractive

RUN set -xe \
  && apt-get update \
  && apt-get install -y vim curl wget git openjdk-11-jdk dos2unix \
     ffmpeg fluidsynth normalize-audio lame timidity lilypond imagemagick \
     sqlite openssh-server gradle parallel;

RUN mkdir /var/run/sshd
COPY .ssh /root/.ssh
RUN chmod 700 /root/.ssh && chmod 600 /root/.ssh/authorized_keys

COPY anki.sh /root/.
RUN chmod 755 /root/anki.sh
RUN dos2unix /root/anki.sh

COPY imagemagick-policy.xml /etc/ImageMagick-6/policy.xml

# install documentation and other data for system where users log-in
# RUN apt-get install -y man-db
# RUN unminimize -y

EXPOSE 22
CMD ["/usr/sbin/sshd", "-D"]

