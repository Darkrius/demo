package com.example.demo.infraestructure.external;

import com.example.demo.application.dto.DatosEmpresaDto;
import com.example.demo.application.exceptions.ErrorDeConexionExternaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SunarApiTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SunatApi adapter; // Tu clase implementada

    @Test
    @DisplayName("Debe retornar Optional con nombre si la API responde OK")
    void consultarApi_Exito() {
        // Given
        com.example.demo.infraestructure.external.SunatApiResponse respuestaMock = new SunatApiResponse("MI EMPRESA S.A.C.");

        when(restTemplate.getForObject(anyString(), eq(SunatApiResponse.class)))
                .thenReturn(respuestaMock);

        // When
        Optional<DatosEmpresaDto> resultado = adapter.consultarRuc("20608300393");

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("MI EMPRESA S.A.C.", resultado.get().razonSocial());
    }

    @Test
    @DisplayName("Debe retornar Empty si la API responde 404 Not Found")
    void consultarApi_NotFound() {
        // Given
        // Simulamos que RestTemplate lanza la excepción 404
        when(restTemplate.getForObject(anyString(), eq(SunatApiResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // When
        Optional<DatosEmpresaDto> resultado = adapter.consultarRuc("20608300393");

        // Then
        assertTrue(resultado.isEmpty(), "Debería retornar vacío, no lanzar error");
    }

    @Test
    @DisplayName("Debe lanzar ErrorDeConexionExternaException si la API falla (500 o Timeout)")
    void consultarApi_ErrorServidor() {
        // Given
        // Simulamos error genérico de conexión
        when(restTemplate.getForObject(anyString(), eq(SunatApiResponse.class)))
                .thenThrow(new RestClientException("Timeout de conexión"));

        // When & Then
        assertThrows(ErrorDeConexionExternaException.class, () -> {
            adapter.consultarRuc("20608300393");
        });
    }
}
