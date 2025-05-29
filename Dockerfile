# 빌드 스테이지
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# 소스 코드 복사 및 빌드
COPY pom.xml .
COPY backend/src ./src
RUN mvn clean package -DskipTests

# 실행 스테이지
FROM eclipse-temurin:17-jre
WORKDIR /app

# JAR 파일 복사
COPY target/maplytic-0.0.1-SNAPSHOT.jar app.jar

# CSV 파일 복사
COPY backend/src/main/resources/bus_stops.csv /app/resources/

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
