# ================================
# STAGE 1 - Build
# ================================
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /build

# Copia só o que muda menos primeiro (cache do Docker)
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Dá permissão de execução pro Maven Wrapper
RUN chmod +x mvnw

# Baixa dependências (cache)
RUN ./mvnw -B -q dependency:go-offline

# Agora sim o código
COPY src src

# Gera o jar
RUN ./mvnw -B -q clean package -DskipTests


# ================================
# STAGE 2 - Runtime
# ================================
FROM eclipse-temurin:17-jre-alpine

# Usuário não-root (obrigatório em ambiente sério)
RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

# Copia só o jar final
COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

USER spring

# JVM preparada para container
ENTRYPOINT ["java","-XX:+UseContainerSupport","-XX:MaxRAMPercentage=75","-jar","app.jar"]
