package com.example.demo.application.interfaces.usecases;

import com.example.demo.application.dto.queries.PromotorDetalleDto;

public interface DetallePromotorService {

    PromotorDetalleDto listar (Long idUsuario);
}
