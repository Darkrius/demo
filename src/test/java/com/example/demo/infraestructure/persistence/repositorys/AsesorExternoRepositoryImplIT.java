package com.example.demo.infraestructure.persistence.repositorys;

import com.example.demo.domain.entities.AsesorExterno;
import com.example.demo.domain.repository.query.AsesorRepositoryQuery;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;


import java.util.List;


@SpringBootTest
@ActiveProfiles("test")
public class AsesorExternoRepositoryImplIT {

    @Autowired
    private AsesorRepositoryQuery repository;

    @Test
    void testListarAsesoresReal() {

        // El SP inicia en página 1
        int page = 1;
        int size = 10;

        List<AsesorExterno> asesores =
                repository.listarAsesores("", "Ica", page, size);

        assertThat(asesores)
                .as("La lista de asesores no debe ser nula")
                .isNotNull();

        // Si en la BD puede estar vacío, cambia a isNotNull() solamente
        assertThat(asesores)
                .as("Debe retornar registros si existen asesores en Lima")
                .isNotEmpty();
    }

    @Test
    void testContarAsesoresReal() {

        long total = repository.contarAsesores("", "Lima");

        assertThat(total)
                .as("El total debe ser >= 0, dependiendo de la base real")
                .isGreaterThanOrEqualTo(0);
    }
}
