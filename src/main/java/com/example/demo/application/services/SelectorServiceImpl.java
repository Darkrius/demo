package com.example.demo.application.services;

import com.example.demo.application.dto.SelectorDto;
import com.example.demo.application.interfaces.external.ISelectorService;
import com.example.demo.application.interfaces.usecases.SelectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SelectorServiceImpl implements SelectorService {

    private final ISelectorService selectorService;

    public SelectorServiceImpl(ISelectorService selectorService) {
        this.selectorService = selectorService;
    }

    @Override
    public List<SelectorDto> obtenerInmobiliaria(String idAdminCreador) {
        log.debug("SERVICE: Buscando inmobiliarias activas para selectores. Admin: [{}]", idAdminCreador);
        return selectorService.listarInmobiliarias(idAdminCreador);
    }

    @Override
    public List<SelectorDto> obtenerProyecto(Long idInmobiliaria) {
        log.debug("SERVICE: Buscando proyectos activos para inmobiliaria ID: [{}]", idInmobiliaria);

        if (idInmobiliaria == null) {
            return List.of();
        }
        return selectorService.listarProyectos(idInmobiliaria);
    }
}
