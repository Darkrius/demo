package com.example.demo.application.service;


import com.example.demo.application.dto.PaginacionResponseDto;
import com.example.demo.application.service.asesor.AsesorListService;
import com.example.demo.domain.entities.AsesorExterno;
import com.example.demo.domain.repository.query.AsesorRepositoryQuery;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
class AsesorListServiceTest {

    @Test
    void debeListarAsesoresCorrectamente() {
        // Mocks
        AsesorRepositoryQuery repo = Mockito.mock(AsesorRepositoryQuery.class);

        AsesorListService service = new AsesorListService(repo);

        // Datos simulados
        String nombre = "Juan";
        String ciudad = "Lima";
        int page = 0;
        int size = 2;

        List<AsesorExterno> lista = List.of(
                new AsesorExterno("1", "Juan", "Perez", "j@correo.com", "Lima", "12345678"),
        new AsesorExterno("1", "Juan", "Perez", "j@correo.com", "Lima", "12345678")
        );

        when(repo.contarAsesores(nombre, ciudad)).thenReturn(2L);
        when(repo.listarAsesores(nombre, ciudad, page, size)).thenReturn(lista);

        // Ejecutar
        PaginacionResponseDto<AsesorExterno> response =
                service.listarAsesoresPage(nombre, ciudad, page, size);

        // Validar usando los métodos del record (SIN GETTERS)
        assertThat(response.content()).hasSize(2);
        assertThat(response.currentPage()).isEqualTo(0);
        assertThat(response.totalPages()).isEqualTo(1);
        assertThat(response.totalItems()).isEqualTo(2);

        // Verificar llamadas al repo
        verify(repo, times(1)).contarAsesores(nombre, ciudad);
        verify(repo, times(1)).listarAsesores(nombre, ciudad, page, size);
    }

    @Test
    void debeRetornarVacioCuandoNoHayAsesores() {

        AsesorRepositoryQuery repo = Mockito.mock(AsesorRepositoryQuery.class);
        AsesorListService service = new AsesorListService(repo);

        when(repo.contarAsesores("X", "Y")).thenReturn(0L);

        PaginacionResponseDto<AsesorExterno> response =
                service.listarAsesoresPage("X", "Y", 0, 10);

        assertThat(response.content()).isEmpty();
        assertThat(response.totalItems()).isEqualTo(0);
        assertThat(response.totalPages()).isEqualTo(0);
    }
}
