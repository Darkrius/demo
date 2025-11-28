package com.example.demo.infraestructure.repository;

import com.example.demo.application.dto.SelectorDto;
import com.example.demo.application.interfaces.external.ISelectorService;
import com.example.demo.domain.model.Inmobiliarias;
import com.example.demo.domain.model.Proyectos;
import com.example.demo.domain.repository.InmobilariaRepository; // El de Escritura
import com.example.demo.infraestructure.config.LegacyDataSourceConfig;
import com.example.demo.infraestructure.config.PrincipalDataSourceConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.security.SecureRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Tag("integration")
@Transactional // Limpia la BD al terminar
@Import({PrincipalDataSourceConfig.class, LegacyDataSourceConfig.class})
class SelectorRepositoryImplTest {


    @Autowired
    private ISelectorService selectorRepository; // El que probamos (Lectura)



    @Autowired
    private InmobilariaRepository writeRepository; // Helper para crear datos (Escritura)

    private final SecureRandom secureRandom = new SecureRandom();

    private String generarRucValido() {
        int aleatorio = 100_000_000 + secureRandom.nextInt(900_000_000);

        return "20" + aleatorio;
    }

    @Test
    @DisplayName("Debe listar inmobiliarias activas en el selector")
    void listarInmobiliarias_Exito() {
        // 1. GIVEN: Crear datos reales
        String ruc = generarRucValido();
        String nombreEmpresa = "Selector Test SAC";

        Inmobiliarias inmo = Inmobiliarias.crear(ruc, nombreEmpresa, "ADMIN_TEST", null);
        writeRepository.guardarInmobiliaria(inmo);

        // 2. WHEN
        List<SelectorDto> resultados = selectorRepository.listarInmobiliarias("ADMIN_TEST");

        // 3. THEN
        assertFalse(resultados.isEmpty());

        // Buscamos la nuestra en la lista
        boolean existe = resultados.stream()
                .anyMatch(dto -> dto.descripcion().equals(nombreEmpresa));

        assertTrue(existe, "La inmobiliaria creada debería aparecer en el selector");
    }

    @Test
    @DisplayName("Debe listar proyectos de una inmobiliaria específica")
    void listarProyectos_Exito() {
        // 1. GIVEN
        String ruc = generarRucValido();
        Inmobiliarias padre = Inmobiliarias.crear(ruc, "Padre Proyectos SAC", "ADMIN", null);
        long idPadre = writeRepository.guardarInmobiliaria(padre);

        // Guardamos 2 proyectos para este padre
        writeRepository.guardarProyectos(Proyectos.crear("Proyecto A"), idPadre);
        writeRepository.guardarProyectos(Proyectos.crear("Proyecto B"), idPadre);

        // 2. WHEN
        List<SelectorDto> proyectos = selectorRepository.listarProyectos(idPadre);

        // 3. THEN
        assertEquals(2, proyectos.size());
        assertTrue(proyectos.stream().anyMatch(p -> p.descripcion().equals("Proyecto A")));
        assertTrue(proyectos.stream().anyMatch(p -> p.descripcion().equals("Proyecto B")));
    }
}
