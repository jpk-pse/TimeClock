package com.psedb.timeclock.common;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;

@Configuration
public class DbSmokeTestRunner {

    @Bean
    CommandLineRunner dbSmokeTest(DataSource dataSource) {
        return args -> {
            try (Connection c = dataSource.getConnection()){
                System.out.println("Connection to DB established successfully." + c.getMetaData().getURL());
            }
        };
    }

}
