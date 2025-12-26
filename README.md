# Microsserviços com Java e Spring Boot – Alura Food

O foco deste projeto é compreender, na prática, como microsserviços se comunicam entre si, como são organizados e como o ecossistema Spring ajuda a resolver desafios reais de sistemas distribuídos.

---

## 🚀 Tecnologias Utilizadas
```
Java 17+
Spring Boot
Spring Cloud (Eureka, Gateway)
Resilience4J
MySQL
Maven
Git e GitHub
Docker
```
---

## 🎯 Objetivos do Projeto

Aplicar, na prática, os principais conceitos de arquitetura de microsserviços utilizando o ecossistema Spring.

Principais objetivos:
```
Decompor um sistema monolítico em microsserviços
Garantir responsabilidade única por serviço
Implementar comunicação síncrona entre serviços
Utilizar Service Discovery e API Gateway
Trabalhar tolerância a falhas e resiliência
```
---

# Microsserviços com Docker e Docker Compose

Este projeto demonstra, na prática, como configurar um ambiente completo de microsserviços com Java e Spring Boot, utilizando Docker, Docker Hub e Docker Compose.
O objetivo é mostrar o fluxo real utilizado em projetos profissionais, desde o build das imagens até a execução integrada de todos os serviços.

---

## 🧱 Arquitetura do Projeto

A aplicação é composta por:
```
Eureka Server para Service Discovery
MySQL como banco de dados
Microsserviço de pedidos
Microsserviço de pagamentos
API Gateway como ponto único de entrada
```
Todos os serviços rodam em containers Docker e se comunicam por meio de uma rede interna.
---

## 📦 Pré-requisitos

Antes de começar, é necessário ter instalado:
```
Docker
Docker Compose
Git
Java 17
Maven
```
Verifique as instalações com:

```
docker --version
docker compose version
git --version
java --version
```

---

## 🔨 Build da Aplicação Spring Boot

Cada microsserviço deve gerar seu próprio arquivo JAR antes da criação da imagem Docker.

Na raiz de cada serviço, executei:
```
./mvnw clean package
```

Ao final do processo, o JAR estará disponível na pasta `target`.

---

## 🐳 Dockerfile Padrão dos Serviços

Cada microsserviço utiliza um Dockerfile simples como base:
```
# STAGE 1 - Build
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

# STAGE 2 - Runtime
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
```

Esse Dockerfile cria uma imagem com Java 17 e executa a aplicação Spring Boot.

---

## 📦 Build da Imagem Docker

Com o JAR gerado, criei uma imagem Docker do serviço.

Exemplo para o microsserviço de pedidos:
```
docker build -t martnsdev/pedidos-ms:1.0 .
```
Utilizei o seu próprio usuário do Docker Hub.
![Docker Hub - Repositorios](Img/Docker-Repositories.png)
Padrão adotado para nomear as imagens:
```
usuario-docker/nome-do-servico:versao
```

Repeti esse processo para todos os microsserviços.

---

## 🔐 Login no Docker Hub

Antes de enviar as imagens, faça login no Docker Hub:
```
docker login
```
Informei meu usuário e senha.
Precisei logar com GitHub,então criei um token de acesso: 
![Logar no Docker — GitHub](Img/Login-dockerHub-with-github.png)
Read & Write.

---

## 🚀 Push das Imagens para o Docker Hub

Após o build, enviei as imagens:
```
docker push martnsdev/server-eureka:1.0
docker push martnsdev/mysql-ms:1.0
docker push martnsdev/pedidos-ms:1.5
docker push martnsdev/pagamentos-ms:1.0
docker push martnsdev/gateway-ms:1.0
```
Após o push, qualquer máquina poderá baixar essas imagens.

---

## 🧩 Docker Compose do Projeto

O Docker Compose é responsável por subir todos os serviços conectados na mesma rede.

services:
```
server:
image: martnsdev/server-eureka:1.0
container_name: server
ports:
- "8081:8080"
networks:
- alurafood-net

mysql:
image: martnsdev/mysql-ms:1.0
container_name: mysql-ms
environment:
MYSQL_ROOT_PASSWORD: 5517
MYSQL_DATABASE: alurafood
ports:
- "3306:3306"
volumes:
- mysql_data:/var/lib/mysql
networks:
- alurafood-net

pedidos:
image: martnsdev/pedidos-ms:1.5
container_name: pedidos-ms
depends_on:
- mysql
- server
environment:
SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/alurafood
SPRING_DATASOURCE_USERNAME: root
SPRING_DATASOURCE_PASSWORD: 5517
EUREKA_CLIENT_SERVICEURL_DEFAULTZONE: http://server:8080/eureka
ports:
- "8082:8080"
networks:
- alurafood-net

pagamentos:
image: martnsdev/pagamentos-ms:1.0
container_name: pagamentos-ms
depends_on:
- mysql
- server
environment:
SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/alurafood
SPRING_DATASOURCE_USERNAME: root
SPRING_DATASOURCE_PASSWORD: 5517
EUREKA_CLIENT_SERVICEURL_DEFAULTZONE: http://server:8080/eureka
ports:
- "8083:8080"
networks:
- alurafood-net

gateway:
image: martnsdev/gateway-ms:1.0
container_name: gateway-ms
depends_on:
- server
- pedidos
- pagamentos
environment:
EUREKA_CLIENT_SERVICEURL_DEFAULTZONE: http://server:8080/eureka
ports:
- "8080:8080"
networks:
- alurafood-net

volumes:
mysql_data:

networks:
alurafood-net:
driver: bridge
```
---

## ▶️ Subindo o Ambiente Completo

Com todas as imagens publicadas no Docker Hub, execute:

```
docker compose up
```
![Logar no docker — GitHub](Img/Docker-compose.png)
O Docker irá baixar as imagens automaticamente e iniciar todos os serviços.

---

## 🌐 Portas dos Serviços

Eureka Server:
```
http://localhost:8081
```
API Gateway:
```
http://localhost:8080
```
Pedidos:
```
http://localhost:8082
```
Pagamentos:
```
http://localhost:8083
```
Após rodar o projeto, abra o EurekaServer:
```
http://localhost:8081
```
![Eureka Server](Img/Eureka-server.png)

---

## ⏹️ Parando o Ambiente

Para parar os containers:
```
docker compose down
```

Para remover também os volumes e dados do banco:
```
docker compose down -v
```
---

## ⚠️ Observação Importante

Dentro do Docker, os microsserviços não se comunicam usando `localhost`.
Eles utilizam o nome do contêiner definido no Docker Compose.

Esse é um dos pontos mais importantes em ambientes containerizados e costuma gerar erros em quem está começando.

---

## 🧩 Arquitetura Implementada

###🔹Microsserviço de Pagamentos
```
API REST com Spring Boot
Banco de dados próprio utilizando MySQL
Responsável pelo processamento de pagamentos
```
###🔹Microsserviço de Pedidos
```
Comunicação síncrona com o serviço de pagamentos
Balanceamento de carga entre múltiplas instâncias
Integração via Service Discovery
```
###🔹Service Discovery
```
Implementado com Eureka (Spring Cloud Netflix)
Registro e descoberta automática dos microsserviços
```
###🔹API Gateway
```
Implementado com Spring Cloud Gateway
Ponto único de entrada da aplicação
Centraliza o roteamento das requisições
```
###🔹Resiliência
```
Circuit Breaker e Fallback
Utilização do Resilience4J
Tratamento de falhas entre serviços
```
---

## 📚 Contexto do Projeto

O projeto faz parte da formação de Microsserviços com Spring da Alura e parte de um cenário onde a aplicação Alura Food era originalmente um monólito, passando por um processo de decomposição em microsserviços.

Cursos relacionados:
- [Formação Spring Framework](https://cursos.alura.com.br/formacao-spring-framework)
- [Microsserviços: padrões de projeto](https://cursos.alura.com.br/course/microsservicos-padroes-projeto)
- [Fundamentos de Microsserviços](https://cursos.alura.com.br/course/fundamentos-microsservicos-aprofundando-conceitos)
- [Microsserviços na prática: entendendo a tomada de decisões](https://cursos.alura.com.br/course/Microsservicos-pratica-tomada-decisoes)

![Microsserviços com Java e Spring](https://user-images.githubusercontent.com/66698429/169815319-20640ad4-cda0-4868-9728-d380c5fcc799.png)

---

## ⚠️ Aviso
```
Este projeto foi desenvolvido exclusivamente para fins de estudo.
Não representa um sistema pronto para produção sem ajustes adicionais de segurança, observabilidade e infraestrutura.
```
---

## 📄 Licença
```
Este projeto utiliza conteúdo educacional da Alura, respeitando sua licença.
O código foi desenvolvido por Matheus Martins durante o processo de aprendizado.
```
