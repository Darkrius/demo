package com.example.demo.infraestructure.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;

import javax.sql.DataSource;

@Configuration
public class LegacyDataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "db-legacy.datasource")
    public DataSourceProperties legacyDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "legacyDataSource")
    public DataSource legacyDataSource(@Qualifier("legacyDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean(name = "legacyJdbcTemplate")
    public JdbcTemplate legacyJdbcTemplate(@Qualifier("legacyDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSourceProperties mainDataSourceProperties() {
//        return new DataSourceProperties();
//    }
//
//    @Bean(name = "mainDataSource")
//    public DataSource mainDataSource(@Qualifier("mainDataSourceProperties") DataSourceProperties properties) {
//        return properties.initializeDataSourceBuilder().build();
//    }
//
//    @Bean(name = "jdbcTemplate")
//    public JdbcTemplate jdbcTemplate(@Qualifier("mainDataSource") DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }

}
