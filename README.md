# Alura Food - Microsserviços com Spring Boot

Sistema de gestão de pedidos e pagamentos desenvolvido com arquitetura de microsserviços, utilizando Java, Spring Boot e Spring Cloud. O projeto demonstra a decomposição de um monólito em serviços independentes, comunicação entre microsserviços, service discovery, API gateway e containerização com Docker.

## 📋 Sobre o Projeto

Este projeto foi desenvolvido como parte da formação de Microsserviços da Alura, com o objetivo de aplicar conceitos fundamentais de sistemas distribuídos em um cenário real. A aplicação gerencia pedidos e pagamentos de forma desacoplada, com cada serviço possuindo sua própria responsabilidade e banco de dados.

## 🏗️ Arquitetura

O sistema é composto por cinco componentes principais:

- **Eureka Server**: Service Discovery para registro e descoberta automática dos microsserviços
- **API Gateway**: Ponto único de entrada, centraliza o roteamento das requisições
- **Microsserviço de Pedidos**: Gerencia pedidos e se comunica com o serviço de pagamentos
- **Microsserviço de Pagamentos**: Processa pagamentos de forma independente
- **MySQL**: Banco de dados compartilhado (pode ser isolado por serviço em ambientes de produção)

Todos os serviços são containerizados e comunicam-se através de uma rede Docker interna.

## 🛠️ Tecnologias

- **Java 17**
- **Spring Boot** - Framework base dos microsserviços
- **Spring Cloud Netflix Eureka** - Service Discovery
- **Spring Cloud Gateway** - API Gateway
- **Resilience4J** - Circuit Breaker e resiliência
- **MySQL** - Banco de dados
- **Docker & Docker Compose** - Containerização e orquestração
- **Maven** - Gerenciamento de dependências

## 📁 Estrutura do Repositório

```
.
├── eureka-server/          # Service Discovery
├── gateway/                # API Gateway
├── pedidos-ms/             # Microsserviço de Pedidos
├── pagamentos-ms/          # Microsserviço de Pagamentos
├── docker-compose.yml      # Orquestração dos containers
└── README.md
```

Cada microsserviço contém seu próprio `Dockerfile` e configurações independentes.

## 🚀 Como Executar

### Pré-requisitos

- Docker e Docker Compose instalados
- Git
- (Opcional) Java 17 e Maven, caso queira buildar localmente

### Executando com Docker Compose

Clone o repositório e execute:

```bash
docker compose up
```

O Docker irá baixar automaticamente as imagens publicadas no Docker Hub e iniciar todos os serviços.

### Build Local (Opcional)

Se preferir buildar as imagens localmente, execute em cada microsserviço:

```bash
./mvnw clean package
docker build -t nome-da-imagem:versao .
```

## 🌐 Endpoints

Após iniciar os containers, os serviços estarão disponíveis em:

- **Eureka Server**: http://localhost:8081
- **API Gateway**: http://localhost:8080
- **Pedidos MS**: http://localhost:8082
- **Pagamentos MS**: http://localhost:8083

### Exemplo de Requisição

Todas as requisições devem ser feitas através do API Gateway:

```bash
# Listar pedidos
curl http://localhost:8080/pedidos-ms/pedidos

# Criar pagamento
curl -X POST http://localhost:8080/pagamentos-ms/pagamentos \
  -H "Content-Type: application/json" \
  -d '{"valor": 100.00, "nome": "João Silva"}'
```

## 🐳 Docker

### Imagens Publicadas

As imagens Docker estão disponíveis no Docker Hub:

- `martnsdev/server-eureka:1.0`
- `martnsdev/gateway-ms:1.0`
- `martnsdev/pedidos-ms:1.5`
- `martnsdev/pagamentos-ms:1.0`
- `martnsdev/mysql-ms:1.0`

### Dockerfile Multi-Stage

Cada serviço utiliza um Dockerfile multi-stage para otimizar o tamanho da imagem:

```dockerfile
# STAGE 1 - Build
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /build
COPY pom.xml mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw
RUN ./mvnw -B dependency:go-offline
COPY src src
RUN ./mvnw -B clean package -DskipTests

# STAGE 2 - Runtime
FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S spring && adduser -S spring -G spring
WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar
EXPOSE 8080
USER spring
ENTRYPOINT ["java","-XX:+UseContainerSupport","-XX:MaxRAMPercentage=75","-jar","app.jar"]
```

### Comunicação entre Containers

**Importante**: Dentro do Docker, os microsserviços não se comunicam usando `localhost`. Eles utilizam o nome do container definido no `docker-compose.yml`.

Exemplo de configuração de variável de ambiente:

```yaml
environment:
  EUREKA_CLIENT_SERVICEURL_DEFAULTZONE: http://server:8080/eureka
```

## 🛡️ Resiliência

O projeto implementa padrões de resiliência utilizando Resilience4J:

- **Circuit Breaker**: Previne falhas em cascata
- **Fallback**: Respostas alternativas em caso de falha
- **Retry**: Tentativas automáticas de requisições

## 🔧 Configuração

### Variáveis de Ambiente

Os microsserviços são configurados via variáveis de ambiente no `docker-compose.yml`:

```yaml
environment:
  SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/alurafood
  SPRING_DATASOURCE_USERNAME: root
  SPRING_DATASOURCE_PASSWORD: 5517
  EUREKA_CLIENT_SERVICEURL_DEFAULTZONE: http://server:8080/eureka
```

## 📚 Aprendizados

Este projeto aborda:

- Decomposição de monólitos em microsserviços
- Comunicação síncrona entre serviços
- Service Discovery com Eureka
- Roteamento centralizado com API Gateway
- Balanceamento de carga
- Tratamento de falhas e resiliência
- Containerização e orquestração com Docker

## ⚠️ Nota

Este projeto foi desenvolvido para fins educacionais. Para ambientes de produção, considere implementar:

- Autenticação e autorização (OAuth2, JWT)
- Observabilidade (logs centralizados, métricas, tracing)
- Separação de bancos de dados por serviço
- Secrets management
- CI/CD pipeline
- Health checks e liveness probes

## 📖 Recursos Adicionais

- [Formação Spring Framework - Alura](https://cursos.alura.com.br/formacao-spring-framework)
- [Microsserviços: padrões de projeto - Alura](https://cursos.alura.com.br/course/microsservicos-padroes-projeto)
- [Spring Cloud Documentation](https://spring.io/projects/spring-cloud)

## 📄 Licença

Projeto desenvolvido durante a formação da Alura. Código criado por Matheus Martins

- LinkedIn: [@matheusmartnsdev](https://www.linkedin.com/in/matheusmartnsdev/)
- GitHub: [@MartnsDev](https://github.com/MartnsDev)


---

⭐ Se este projeto foi útil para você, considere deixar uma estrela!
