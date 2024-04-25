#FROM gradle:jdk17
#EXPOSE 8081
#COPY . /home/gradle/project
#WORKDIR /home/gradle/project
#CMD ["gradle", "--stacktrace", "build"]
#CMD ["java", "-jar", "./build/libs/Manager.jar", "worker:8080"]

FROM gradle:jdk17 AS builder
COPY . /home/gradle/project
WORKDIR /home/gradle/project
RUN gradle build

FROM openjdk:17
EXPOSE 8081
WORKDIR /home/gradle/project
COPY --from=builder /home/gradle/project/build/libs/Manager.jar ./Manager.jar
CMD ["java", "-jar", "./Manager.jar", "rabbitmq"]