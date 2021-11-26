FROM maven:openjdk AS build
COPY src /app/src
COPY pom.xml /app
RUN mvn -f /app/pom.xml clean package

FROM openjdk
COPY --from=build /app/target/cryp-0.0.1-SNAPSHOT.jar /cryp-0.0.1-SNAPSHOT.jar
#COPY target/cryp-0.0.1-SNAPSHOT.jar /cryp-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/cryp-0.0.1-SNAPSHOT.jar"]