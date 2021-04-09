FROM openjdk:11
ARG JAR_FILE=build/libs/*all.jar
COPY ${JAR_FILE} wst-routes.jar
ENTRYPOINT ["java","-jar","/wst-routes.jar"]