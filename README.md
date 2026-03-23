# 🛒 Loja Virtual – API Back-end com Spring Boot

API REST desenvolvida em Java com Spring Boot, simulando um ambiente real de mercado, desde o levantamento de requisitos até a implementação de regras de negócio, segurança, integrações externas, relatórios e SQL avançado.

O projeto representa um sistema completo de **e-commerce / gestão comercial**, com foco em **boas práticas, performance, segurança e arquitetura back-end**.

---

## 🚀 Funcionalidades

- Cadastro de Pessoa Física (CPF) e Jurídica (CNPJ)  
- Gestão de empresas (multi-tenant)  
- Cadastro de produtos, categorias e marcas  
- Processamento de vendas  
- Controle de estoque  
- Notas fiscais e itens de nota  
- Avaliação de produtos  
- Upload e consulta de imagens  
- Exclusão lógica e física  
- Relatórios com SQL avançado  
- Envio de e-mails automáticos  
- Tarefas agendadas  
- Integração com API de frete  
- Segurança com JWT  

---

## 🛠️ Tecnologias

- Java 17  
- Spring Boot  
- Spring Data JPA  
- Spring Security + JWT  
- Hibernate  
- Flyway  
- PostgreSQL  
- JUnit 5 + Mockito  
- Maven  
- APIs externas (Receita Federal, Frete)  

---


## 🧱 Arquitetura

Arquitetura em camadas bem definida:

- **Controller** → entrada da API  
- **Service** → regras de negócio  
- **Repository** → acesso a dados  

Boas práticas aplicadas:

- Uso de DTOs  
- Validações com `@Valid`  
- Tratamento global de exceções  
- Profiles (`dev` / `prod`)  
- Versionamento de banco com Flyway  

---

## 🔐 Segurança

- Autenticação com Spring Security  
- JWT (JSON Web Token)  
- Filtros de autenticação  
- Proteção de endpoints  
- Tratamento de exceções de acesso  

---

## 🧪 Testes

- Testes unitários de Services e Controllers  
- JUnit 5  
- Mockito  
- Testes de endpoints REST  
- Validação de regras de negócio  

---

## 📊 SQL Avançado e Relatórios

Consultas com:

- INNER JOIN  
- Filtros dinâmicos  
- Parâmetros  

Relatórios implementados:

- Produtos comprados  
- Produtos com estoque baixo  
- Compras canceladas

Uso de **SQL nativo e JPQL**

---

## 🔄 Integrações Externas

- Consulta de CNPJ (Receita Federal)  
- Integração com frete e transporte  
- Emissão de etiquetas  
- Envio de e-mails automáticos  
- Processamento assíncrono com `@Async`  
- Agendamentos com `@Scheduled`  

---

## 🗄️ Banco de Dados

- Modelagem com DER  
- Criação de tabelas, constraints e triggers  
- Versionamento com Flyway  

Compatível com:

- PostgreSQL  
- MySQL  
- SQL Server  

---

## 🚀 Como executar

### Pré-requisitos
- Java 17+
- Maven
- PostgreSQL

### Passos
```bash
git clone https://github.com/seu-usuario/nome-do-projeto.git
cd nome-do-projeto
mvn spring-boot:run
```

A API estará disponível em:

👉 http://localhost:8080

---

📌 Objetivo

Este projeto foi desenvolvido para:

Simular um sistema real de mercado
Consolidar conhecimentos em Java e Spring Boot
Aplicar SQL avançado
Trabalhar com segurança, testes e integrações
Servir como projeto de portfólio profissional

---

### Diagrama das classes para a criação das classe Back-end. 

<img width="1564" height="883" alt="Diagrama Finalizado" src="https://github.com/user-attachments/assets/cce2026b-7183-440a-a9a4-b21b3c64b674" />


---

👨‍💻 Autor

Tiago Simao
Desenvolvedor Java Back-end

🔗 GitHub: https://github.com/TiagoSimaodev

💼 LinkedIn: https://www.linkedin.com/in/tiagosimaodev/
