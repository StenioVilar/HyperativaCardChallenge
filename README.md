# Hyperativa Card Challenge
## Descrição
Desafio para a empresa Hyperativa

O enunciado completo do desafio pode ser acessado no seguinte link: [Enunciado do Desafio](https://github.com/hyperativa/back-end/blob/master/README.md)
Porém feito com Java Spring boot com MySql.

## Pré-requisitos
Antes de começar, certifique-se de ter instalado:
- [Docker](https://www.docker.com/)
- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- Uma IDE ou ambiente para rodar a aplicação Spring Boot (exemplo: IntelliJ, Eclipse ou terminal com `Maven`).

## Configuração e execução do projeto
1. **Subir os serviços do Docker (Banco de Dados):**
   No terminal, dentro do diretório do projeto, execute o comando:
``` bash
   docker-compose up -d
```
Este comando irá criar o banco de dados e configurar os dados iniciais, incluindo o registro do usuário.
1. **Iniciar a aplicação Spring Boot:**
``` bash
   mvn spring-boot:run
```

2. **Autenticação nos endpoints:**
   Para acessar os endpoints protegidos, primeiro você precisa autenticar. Utilize o seguinte payload no endpoint `/auth`:
   **Requisição POST para autenticação:**
``` http
   {
       "username": "Stenio",
       "password": "123456"
   }
```
**Resposta exemplo esperada:**
``` json
   {
        "id": 1,
        "username": "Stenio",
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJTdGVuaW8iLCJpYXQiOjE3Mzg1NjE2MDIsImV4cCI6MTczODY0ODAwMn0.3dGBFJn8YZr7daXK3PNVObTPUMlIRGkYpFdn7ckzrOA",
        "type": "Bearer"
   }
```
3. **Usar os endpoints protegidos:**
   Inclua o token gerado no campo de cabeçalho `Authorization` precedido por `Bearer`.

## Observações
- O registro do usuário `Stenio` com a senha `123456` é automaticamente criado ao subir o serviço com o `docker-compose`.
- Caso encontre problemas para conectar ao banco, certifique-se de que os contêineres estão rodando corretamente:
``` bash
  docker ps
```
- Para o endpoint de insercão em lotes, o arquivo se encontra na raíz do projeto: massa_dados.txt

# Documentação da API

### Swagger UI

A documentação interativa da API está disponível através do Swagger. Você pode acessá-la pela URL abaixo:

[Swagger UI - Interface de Documentação](http://localhost:8080/swagger-ui/index.html#/)

Certifique-se de que o servidor esteja em execução no ambiente local para acessar o link.
