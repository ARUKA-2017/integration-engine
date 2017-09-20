FROM nginx
MAINTAINER nilesh.j@platformer.com


ENV VOWL_DIR "/usr/share/nginx/html/"
ENV NGINX_CONF_DIR "/etc/nginx/conf.d"

RUN apt-get update
#RUN  apt-get install oracle-java8-installer -y
#RUN apt-get install mvn -y
#
#COPY nginx.conf $NGINX_CONF_DIR/
#
#COPY . $APP_DIR
#RUN chown -R root: $APP_DIR

