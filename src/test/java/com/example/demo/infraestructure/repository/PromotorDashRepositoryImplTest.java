package com.example.demo.infraestructure.repository;


import com.example.demo.application.dto.PaginationResponseDTO;
import com.example.demo.application.dto.queries.PromotorDashBoardDto;
import com.example.demo.application.dto.queries.PromotorDetalleDto;
import com.example.demo.domain.model.DatosPersonales;
import com.example.demo.domain.model.Inmobiliarias;
import com.example.demo.domain.model.Promotor;
import com.example.demo.domain.model.Proyectos;
import com.example.demo.domain.repository.InmobilariaRepository; // Para crear datos previos
import com.example.demo.domain.repository.PromotorRepository;    // Para crear datos previos
import com.example.demo.infraestructure.config.LegacyDataSourceConfig;
import com.example.demo.infraestructure.config.PrincipalDataSourceConfig;
import com.example.demo.infraestructure.persistence.repositorys.PromotorDashRepositoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Tag("integration")
@Transactional // Limpia la BD al terminar el test (Rollback)
@Import({PrincipalDataSourceConfig.class, LegacyDataSourceConfig.class})
class PromotorDashRepositoryImplTest {


    @Autowired
    private PromotorDashRepositoryImpl repository; // <--- La clase que estamos testeando

    // Helpers para insertar datos de prueba
    @Autowired private InmobilariaRepository inmoRepo;
    @Autowired private PromotorRepository promotorWriteRepo;

    private String rand() { return String.valueOf(System.currentTimeMillis()); }

    @Test
    @DisplayName("Debe listar promotores filtrados por Admin y traer el nombre de la Inmobiliaria")
    void listarPromotores_Exito() {
        // 1. GIVEN (Preparamos el escenario en BD)
        String adminId = "ADMIN_TEST_DASH";
        String nombreInmo = "Inmobiliaria Test " + rand();

        // A. Insertamos Inmobiliaria (Padre)
        Inmobiliarias inmo = Inmobiliarias.crear("20" + rand().substring(0,9), nombreInmo, adminId, null);
        Long idInmo = inmoRepo.guardarInmobiliaria(inmo);

        // B. Insertamos Promotor (Hijo)
        String doi = "8" + rand().substring(0, 7);
        DatosPersonales datos = new DatosPersonales("Juan", "Perez", doi, "juan"+rand()+"@test.com");

        Promotor p = Promotor.registrar(datos, adminId, idInmo, "HIPOTECARIO", Collections.emptyList());
        promotorWriteRepo.guardarPromotor(p);

        // 2. WHEN (Ejecutamos el método a probar)
        PaginationResponseDTO<PromotorDashBoardDto> respuesta =
                repository.promotorListar(adminId, 1, 10);

        // 3. THEN (Verificamos resultados)
        assertNotNull(respuesta);
        assertFalse(respuesta.content().isEmpty(), "La lista no debe estar vacía");

        // Buscamos el elemento específico que creamos
        PromotorDashBoardDto encontrado = respuesta.content().stream()
                .filter(dto -> dto.nombreCompleto().contains("Juan Perez"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("El promotor insertado no apareció en el listado"));

        // Validaciones Críticas del Mapeo
        assertEquals(nombreInmo, encontrado.nombreInmobiliaria(), "El JOIN con inmobiliaria falló o no se mapeó bien");
        assertTrue(encontrado.estado(), "El estado debería ser true");
        assertNotNull(encontrado.fechaModificacion());

        // Validación de Paginación (TotalRegistros del SP)
        assertTrue(respuesta.totalElements() >= 1);
    }

    @Test
    @DisplayName("Debe obtener el detalle completo del promotor (Padre + Proyectos + Prospectos)")
    void obtenerDetallePorId_Completo() {
        String adminId = "ADMIN_DETALLE";

        Inmobiliarias inmo = Inmobiliarias.crear("20" + rand().substring(0,9), "Inmo Detalle SAC", adminId, null);
        Long idInmo = inmoRepo.guardarInmobiliaria(inmo);

        Proyectos proy = Proyectos.crear("Proyecto Detalle 1");
        inmoRepo.guardarProyectos(proy, idInmo);

        DatosPersonales datos = new DatosPersonales("Maria", "Detalle", "88888888", "maria@detalle.com");
        Promotor p = Promotor.registrar(datos, adminId, idInmo, "HIPO", null);
        Long idPromotor = promotorWriteRepo.guardarPromotor(p);

        Optional<PromotorDetalleDto> resultado = repository.listarPromotorPorId(idPromotor);

        assertTrue(resultado.isPresent(), "Debería encontrar al promotor");
        PromotorDetalleDto detalle = resultado.get();

        assertEquals(idPromotor, detalle.idUsuario());
        assertEquals("Maria", detalle.nombres());
        assertEquals("Detalle", detalle.apellidos()); // Si concatenas
        assertEquals("Inmo Detalle SAC", detalle.razonSocial());
        assertEquals(inmo.getRuc(), detalle.ruc());

        // Validar Listas (Deben estar vacías pero no nulas si no asignamos nada)
        assertNotNull(detalle.proyectosAsignados());
        assertNotNull(detalle.prospectosCaptados());

    }

}
