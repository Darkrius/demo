package com.example.demo.application.interfaces.usecases;

import com.example.demo.application.dto.queries.InmobiliariaDetalleDto;

public interface DetalleInmobiliariaService {

    InmobiliariaDetalleDto listar (Long idInmobiliaria);
}
