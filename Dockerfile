FROM nginx:1.18
ADD ./build/dokka/ /usr/share/nginx/html/

RUN sed -i "s#\/usr\/share\/nginx\/html#\/usr\/share\/nginx\/html\/sea-oidc\/#g" /etc/nginx/conf.d/default.conf
EXPOSE 80

