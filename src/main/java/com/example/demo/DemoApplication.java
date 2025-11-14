package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

	}

    @Bean
    public CommandLineRunner testJdbcTemplates(Map<String, JdbcTemplate> templates) {
        return args -> templates.forEach((name, template) -> {
            String db = template.queryForObject("SELECT DB_NAME()", String.class);
            System.out.println("Bean: " + name + " -> DB: " + db);
        });
    }

}
