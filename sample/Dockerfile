FROM openjdk:8-jre-slim

ENV APP_ENV docker
ENV HOSTNAME ${HOSTNAME}

# copy files files
ADD build/libs/*.jar /app/gac-demo.jar
ADD run.sh /app/run.sh
RUN chmod +x /app/run.sh

# run app
CMD ["/app/run.sh"]
