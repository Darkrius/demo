package com.example.demo.application.services;

import com.example.demo.application.dto.queries.DatosEmpresaDto;
import com.example.demo.application.exceptions.RecursoNoEncontradoException;
import com.example.demo.application.exceptions.ReglasNegocioException;
import com.example.demo.application.interfaces.external.SunatPort;
import com.example.demo.application.interfaces.usecases.ConsultarRucService;
import org.springframework.stereotype.Service;


@Service
public class SunatPortImpl implements ConsultarRucService {

    private final SunatPort sunatPort;
    public SunatPortImpl(SunatPort sunatPort) {
        this.sunatPort = sunatPort;
    }


    @Override
    public DatosEmpresaDto obtenerRazonSocial(String ruc) {
            if (ruc == null || ruc.length() != 11) {
                throw new ReglasNegocioException("RUC inválido");
            }

            return sunatPort.consultarRuc(ruc)
                    .orElseThrow(() -> new RecursoNoEncontradoException("No se encontraron datos para el RUC: " + ruc));
        }

    }




