package com.example.demo.application.interfaces.usecases;

import com.example.demo.application.dto.SelectorDto;

import java.util.List;

public interface SelectorService {

    List<SelectorDto> obtenerInmobiliaria (String idAdminCreador);

    List<SelectorDto> obtenerProyecto (Long idInmobiliaria);

}
