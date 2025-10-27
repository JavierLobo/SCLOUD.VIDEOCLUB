package com.javierlobo.videoclub.rentalservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class RentalServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RentalServiceApplication.class, args);
    }
}
