FROM openjdk:21-jdk-slim
VOLUME /tmp
EXPOSE 6082
COPY target/*.jar weche-apis.jar
ENTRYPOINT ["java","-jar","weche-apis.jar"]


#version: '3.9'
#
#services:
#  back-office:
#    image: sineb/back-office:latest
#    ports:
#     - "88:88"
#    restart: always
#    environment:
#     - NODE_ENV=production
