package com.example.demo.infraestructure.external;

import com.example.demo.application.dto.queries.DatosEmpresaDto;
import com.example.demo.application.exceptions.ErrorDeConexionExternaException;
import com.example.demo.application.interfaces.external.SunatPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;



import java.util.Optional;
@Slf4j
@Component
public class SunatApi implements SunatPort {

    private final RestTemplate restTemplate;

    private static final String   SUNAT_API_URL="https://springlabs.dev/sunat/consultadatos/";

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

        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty(); // 404 controlado
        } catch (Exception e) {
            throw new ErrorDeConexionExternaException("Error al conectar con SUNAT", e);
        }
    }
    }

