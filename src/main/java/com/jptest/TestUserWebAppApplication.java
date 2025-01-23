package com.jptest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * <h1>Simple CRUD Web Application for User entity</h1>
 * by using Vaadin as FrontEnd framework and Spring Boot as Backend framework
 */
@SpringBootApplication
public class TestUserWebAppApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(TestUserWebAppApplication.class, args);
    }
}
