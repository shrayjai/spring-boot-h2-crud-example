# Spring Boot CRUD [Example]

Example code for Spring Boot CRUD.

## Run locally

H2 properties are under `/resources/application.properties`.  

```
server.port=8080
server.servlet.context-path=/banking

spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update

spring.h2.console.enabled=true
spring.h2.console.path=/h2
```

Run the project in an IDE or by this command on the command line:

```
mvn spring-boot:run
```
