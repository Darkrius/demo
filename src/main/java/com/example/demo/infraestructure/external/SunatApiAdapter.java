package com.example.demo.infraestructure.external;

import com.example.demo.domain.repository.SunatPort;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;


@Component
public class SunatApiAdapter implements SunatPort {

    private final RestTemplate restTemplate;
    private final String   SUNAT_API_URL="https://springlabs.dev/sunat/consultadatos/";

    public SunatApiAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Optional<String> obtenerRazonSocialPorRuc(String ruc) {
        String urlCompleta = SUNAT_API_URL + ruc;

        try{
            SunatApiResponse respuesta = restTemplate.getForObject(urlCompleta,SunatApiResponse.class);
            if (respuesta!=null && respuesta.fullname() !=null){
                return Optional.of(respuesta.fullname());
            }
            return Optional.empty();
        } catch (HttpClientErrorException.NotFound e){
            return Optional.empty();
        }catch (Exception e){
            System.out.println("Error al obtener razon social de sunat: " + e.getMessage());
            return Optional.empty();
        }
    }
}
