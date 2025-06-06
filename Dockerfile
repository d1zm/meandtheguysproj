# Этап 1: Сборка проекта с помощью Maven
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Копируем pom.xml и зависимости
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Копируем исходники и собираем jar
COPY src ./src
RUN mvn package -DskipTests

# Этап 2: Минимальный образ только с JDK
FROM openjdk:21-jdk-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный jar из первого этапа
COPY --from=builder /build/target/meandtheguysproj-1.0-SNAPSHOT.jar app.jar

# показываем на каком порте крутится прилага
EXPOSE 8080

# Указываем точку входа
ENTRYPOINT ["java", "-jar", "app.jar"]



