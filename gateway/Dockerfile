# ================================
# STAGE 1 - Build
# ================================
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /build

# Cache de dependências
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

RUN chmod +x mvnw
RUN ./mvnw -B -q dependency:go-offline

# Código-fonte
COPY src src

# Build do jar
RUN ./mvnw -B clean package -DskipTests


# ================================
# STAGE 2 - Runtime
# ================================
FROM eclipse-temurin:17-jre-alpine

# JVM preparada para container
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75"

# Usuário não-root
RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

# Copia o jar final
COPY --from=builder /build/target/*SNAPSHOT.jar app.jar

EXPOSE 8080

USER spring

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
