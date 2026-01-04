FROM sbtscala/scala-sbt:eclipse-temurin-17.0.15_6_1.11.7_3.7.4
WORKDIR /app
COPY . .
RUN sbt compile
CMD ["sbt", "run"]