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
import com.example.demo.domain.dto.EditarInmobiliaria;
import com.example.demo.application.exceptions.EntidadDuplicadaException;
import java.util.Collections;
import java.util.List;


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

    @Test
    @DisplayName("Debe editar Inmobiliaria: Agregar nuevos proyectos y cambiar estado")
    void guardarEdicion_AgregaryCambiarEstado() {
        // 1. GIVEN: Creamos una inmobiliaria base
        String ruc = generarRucValido();
        Inmobiliarias padre = Inmobiliarias.crear(ruc, "Inmo Editar " + ruc, "ADMIN", null);
        Long idPadre = repository.guardarInmobiliaria(padre);

        // Definimos los cambios: Activamos y agregamos 2 torres nuevas
        List<String> proyectosNuevos = List.of("Torre Alpha", "Torre Beta");
        List<Long> idsEliminar = Collections.emptyList(); // No eliminamos nada por ahora

        EditarInmobiliaria cmd = new EditarInmobiliaria(idPadre, true, proyectosNuevos, idsEliminar);

        // 2. WHEN & THEN: Ejecutamos la edición
        // Al ser un test de integración, si el SP falla lanzará excepción y el test fallará.
        assertDoesNotThrow(() -> repository.guardarEdicion(cmd));

        System.out.println("Test Exitoso: Edición realizada para ID: " + idPadre);
    }

    @Test
    @DisplayName("Debe lanzar EntidadDuplicadaException si al editar agregamos un nombre existente")
    void guardarEdicion_Fallo_Duplicado() {
        // 1. GIVEN: Inmobiliaria con un proyecto ya existente
        String ruc = generarRucValido();
        Inmobiliarias padre = Inmobiliarias.crear(ruc, "Inmo Duplicado " + ruc, "ADMIN", null);
        Long idPadre = repository.guardarInmobiliaria(padre);

        // Guardamos manualmente el proyecto "Torre X"
        repository.guardarProyectos(Proyectos.crear("Torre X"), idPadre);

        // 2. WHEN: Intentamos editar agregando "Torre X" DE NUEVO
        List<String> proyectosNuevos = List.of("Torre Y", "Torre X"); // "Torre X" ya existe
        EditarInmobiliaria cmd = new EditarInmobiliaria(idPadre, true, proyectosNuevos, Collections.emptyList());

        // 3. THEN: Esperamos que el SP detecte el duplicado y el Repo lance nuestra excepción
        assertThrows(EntidadDuplicadaException.class, () -> repository.guardarEdicion(cmd));

        System.out.println("Test Exitoso: Se detectó duplicado correctamente.");
    }



}
