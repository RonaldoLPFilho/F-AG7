# Serviço de Autenticação

Este é um microsserviço de autenticação que utiliza Spring Boot, Spring Security e JWT para gerenciar autenticação de usuários.

## Requisitos

- Docker e Docker Compose
- Java 17
- Maven

## Configuração do Banco de Dados

O projeto utiliza MySQL como banco de dados. Para iniciar o container do MySQL, execute:

```bash
docker-compose up -d
```

Isso irá:
1. Criar um container MySQL na porta 3306
2. Criar um banco de dados chamado `auth_service`
3. Configurar o usuário root com senha `root`
4. Criar um volume para persistir os dados
5. Configurar a rede para comunicação entre containers

Para parar o container:

```bash
docker-compose down
```

Para parar o container e remover os volumes (isso apagará todos os dados):

```bash
docker-compose down -v
```

## Configuração do Aplicativo

O arquivo `application.yml` já está configurado para se conectar ao MySQL rodando no Docker:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/auth_service
    username: root
    password: root
```

## Endpoints Disponíveis

1. Registro de usuário:
```
POST /api/auth/register
{
    "username": "usuario",
    "password": "senha",
    "email": "usuario@email.com",
    "fullName": "Nome Completo",
    "role": "ADMIN"
}
```

2. Login:
```
POST /api/auth/login
{
    "username": "usuario",
    "password": "senha"
}
```

3. Validação de token:
```
POST /api/auth/validate
Header: Authorization: Bearer <token>
```

## Observações

- O container do MySQL está configurado para usar o plugin de autenticação nativo do MySQL para maior compatibilidade
- Os dados do banco são persistidos em um volume Docker chamado `auth_service_mysql_data`
- A rede Docker `auth_service_network` é criada para permitir comunicação entre containers 