package com.example.demo.application.interfaces.usecases;

import com.example.demo.application.dto.queries.InmobiliariaDetalleDto;
import com.example.demo.domain.dto.EditarInmobiliaria;
import com.example.demo.domain.model.Inmobiliarias;

public interface EditarInmobiliariaService {

    InmobiliariaDetalleDto editar(EditarInmobiliaria command);
}
