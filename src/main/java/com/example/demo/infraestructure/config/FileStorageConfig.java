package com.example.demo.infraestructure.config;


import com.example.demo.application.interfaces.external.IStorageService;
import com.example.demo.infraestructure.external.FileSystemStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileStorageConfig {

    @Bean
    public IStorageService storageService(
            @Value("${storage.root}") String rootDirectory
    ) {
        return new FileSystemStorageService(rootDirectory);
    }
}
