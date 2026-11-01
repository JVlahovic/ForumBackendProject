# ---- build ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw
RUN ./mvnw -q -DskipTests package

# ---- runtime ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","app.jar"]