package com.example.demo.application.interfaces.external;

import com.example.demo.application.dto.SelectorDto;

import java.util.List;

public interface ISelectorService {


    List<SelectorDto> listarInmobiliarias (String idAdminCreador);

    List<SelectorDto> listarProyectos (Long idInmobiliaria);
}
