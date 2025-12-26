# Microsserviços com Java e Spring Boot – Alura Food

Projeto desenvolvido por **Matheus Martins** como parte dos estudos em **arquitetura de microsserviços com Java e Spring Boot**, baseado no curso **Microsserviços na prática implementando com Java e Spring** da Alura.

O foco deste projeto é entender como microsserviços se comunicam, como são organizados e como o ecossistema Spring ajuda a resolver problemas reais de sistemas distribuídos.

---

## Objetivos do Projeto

Aplicar, na prática, os principais conceitos de **arquitetura de microsserviços** utilizando o ecossistema Spring.

Principais objetivos:
```
- Decompor um sistema monolítico em microsserviços
- Garantir responsabilidade única por serviço
- Implementar comunicação síncrona entre serviços
- Utilizar service discovery e API Gateway
- Trabalhar tolerância a falhas e resiliência
```
---
## 🧩 Arquitetura Implementada

A arquitetura do projeto é composta pelos seguintes componentes:

### 🔹 Microsserviço de Pagamentos
```
- API REST com Spring Boot
- Banco de dados próprio utilizando MySQL
- Responsável pelo processamento de pagamentos
```
### 🔹 Microsserviço de Pedidos
```
- Comunicação síncrona com o serviço de pagamentos
- Balanceamento de carga entre múltiplas instâncias
- Integração via Service Discovery
```
### 🔹 Service Discovery
```
- Implementado com Eureka (Spring Cloud Netflix)
- Registro e descoberta automática dos microsserviços
```
### 🔹 API Gateway
```
- Implementado com Spring Cloud Gateway
- Ponto único de entrada da aplicação
- Centraliza o roteamento das requisições
```
### 🔹 Resiliência
```
- Implementação de Circuit Breaker e Fallback
- Utilização do Resilience4J
- Tratamento de falhas entre serviços
```
---
## 🚀 Tecnologias Utilizadas
```
- Java 17+
- Spring Boot
- Spring Cloud (Eureka, Gateway)
- Resilience4J
- MySQL
- Maven
- Git e GitHub
- Docker 
```
---



## 📚 Contexto do Projeto

O projeto faz parte da formação de **Microsserviços com Spring** da Alura e parte de um cenário onde a aplicação **Alura Food** era originalmente um **monólito**, passando por um processo de decomposição em microsserviços.

Cursos que fundamentam este projeto:

- [Formação Spring Framework](https://cursos.alura.com.br/formacao-spring-framework)
- [Microsserviços: padrões de projeto](https://cursos.alura.com.br/course/microsservicos-padroes-projeto)
- [Fundamentos de Microsserviços](https://cursos.alura.com.br/course/fundamentos-microsservicos-aprofundando-conceitos)
- [Microsserviços na prática: entendendo a tomada de decisões](https://cursos.alura.com.br/course/Microsservicos-pratica-tomada-decisoes)
![Microsserviços com Java e Spring](https://user-images.githubusercontent.com/66698429/169815319-20640ad4-cda0-4868-9728-d380c5fcc799.png)
---

## ⚠️ Aviso

Este projeto foi desenvolvido **exclusivamente para fins de estudo**.  
Não representa um sistema pronto para produção sem ajustes adicionais de segurança, observabilidade e infraestrutura.

---

## 📄 Licença
```
Este projeto utiliza o conteúdo educacional da Alura, respeitando sua licença educacional
O código foi desenvolvido por **Matheus Martins** durante o processo de aprendizado, com base nos cursos da plataforma Alura.
```
