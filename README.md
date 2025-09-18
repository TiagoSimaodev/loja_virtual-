Loja Virtual - Backend

📌 Descrição

API backend de uma Loja Virtual desenvolvida em Java com Spring Boot, projetada para gerenciar clientes, produtos, pedidos e regras de negócio. 
A aplicação segue arquitetura em camadas (Controller, Service, Repository), com segurança, validações, testes e agendamentos, 
pronta para integração com frontend ou outros sistemas.

📌 Funcionalidades

Cadastro e gerenciamento de clientes (Pessoa Física e Jurídica).

Cadastro e gerenciamento de produtos e categorias.

Processos assíncronos e envio de e-mails.

Agendamento de tarefas com @Scheduled.

Validações automáticas de CPF e CNPJ com Spring Validation (@Valid).

Autenticação e autorização via JWT.

Testes unitários e de integração com JUnit e Mockito.

Migrações de banco com Flyway.

Performance otimizada para operações CRUD.

📌 Tecnologias

Java 17

Spring Boot 

Spring Data JPA

Spring Security com JWT

JUnit / Mockito

Flyway

PostgreSQL / PLpgSQL

Maven

src/main/java/com/loja_virtual
├── controller       # Endpoints REST
├── service          # Regras de negócio
├── repository       # Repositórios JPA
├── model            # Entidades do banco
├── dto              # Objetos de transferência de dados
├── security         # Configurações (Security, JWT, Profiles) 
├── exception        # Tratamento de exceções customizadas
└── Util             # validar cpf e cnpj
└── enums            #status 

📌 Como Executar 

Clone o repositório:

Configure o banco de dados em application.properties

Execute o projeto: 

Acesse os endpoints via Postman, Insomnia ou frontend integrado

📌 Próximos Recursos

Relatórios avançados e dashboards.

Integração com APIs de pagamento (Juno, Asaas) e transporte.

Emissão de Nota Fiscal Eletrônica.

Disparo de campanhas de e-mail marketing.

Deployment em AWS com HTTPS.








Diagrama das classes para a criação das classe Back-end. 





<img width="1564" height="883" alt="Diagrama Finalizado" src="https://github.com/user-attachments/assets/cce2026b-7183-440a-a9a4-b21b3c64b674" />

