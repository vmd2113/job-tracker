package com.duongw.workforceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication

@ComponentScan(
        basePackages = {
                "com.duongw.common",
                "com.duongw.workforceservice",
                "com.duongw.workforceservice.controller",
        })
@EnableDiscoveryClient
public class WorkforceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkforceServiceApplication.class, args);
    }

}
