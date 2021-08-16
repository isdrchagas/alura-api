# Projeto school

## Sobre o projeto

Esta é uma API simples que tem o objetivo de gerenciar usuários, cursos e matrículas.

## Tecnologias Utilizadas

[![Framework](https://img.shields.io/badge/-Spring%20Framework-brightgreen)](https://docs.spring.io/spring-framework/docs/current/reference/html/) [![Language](https://img.shields.io/badge/-Java%2011-red)](https://docs.oracle.com/en/java/javase/11/index.html) [![ORM](https://img.shields.io/badge/-Hibernate-%2359666c)](https://hibernate.org/) [![Apache Maven](https://img.shields.io/badge/-Maven-%23e36529)](https://maven.apache.org/)

## Funcionalidades

- `GET /users/{username}` obtém os detalhes de um usuário
- `POST /users` adiciona um novo usuário
- `GET /courses` lista os cursos já cadastrado
- `POST /courses` adiciona um novo curso
- `GET /courses/{code}` obtém os detalhes de um curso
- `POST /courses/{courseCode}/enroll` adiciona matricula de um usuário
- `GET /courses/enroll/report ` obtém relatório de matriculas ordenada pela quantidade

## Documentação da API

É possível acessar a documentação da API rodando a aplicação e acessando http://localhost:8080/swagger-ui.html