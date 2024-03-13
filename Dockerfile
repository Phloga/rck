FROM openjdk:21
EXPOSE 8080
ADD ./backend/build/libs/backend.jar rck.jar
ENTRYPOINT ["java","-jar","rck.jar"]