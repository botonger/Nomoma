package com.nomoma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class NomomaApplication {

    public static void main(String[] args) {
        SpringApplication.run(NomomaApplication.class, args);
    }

}
