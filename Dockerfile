FROM maven:3.8.6-openjdk-18
WORKDIR /app
COPY . .
RUN mvn package
CMD ["bash", "-c", "java -jar /app/target/*.jar"]
