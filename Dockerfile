FROM nginx:1.18
ADD ./build/dokka/ /usr/share/nginx/html/
ADD ./src/main/resources/static/index.html /usr/share/nginx/html/

EXPOSE 80

