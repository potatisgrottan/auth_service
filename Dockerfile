# ---- Build stage ----
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Kopiera pom.xml och hämta dependencies först (cache)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Kopiera källkod och bygg jar
COPY src ./src
RUN mvn -B -DskipTests package


# ---- Runtime stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app

# Kopiera jar från build-steget
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
