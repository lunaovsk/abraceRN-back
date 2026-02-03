# API Abrace RN - Sistema de Gestão de Estoque

Sistema de gestão de estoque desenvolvido para a ONG **Abrace RN**, que
auxilia mulheres em situação de vulnerabilidade no pós-parto, garantindo
a distribuição organizada de itens essenciais para recém-nascidos.

## Sobre o Projeto

Esta API fornece endpoints para gerenciar itens de doação (roupas,
acessórios, higiene e alimentação) e montar kits personalizados para as
famílias atendidas pela ONG.

### Funcionalidades Principais

-   **Gestão de Itens**: Cadastro, listagem, atualização e exclusão de
    itens
-   **Controle de Estoque**: Controle de quantidades e tipos de itens
-   **Sistema de Kits**: Montagem automática de kits (enxoval e
    higiene)
-   **Cálculo Inteligente**: Verificação de disponibilidade e
    identificação de itens limitantes
-   **Documentação Automática**: API documentada com Swagger/OpenAPI

## Arquitetura

### Tecnologias Utilizadas

-   Java 21
-   Spring Boot 3.5.6
-   Spring Data JPA
-   MySQL
-   Flyway
-   SpringDoc OpenAPI
-   Maven

### Padrões de Projeto

-   Strategy Pattern
-   DTO Pattern
-   Repository Pattern
-   Factory Pattern

## Estrutura do Projeto
```
src/main/java/abraceumrn/com/api/
├── controller/
│ ├── ItemsController.java\
│ └── KitsController.java\
├── domain/
│ ├──dto/
│ ├── enumItem/
│ ├── items/
│ ├── repository/
│ └── strategy/
├── service/
├── infra/
│ ├── exception/
│ └── security/
└── Application.java
```
---

## Como Executar

### Pré-requisitos

- Java 21
- Maven 3.6+
- MySQL 8.0+

### Configuração

1. Clone o repositório
2. Configure o banco de dados
3. Ajuste application.properties
4. Execute `mvn spring-boot:run`

## Endpoints Principais

### Gestão de Itens

POST /items\
GET /items/all-items\
PUT /items/atualizar/{id}\
DELETE /items/deletar/{id}\
GET /items/total

### Gestão de Kits

POST /kit/calcular\
PUT /kit/gerar-kits

## Tipos de Itens

ROUPA, ACESSORIO, HIGIENE, ALIMENTACAO

## Documentação

http://localhost:8080/swagger-ui.html

## Contribuição

Fork, branch, commit, PR.

## Licença

Projeto destinado à ONG Abrace RN.
