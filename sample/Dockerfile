FROM adoptopenjdk/openjdk11:jre-11.0.11_9-alpine

ENV APP_ENV docker
ENV HOSTNAME ${HOSTNAME}

# copy files files
ADD build/libs/*.jar /app/com.thoughtworks.inspac.integration-sample.jar
ADD run.sh /app/run.sh
RUN chmod +x /app/run.sh

# run app
CMD ["/app/run.sh"]
