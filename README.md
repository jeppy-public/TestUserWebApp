# TestUserWebApp
This web application implement simple CRUD operations with a `User` entity

## What's inside
This project is based on the Spring framework as Back End engine and Vaadin as UI engine
Below are packages used in this project:
- Java 21
- Maven 3.9.9
- Spring Boot 3.4
- Spring Web
- Spring Data JPA
- Jasper Report 7.0.1
- Mockito 4.8.0
- JUnit
- Lombok
- H2 database 2.3.232
- Vaadin 24.6.2

## Installation
The project is created with Maven, so you just need to import it to your IDE and build or rebuild the project to resolve the dependencies.
Execute the application from \src\main\java\com.jptest.TestUserWebAppApplication

## Database configuration
Create In Memory H2 database with the name `mydb`.

## Execute Test Suite
Execute the application from \src\test\java\com.jptest.AllTestSuite.
The test is meant for BackEnd module
- User Service
- User Repository
- Report Generator

## Usage
Run the project through the IDE and head out to [http://localhost:8080](http://localhost:8080)

or

run this command in the command line:
```
mvn spring-boot:run
```
## Functionality
- Add User
- Update User
- Delete USer
- List User
- Generate PDF Report of User