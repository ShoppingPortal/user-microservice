package com.cisco.microservices.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

@EnableAutoConfiguration
@EnableDiscoveryClient
@Import(UsersWebApplication.class)
public class UsersServer {

    public static void main(String[] args) {
        SpringApplication.run(UsersServer.class, args);
    }
}
