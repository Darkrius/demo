package com.example.demo.application.interfaces.usecases;

import com.example.demo.application.dto.DatosEmpresaDto;

public interface ConsultarRucService {
    DatosEmpresaDto obtenerRazonSocial(String ruc);
}
