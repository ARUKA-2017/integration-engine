FROM  java:8-jdk-alpine
MAINTAINER nilesh.j@platformer.com

#RUN apt-get update
#RUN apt-get install software-properties-common  -y
#RUN add-apt-repository ppa:webupd8team/java -y
#RUN apt-get update
# Install Java.
RUN apk update && apk add nginx
RUN apk add --update bash
RUN apk add maven --update-cache --repository http://dl-4.alpinelinux.org/alpine/edge/community/ --allow-untrusted \
    	&& rm -rf /var/cache/apk/*

RUN apk add openrc --no-cache

RUN rc-update add nginx default

ENV MAVEN_HOME /usr/share/java/maven-3
ENV PATH $PATH:$MAVEN_HOME/bin

#RUN \
#    echo oracle-java7-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections && \
#    apt-get install -y  --allow-unauthenticated  oracle-java8-installer && \
#    echo oracle-java7-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections && \
#    apt-get clean

#RUN apt-get install oracle-java8-installer -y --allow-unauthenticated

#
#ENV VOWL_DIR "/usr/share/nginx/html/"
#ENV NGINX_CONF_DIR "/etc/nginx/conf.d"

#RUN  apt-get install oracle-java8-installer -y
#RUN apt-get install mvn -y
#
#COPY nginx.conf $NGINX_CONF_DIR/
#
#COPY . $APP_DIR
#RUN chown -R root: $APP_DIR
#ENTRYPOINT   /bin/bash   /usr/sbin/nginx

EXPOSE 80
CMD ["bash"]
