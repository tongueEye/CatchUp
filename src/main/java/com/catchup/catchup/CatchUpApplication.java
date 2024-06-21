package com.catchup.catchup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CatchUpApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatchUpApplication.class, args);
    }

}
