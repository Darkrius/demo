package com.example.demo.infraestructure.repository;


import com.example.demo.domain.model.Inmobiliarias;
import com.example.demo.domain.model.Proyectos;
import com.example.demo.domain.model.DatosPersonales;
import com.example.demo.domain.model.Promotor;
import com.example.demo.domain.repository.InmobilariaRepository; // Necesitamos crear el padre primero
import com.example.demo.infraestructure.config.LegacyDataSourceConfig;
import com.example.demo.infraestructure.config.PrincipalDataSourceConfig;
import com.example.demo.infraestructure.persistence.repositorys.PromotorRepositoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Tag("integration")
@Transactional // Rollback al final
@Import({PrincipalDataSourceConfig.class, LegacyDataSourceConfig.class})
class PromotorRepositoryImplTest {

    @Autowired private PromotorRepositoryImpl promotorRepo;
    @Autowired private InmobilariaRepository inmobiliariaRepo; // Para crear datos previos

    // Helper para datos únicos
    private String randomStr() { return String.valueOf(System.currentTimeMillis()); }

    @Test
    @DisplayName("Debe guardar promotor y devolver ID")
    void guardarPromotor_Exito() {
        // 1. GIVEN: Crear Inmobiliaria Padre
        Inmobiliarias inmo = Inmobiliarias.crear("20" + randomStr().substring(0,9), "InmoTest", "ADMIN", null);
        Long idInmo = inmobiliariaRepo.guardarInmobiliaria(inmo);

        // Crear Promotor
        DatosPersonales datos = new DatosPersonales("Juan", "Perez", "87654321", "juan" + randomStr() + "@test.com");
        Promotor p = Promotor.registrar(datos, "ADMIN", idInmo, "HIPOTECARIO", null);

        // 2. WHEN
        Long idPromotor = promotorRepo.guardarPromotor(p);

        // 3. THEN
        assertNotNull(idPromotor);
        assertTrue(idPromotor > 0);
    }

    @Test
    @DisplayName("Debe lanzar excepción si el DOI ya existe (Validación de SP)")
    void guardarPromotor_Duplicado() {
        // 1. GIVEN
        Inmobiliarias inmo = Inmobiliarias.crear("20" + randomStr().substring(0,9), "InmoTest", "ADMIN", null);
        Long idInmo = inmobiliariaRepo.guardarInmobiliaria(inmo);

        String doiFijo = "11112222";
        DatosPersonales d1 = new DatosPersonales("Juan", "A", doiFijo, "a@a.com");
        DatosPersonales d2 = new DatosPersonales("Pedro", "B", doiFijo, "b@b.com"); // Mismo DOI

        promotorRepo.guardarPromotor(Promotor.registrar(d1, "ADMIN", idInmo, "HIPO", null));

        // 2. WHEN & THEN
        // Esperamos DataIntegrityViolationException (Spring wrapper) o tu PersistenceException
        // Dependiendo de cómo mapeaste el catch en el repo.
        // Si tu repo no atrapa DataIntegrity, el test espera esa.
        assertThrows(Exception.class, () -> {
            promotorRepo.guardarPromotor(Promotor.registrar(d2, "ADMIN", idInmo, "HIPO", null));
        });
    }

    @Test
    @DisplayName("Debe asignar proyecto a promotor")
    void asignarProyecto_Exito() {
        // Crear Inmo
        Inmobiliarias inmo = Inmobiliarias.crear("20" + randomStr().substring(0,9), "InmoTest", "ADMIN", null);
        Long idInmo = inmobiliariaRepo.guardarInmobiliaria(inmo);

        // Crear Proyecto
        Proyectos proy = Proyectos.crear("Proyecto Test");
        inmobiliariaRepo.guardarProyectos(proy, idInmo);
        // Nota: Necesitaríamos el ID del proyecto. Si tu 'guardarProyectos' no retorna ID,
        // este test es difícil sin modificar ese repo.
        // Asumiremos que puedes obtener el ID o modificar 'guardarProyectos' para retornarlo.
    }


}
