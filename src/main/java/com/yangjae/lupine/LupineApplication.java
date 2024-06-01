package com.yangjae.lupine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LupineApplication {

    public static void main(String[] args) {
        SpringApplication.run(LupineApplication.class, args);
    }

}
