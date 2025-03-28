package com.github.fluxith.taskstatusquery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication
@SpringBootApplication(scanBasePackages = {"com.github.fluxith.taskstatusquery", "com.alibaba.cola"})
public class TaskStatusQueryApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskStatusQueryApplication.class, args);
    }

}
