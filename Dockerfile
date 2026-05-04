# ──────────────────────────────────────────
# Stage 1 — build
# ──────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copia o pom.xml primeiro para aproveitar o cache de layers
# (dependências só são re-baixadas se o pom mudar)
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copia o código e compila (pula testes — serão rodados no CI)
COPY src ./src
RUN mvn package -DskipTests -q

# ──────────────────────────────────────────
# Stage 2 — runtime (imagem mínima)
# ──────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Usuário não-root (boas práticas de segurança em K8s)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copia apenas o jar do stage de build
COPY --from=build /app/target/*.jar app.jar

# Porta da aplicação
EXPOSE 8080

# Flags JVM otimizadas para container:
# -XX:+UseContainerSupport   → respeita os limits de CPU/mem do K8s
# -XX:MaxRAMPercentage=75    → usa 75% da RAM do container (não fixa heap)
# -Djava.security.egd        → fonte de entropia mais rápida (reduz startup)
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
