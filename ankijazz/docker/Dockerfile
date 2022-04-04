FROM ubuntu:20.04

ENV DEBIAN_FRONTEND=noninteractive

RUN set -xe \
  && apt-get update \
  && apt-get install -y vim curl wget git dos2unix \
     ffmpeg fluidsynth normalize-audio lame timidity lilypond parallel;

COPY ankijazz.sh /root/.
RUN chmod 755 /root/ankijazz.sh
RUN dos2unix /root/ankijazz.sh

ENTRYPOINT ["/usr/bin/bash", "/root/ankijazz.sh"]

