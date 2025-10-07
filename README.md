# URL Shortener
This is a URL Shortener Application following [Tomato Architecture](https://tomato-architecture.github.io/).

[![Maven Build](https://github.com/tomato-architecture/url-shortener-tomato-architecture/actions/workflows/maven.yml/badge.svg)](https://github.com/tomato-architecture/url-shortener-tomato-architecture/actions/workflows/maven.yml)

## Architecture

![url-shortener-tomato-architecture.png](url-shortener-tomato-architecture.png)

## Modules
* url-shortener-core: The core business logic of the URL Shortener application.
* url-shortener-api: The REST API of the URL Shortener application.
* url-shortener-web: The web application of the URL Shortener application.
* url-shortener-cli: The CLI app of the URL Shortener application.

## Tech Stack
* Java
* Spring Boot
* Spring Security
* Spring Data JPA
* PostgreSQL
* FlywayDb Migrations
* Thymeleaf
* Bootstrap CSS

## Local Development

### Prerequisites
* Java 25
* Docker and Docker Compose

```shell
$ git clone https://github.com/tomato-architecture/url-shortener-tomato-architecture.git
$ cd url-shortener-tomato-architecture
$ ./mvnw clean verify
```

### Start URL Shortener Web Application
Run `TestUrlShortenerWebApplication.java` from your IDE. This will also start the required services like PostgreSQL, Redis, etc. using Testcontainers.

### Start URL Shortener REST API Application
Run `TestUrlShortenerApiApplication.java` from your IDE. This will also start the required services like PostgreSQL, Redis, etc. using Testcontainers.

Swagger UI: http://localhost:8080/swagger-ui/index.html

Use the following credentials to login:
* Admin User: `admin@gmail.com/admin`
* Regular User: `siva@gmail.com/secret`

### Start URL Shortener CLI Application
Run `UrlShortenerCliApplication.java` from your IDE.

```shell
shell:> help
shell:> list
shell:> list -p 1
shell:> list --page 2
shell:>create -u https://sivalabs.dev
shell:>create --url https://sivalabs.in
shell:> exit
```
