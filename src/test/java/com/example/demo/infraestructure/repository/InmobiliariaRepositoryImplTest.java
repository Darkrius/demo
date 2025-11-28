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
import java.security.SecureRandom;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.*;
@Import({PrincipalDataSourceConfig.class, LegacyDataSourceConfig.class}) // Cargamos tus configs
@SpringBootTest
@Tag("integration")
@Transactional
class InmobiliariaRepositoryImplTest {

    @Autowired
    private InmobiliariaRepositoryImpl repository;

    private final SecureRandom secureRandom = new SecureRandom();

    private String generarRucValido() {
        // Genera un número entero entre 0 y 899,999,999 y le suma 100,000,000
        // Resultado: Un número de 9 dígitos (100000000 a 999999999)
        int aleatorio = 100_000_000 + secureRandom.nextInt(900_000_000);

        return "20" + aleatorio;
    }

    @Test
    @DisplayName("Debe guardar Inmobiliaria y retornar un ID válido")
    void guardarInmobiliaria_Exito() {
        // 1. GIVEN

        String rucUnico = generarRucValido();

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
        String rucUnico = generarRucValido();
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
        String rucDuplicado = generarRucValido();

        Inmobiliarias inmo1 = Inmobiliarias.crear(rucDuplicado, "Original", "ADMIN", null);
        Inmobiliarias inmo2 = Inmobiliarias.crear(rucDuplicado, "copia", "ADMIN", null);

        // Guardamos el primero
        repository.guardarInmobiliaria(inmo1);

        // 2. WHEN & THEN
        // El segundo debe fallar por el Constraint UNIQUE o por la validación del SP
        assertThrows(PersistenceException.class, () -> repository.guardarInmobiliaria(inmo2));
    }




}
