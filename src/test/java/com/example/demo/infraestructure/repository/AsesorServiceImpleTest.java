package com.example.demo.infraestructure.repository;


import com.example.demo.domain.dto.AsesorDashBoard;
import com.example.demo.domain.model.Asesores;
import com.example.demo.infraestructure.config.LegacyDataSourceConfig;
import com.example.demo.infraestructure.config.PrincipalDataSourceConfig;
import com.example.demo.infraestructure.persistence.repositorys.AsesorServiceImple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Tag("integration")
@Import({PrincipalDataSourceConfig.class, LegacyDataSourceConfig.class})
@Transactional
class AsesorServiceImpleTest {

    @Autowired
    private AsesorServiceImple repository;

    // -------------------------------------------------------------------
    // TEST 1: BUSCAR ID UBIGEO
    // -------------------------------------------------------------------
    @Test
    @DisplayName("Debe encontrar el ID de un Ubigeo existente (ej: ICA)")
    void buscarIdUbigeo_Existente() {
        // Asumimos que en tu tabla Ubigeos existe 'ICA'
        String ciudadBuscada = "ICA";

        Optional<String> resultado = repository.buscarIdUbigeoPorCiudad(ciudadBuscada);

        assertTrue(resultado.isPresent(), "Debería encontrar el ID para ICA");
        // Verifica que el ID tenga 6 dígitos (formato estándar)
        assertEquals(6, resultado.get().length());
        System.out.println("ID encontrado para ICA: " + resultado.get());
    }

    @Test
    @DisplayName("Debe devolver vacío para una ciudad inexistente")
    void buscarIdUbigeo_NoExistente() {
        Optional<String> resultado = repository.buscarIdUbigeoPorCiudad("NARNIA");
        assertTrue(resultado.isEmpty());
    }

    // -------------------------------------------------------------------
    // TEST 2: FLUJO GUARDAR Y LISTAR (Dashboard)
    // -------------------------------------------------------------------
    @Test
    @DisplayName("Debe guardar un asesor y luego encontrarlo en el dashboard con JOIN")
    void guardarYListarAsesor() {
        // 1. GIVEN (Preparamos un Asesor de prueba)
        // Usamos un ID random para asegurar que no choque con datos reales
        String idPrueba = "TEST-" + System.currentTimeMillis();
        String nombrePrueba = "TestNombre";
        String ubigeoPrueba = "140101"; // Asegúrate de usar un ID que exista en tu tabla Ubigeos

        Asesores nuevoAsesor = Asesores.registrar(
                idPrueba,
                nombrePrueba,
                "TestApellido",
                "test@caja.pe",
                ubigeoPrueba
        );

        // 2. WHEN (Guardamos)
        // Si el SP falla (ej: unique key), esto lanzará excepción y el test fallará
        assertDoesNotThrow(() -> repository.guardar(nuevoAsesor));

        // 3. THEN (Verificamos listando)
        // Buscamos por el nombre que acabamos de insertar
        List<AsesorDashBoard> resultados = repository.buscarPorNombreParcial(nombrePrueba, 1, 10);

        // Validaciones
        assertFalse(resultados.isEmpty(), "Debería encontrar al menos un asesor");

        AsesorDashBoard encontrado = resultados.get(0);
        assertEquals(idPrueba, encontrado.idAsesor());
        assertEquals(nombrePrueba + " TestApellido", encontrado.nombreCompleto());

        // VALIDACIÓN DEL JOIN:
        // El DTO debe traer el nombre de la ciudad (ej: "ICA"), NO el código "140101".
        assertNotNull(encontrado.nombreCiudad(), "El nombre de ciudad no debe ser nulo (Fallo en JOIN)");
        assertNotEquals(ubigeoPrueba, encontrado.nombreCiudad(), "Debe traer el Nombre (Ica), no el Código");

        // VALIDACIÓN DEL CONTEO SP:
        assertTrue(encontrado.totalRegistros() > 0, "El campo totalRegistros debe venir lleno del SP");

        System.out.println("Test Exitoso: Guardado y Recuperado -> " + encontrado);
    }
}
