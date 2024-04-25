FROM gradle:jdk17 AS builder
COPY . /home/gradle/project
WORKDIR /home/gradle/project
RUN gradle build

FROM openjdk:17
EXPOSE 8080
WORKDIR /home/gradle/project
COPY --from=builder /home/gradle/project/build/libs/Worker.jar ./Worker.jar
CMD ["java", "-jar", "./Worker.jar", "rabbitmq"]



#FROM gradle:jdk17
#EXPOSE 8080
#COPY . /home/gradle/project
#WORKDIR /home/gradle/project
#
#CMD ["java", "-jar", "./build/libs/Worker.jar", "manager:8081"]
#CMD ["gradle", "build"]