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

## Report
Report template is located at \src\main\resources\reports\user_report.jrxml. To open and preview the report template you can use  [Jaspersoft Studio](https://www.jaspersoft.com/products/jaspersoft-community).   

## Preview on Jaspersoft Studio
To preview it, you need to replace the text **com.jptest.enums.UserStatus** into **java.lang.String**. The reason because field status of User entity is using type **com.jptest.enums.UserStatus** and that type not recognized by jaspersoft studio, so need to replace it temporarily with known type like **java.lang.String**.
```
<field name="status" class="com.jptest.enums.UserStatus"/>
```
to
```
<field name="status" class="java.lang.String"/>
```

And after finish preview and do stuff in the jaspersoft studio, you need to replace back the type that you change before 
```
<field name="status" class="java.lang.String"/>
```
to
```
<field name="status" class="com.jptest.enums.UserStatus"/>
```

## Report PDF

![Image](https://github.com/user-attachments/assets/90541433-1f5e-4213-b546-b27c0dc74dbf)

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

