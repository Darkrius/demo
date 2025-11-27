package com.example.demo.application.interfaces.external;

import com.example.demo.application.dto.DatosEmpresaDto;

import java.util.Optional;

public interface SunatPort {

    Optional<DatosEmpresaDto> consultarRuc(String ruc);
}
