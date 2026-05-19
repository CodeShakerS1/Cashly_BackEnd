# 💰 Cashly — Backend

API REST do **Cashly**, aplicativo de controle financeiro pessoal. Desenvolvida com Spring Boot e MySQL, containerizada com Docker e protegida por autenticação JWT.

---

## 📑 Índice

- [🧰 Tecnologias](#-tecnologias)
- [🏗️ Arquitetura](#️-arquitetura)
- [⚙️ Configuração do Ambiente](#️-configuração-do-ambiente)
- [🐳 Rodando com Docker](#-rodando-com-docker)
- [📋 Variáveis de Ambiente](#-variáveis-de-ambiente)
- [📖 Documentação da API](#-documentação-da-api)
- [🔒 Autenticação](#-autenticação)
- [🤝 Contribuição](#-contribuição)

---

## 🧰 Tecnologias

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 21 |
| Framework | Spring Boot |
| Banco de dados | MySQL 8.0 |
| ORM | Spring Data JPA / Hibernate |
| Containerização | Docker + Docker Compose |
| Documentação | SpringDoc / Swagger UI |
| Build | Maven (Wrapper) |

---

## 🏗️ Arquitetura

```
src/
└── main/
    └── java/
        └── com/cashly/
            ├── controller/     # Endpoints REST
            ├── service/        # Regras de negócio
            ├── repository/     # Acesso a dados (JPA)
            ├── model/          # Entidades JPA
            ├── dto/            # Data Transfer Objects
```

O projeto segue uma arquitetura em camadas (Controller → Service → Repository), com DTOs para entrada/saída e entidades JPA mapeadas para o MySQL.

---

## ⚙️ Configuração do Ambiente

### Pré-requisitos

- [Docker](https://www.docker.com/) e Docker Compose
- Java 21 (apenas se for rodar sem Docker)
- Maven (ou use o `mvnw` incluído no projeto)

### 1. Clone o repositório

```bash
git clone https://github.com/CodeShakerS1/Cashly.git
cd Cashly/backend
```

### 2. Configure as variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto (mesmo nível do `docker-compose.yml`) com base no exemplo abaixo:

```env
MYSQL_ROOT_PASSWORD=sua_senha_root
MYSQL_DATABASE=cashly
MYSQL_USER=cashly_user
MYSQL_PASSWORD=sua_senha
DB_URL=jdbc:mysql://db:3306/cashly
```

> ⚠️ **Nunca versione o `.env` com dados reais.** O arquivo já deve estar no `.gitignore`.

---

## 🐳 Rodando com Docker

O projeto está totalmente containerizado. Um único comando sobe o banco e a aplicação:

```bash
docker compose up --build
```

O Docker Compose irá:
1. Subir o MySQL 8.0 na porta `3307` do host (mapeada para `3306` internamente)
2. Aguardar o banco ficar saudável via `healthcheck`
3. Fazer o build da aplicação Spring Boot com Maven
4. Subir a API na porta `8080`

Para rodar em segundo plano:

```bash
docker compose up --build -d
```

Para derrubar os containers:

```bash
docker compose down
```

Para derrubar e apagar o volume do banco:

```bash
docker compose down -v
```

---

## 📋 Variáveis de Ambiente

| Variável | Descrição |
|---|---|
| `MYSQL_ROOT_PASSWORD` | Senha do usuário root do MySQL |
| `MYSQL_DATABASE` | Nome do banco de dados criado na inicialização |
| `MYSQL_USER` | Usuário da aplicação |
| `MYSQL_PASSWORD` | Senha do usuário da aplicação |
| `DB_URL` | URL de conexão JDBC (ex: `jdbc:mysql://db:3306/cashly`) |

---

## 📖 Documentação da API

Com os containers rodando, acesse a documentação interativa no navegador:

```
http://localhost:8080/docs
```

A interface do Swagger UI permite visualizar e testar todos os endpoints disponíveis.

```
```

---

## 🤝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/minha-feature`)
3. Faça o commit das suas alterações (`git commit -m 'feat: minha feature'`)
4. Faça o push para a branch (`git push origin feature/minha-feature`)
5. Abra um Pull Request

---

## 👥 Contribuidores

- [Andressa Andrade](https://github.com/dressaDevv)
- [Alexander Emanuel](https://github.com/AlexSm121)
- [Joás Fortunato](https://github.com/JoasFortunato)
- [João Victor da Silva](https://github.com/joaovsilva18)
- [José Elias](https://github.com/eliastk0)
- [Mariana Melo](https://github.com/marianameelo)

---

## 📄 Licença

Projeto desenvolvido para fins acadêmicos e de aprendizado.
