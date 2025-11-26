package com.example.demo.application.interfaces.external;

import com.example.demo.application.dto.queries.DatosEmpresaDto;

import java.util.Optional;

public interface SunatPort {

    Optional<DatosEmpresaDto> consultarRuc(String ruc);
}
