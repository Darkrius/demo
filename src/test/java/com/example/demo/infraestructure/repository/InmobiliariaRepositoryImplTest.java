package com.example.demo.infraestructure.repository;


import com.example.demo.application.exceptions.PersistenceException;
import com.example.demo.domain.model.Inmobiliarias;
import com.example.demo.domain.model.Proyectos;
import com.example.demo.infraestructure.config.LegacyDataSourceConfig;
import com.example.demo.infraestructure.config.PrincipalDataSourceConfig;
import com.example.demo.infraestructure.persistence.repositorys.InmobiliariaRepositoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@Import({PrincipalDataSourceConfig.class, LegacyDataSourceConfig.class}) // Cargamos tus configs
@SpringBootTest
@Tag("integration")
@Transactional
class InmobiliariaRepositoryImplTest {

    @Autowired
    private InmobiliariaRepositoryImpl repository;

    private String generarRucAleatorio() {
        long numero = (long) (Math.random() * 9000000000L) + 1000000000L; // Genera 10 dígitos
        return "2" + numero; // RUC empieza con 2 y tiene 11 dígitos
    }


    @Test
    @DisplayName("Debe guardar Inmobiliaria y retornar un ID válido")
    void guardarInmobiliaria_Exito() {
        // 1. GIVEN

        String rucUnico = generarRucAleatorio();

        Inmobiliarias inmobiliaria = Inmobiliarias.crear(
                rucUnico,
                "InmoTest" + rucUnico,
                "ADMIN-TEST",
                "/uploads/logo.png"
        );

        // 2. WHEN
        Long idGenerado = repository.guardarInmobiliaria(inmobiliaria);

        // 3. THEN
        assertNotNull(idGenerado);
        assertTrue(idGenerado > 0, "El ID generado debe ser mayor a 0");

        System.out.println("Test Exitoso: Inmobiliaria creada con ID: " + idGenerado);
    }

    @Test
    @DisplayName("Debe guardar Proyecto asociado a una Inmobiliaria existente")
    void guardarProyecto_Exito() {
        String rucUnico = generarRucAleatorio();
        // 1. GIVEN: Primero necesitamos un padre real en la BD (gracias a @Transactional se borrará luego)
        Inmobiliarias padre = Inmobiliarias.crear(rucUnico, "Padre SAC", "ADMIN", null);
        Long idPadre = repository.guardarInmobiliaria(padre);

        Proyectos hijo = Proyectos.crear("Residencial Los Sauces");

        // 2. WHEN & THEN
        assertDoesNotThrow(() -> repository.guardarProyectos(hijo, idPadre));

        System.out.println("Test Exitoso: Proyecto guardado para el padre ID: " + idPadre);
    }

    @Test
    @DisplayName("Debe lanzar PersistenceException si intentamos guardar con RUC duplicado")
    void guardarInmobiliaria_Duplicado() {
        // 1. GIVEN
        String rucDuplicado = generarRucAleatorio();

        Inmobiliarias inmo1 = Inmobiliarias.crear(rucDuplicado, "Original", "ADMIN", null);
        Inmobiliarias inmo2 = Inmobiliarias.crear(rucDuplicado, "copia", "ADMIN", null);

        // Guardamos el primero
        repository.guardarInmobiliaria(inmo1);

        // 2. WHEN & THEN
        // El segundo debe fallar por el Constraint UNIQUE o por la validación del SP
        assertThrows(PersistenceException.class, () -> repository.guardarInmobiliaria(inmo2));
    }




}
