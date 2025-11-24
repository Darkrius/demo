package com.example.demo.infraestructure.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
//@EnableJpaRepositories(
//        basePackages = "com.example.demo.infraestructure.persistence.repositorys.principal", // repositorios de principal
//        entityManagerFactoryRef = "principalEntityManagerFactory",
//        transactionManagerRef = "principalTransactionManager"
//)
public class PrincipalDataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties principalDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "principalDataSource")
    public DataSource principalDataSource(@Qualifier("principalDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean principalEntityManagerFactory(
            @Qualifier("principalDataSource") DataSource principalDataSource) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(principalDataSource);
        em.setPackagesToScan("com.example.demo.infraestructure.persistence.entities"); // tus entities
        em.setPersistenceUnitName("principalPU");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        return em;
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager principalTransactionManager(
            @Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean(name = "principalJdbcTemplate")
    @Primary // Opcional, pero recomendado si es la DB principal
    public JdbcTemplate principalJdbcTemplate(@Qualifier("principalDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
