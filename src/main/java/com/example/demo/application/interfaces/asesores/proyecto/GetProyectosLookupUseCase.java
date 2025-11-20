package com.example.demo.application.interfaces.asesores.proyecto;

import com.example.demo.application.dto.InmobiliariaLookupDto.ProyectoLookDto;
import com.example.demo.domain.entities.Proyecto;

import java.util.List;

public interface GetProyectosLookupUseCase {

    List<Proyecto> execute(Long idInmobiliaria);
}
