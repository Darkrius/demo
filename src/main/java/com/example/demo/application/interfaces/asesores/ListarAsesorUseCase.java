package com.example.demo.application.interfaces.asesores;

import com.example.demo.application.dto.query.PaginacionResponseDto;
import com.example.demo.domain.entities.AsesorExterno;


public interface ListarAsesorUseCase {

    PaginacionResponseDto<AsesorExterno> listarAsesoresPage(String nombre, String ciudad, int page, int size);
}
