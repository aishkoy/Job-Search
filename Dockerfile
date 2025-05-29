FROM maven:3.9-amazoncorretto-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY data ./data
RUN mvn clean package -DskipTests

FROM amazoncorretto:21
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
COPY --from=build /app/data ./data
EXPOSE 8888
ENV JAVA_OPTS="-Xmx1g -Xms512m"
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]