### Loja Virtual - Backend

🛒 Loja Virtual – API Back-end com Spring Boot

API REST desenvolvida em Java com Spring Boot, simulando um ambiente real de trabalho, desde o levantamento de requisitos até a implementação de regras de negócio, segurança, integração com APIs externas, relatórios e SQL avançado.

O projeto representa um sistema completo de loja virtual / gestão comercial, com foco em boas práticas, performance, segurança e arquitetura back-end.

🧩 Funcionalidades Principais

Cadastro de Pessoa Física (CPF) e Pessoa Jurídica (CNPJ)

Gestão de empresas com separação de dados

Cadastro de produtos, categorias e marcas

Cadastro e processamento de vendas

Controle de estoque

Cadastro de notas fiscais e itens de nota

Avaliação de produtos

Upload e consulta de imagens de produtos

Exclusão lógica e física de registros

Relatórios com SQL avançado

Envio de e-mails automáticos

Tarefas agendadas

Integração com API de transporte e frete

Segurança com JWT

🛠️ Tecnologias Utilizadas

Java 17

Spring Boot

Spring Data JPA

Spring Security

JWT (JSON Web Token)

Hibernate

Flyway

JUnit 5

Mockito

SQL (PostgreSQL / compatível com outros SGBDs)

REST API

Maven

APIs externas (Receita Federal, Transporte/Frete)

🧱 Arquitetura do Projeto

Arquitetura em camadas:

Controller

Service

Repository

Separação clara de responsabilidades

Uso de DTOs

Validações com @Valid

Tratamento global de exceções

Uso de Profiles (dev / prod)

Controle de versionamento de banco com Flyway

🔐 Segurança

Autenticação e autorização com Spring Security

Implementação de JWT

Filtros de segurança

Tratamento de exceções de acesso

Proteção dos endpoints da API

🧪 Testes

Testes unitários de:

Services

Controllers

Uso de:

JUnit

Mockito

Testes de endpoints REST

Validação de regras de negócio

📊 SQL Avançado e Relatórios

Consultas com:

INNER JOIN

filtros dinâmicos

parâmetros

Relatórios:

Produtos comprados

Produtos com estoque baixo

Compras canceladas

Uso de SQL nativo e JPQL

🔄 Integrações Externas

Consulta de CNPJ (Receita Federal)

Consulta e compra de frete

Emissão de etiquetas de transporte

Envio de e-mails automáticos

Tarefas assíncronas com @Async

Agendamentos com @Scheduled

🗄️ Banco de Dados

Modelagem com Diagrama Entidade-Relacionamento

Criação de:

tabelas

constraints

triggers

Versionamento de banco com Flyway

Compatível com:

PostgreSQL

MySQL

SQL Server

🚀 Como Executar o Projeto
Pré-requisitos

Java 17+

Maven

Banco de dados configurado (PostgreSQL recomendado)

Passos
git clone https://github.com/seu-usuario/nome-do-projeto.git
cd nome-do-projeto
mvn spring-boot:run


A API estará disponível em:

http://localhost:8080

📌 Objetivo do Projeto

Este projeto foi desenvolvido com foco em:

Simular experiência real de mercado

Consolidar conhecimentos em Java e Spring Boot

Aplicar SQL avançado

Trabalhar com segurança, testes e integração

Servir como projeto de portfólio profissional

👨‍💻 Autor

Tiago Simão
Desenvolvedor Java Back-end

GitHub: https://github.com/TiagoSimaodev

LinkedIn: https://www.linkedin.com/in/tiago-simao-685015193/








### Diagrama das classes para a criação das classe Back-end. 





<img width="1564" height="883" alt="Diagrama Finalizado" src="https://github.com/user-attachments/assets/cce2026b-7183-440a-a9a4-b21b3c64b674" />

