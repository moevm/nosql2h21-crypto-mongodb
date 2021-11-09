package com.nosql.cryp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication

public class CrypApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrypApplication.class, args);
        ApiToDb apitodb = new ApiToDb();
        apitodb.list_all_assets();
    }

}
