package com.example.demo.infraestructure.external;

import com.example.demo.application.dto.DatosEmpresaDto;
import com.example.demo.application.exceptions.ErrorDeConexionExternaException;
import com.example.demo.application.interfaces.external.SunatPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;



import java.util.Optional;
@Slf4j
@Component
public class SunatApi implements SunatPort {

    private final RestTemplate restTemplate;

    private static final String SUNAT_API_URL = "https://springlabs.dev/sunat/consultadatos/";

    public SunatApi(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Optional<DatosEmpresaDto> consultarRuc(String ruc) {
        String urlCompleta = SUNAT_API_URL + ruc;

        try {
            // 1. Llamar a API Externa
            SunatApiResponse externa = restTemplate.getForObject(urlCompleta, SunatApiResponse.class);

            // 2. Mapear a DTO de Negocio
            if (externa != null && externa.fullname() != null) {

                // Aquí creamos el objeto que el Frontend espera
                DatosEmpresaDto datos = new DatosEmpresaDto(
                        ruc,     // O usamos el parámetro 'ruc' si la API no lo devuelve
                        externa.fullname()
                );

                return Optional.of(datos);
            }
            return Optional.empty();

        } catch (HttpClientErrorException e) { // Captura general de errores 4xx
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.warn("INFRA: RUC [{}] no encontrado.", ruc);
                return Optional.empty(); // <--- Aquí debe entrar
            }
            throw new ErrorDeConexionExternaException("Error cliente SUNAT", e);
        }catch (RestClientException e) {
            // <--- ¡ESTE ES EL QUE TE FALTA!
            // Captura errores de conexión (Timeout, DNS, 500)
            log.error("INFRA ERROR: Falló conexión con API Sunat.", e);
            throw new ErrorDeConexionExternaException("Error de conexión con el servicio de SUNAT.", e);

        } catch (Exception e) {
            // Catch final de seguridad
            throw new ErrorDeConexionExternaException("Error inesperado al consultar SUNAT.", e);
        }

    }
}

