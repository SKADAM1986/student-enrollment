package com.blockone.enrollment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class EnrollmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnrollmentApplication.class, args);
    }

}
