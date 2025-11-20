package com.example.demo.application.service.inmobiliaria;

import com.example.demo.application.dto.InmobiliariaLookupDto.InmobiliariaLookDtp;
import com.example.demo.application.interfaces.asesores.inmobiliaria.ListarInmobiliariasClientUseCase;
import com.example.demo.domain.entities.Inmobiliarias;
import com.example.demo.domain.repository.InmobiliariaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListarInmobiliariasClientService implements ListarInmobiliariasClientUseCase {

    private final InmobiliariaRepository inmobiliariaRepository;

    public ListarInmobiliariasClientService(InmobiliariaRepository inmobiliariaRepository) {
        this.inmobiliariaRepository = inmobiliariaRepository;
    }

    @Override
    public List<Inmobiliarias> execute(String idAdminCreador) {
        return inmobiliariaRepository.listarInmobiliariasClient(idAdminCreador);
    }

}
