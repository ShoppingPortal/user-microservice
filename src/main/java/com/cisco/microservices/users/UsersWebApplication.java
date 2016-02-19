package com.cisco.microservices.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * UsersWebApplication is the entry point of your whole application, which starts tomcat servers  
 * 
 * @author Sandip Bastapure
 */
@SpringBootApplication
public class UsersWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsersWebApplication.class, args);
    }
}
