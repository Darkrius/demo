package com.example.demo.application.service;

import com.example.demo.application.interfaces.asesores.EnriquecerAsesorCommand;
import com.example.demo.application.interfaces.asesores.EnriquecerAsesorUseCase;
import com.example.demo.domain.entities.Asesores;
import com.example.demo.domain.repository.command.AsesorRepositoryCommand;
import com.example.demo.domain.repository.query.UbigeoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnriquecerAsesorService implements EnriquecerAsesorUseCase {

    private final AsesorRepositoryCommand asesorRepository;
    private final UbigeoRepository ubigeoRepository;

    public EnriquecerAsesorService(AsesorRepositoryCommand asesorRepository, UbigeoRepository ubigeoRepository) {
        this.asesorRepository = asesorRepository;
        this.ubigeoRepository = ubigeoRepository;
    }

    @Override
    public void execute(EnriquecerAsesorCommand command) {
        List<Asesores> domainList = command.asesores().stream()
                .map(a -> {
                    Long idUbigeo = ubigeoRepository.findIdUbigeoByDistrito(a.ciudad());
                    return new Asesores(
                            a.idAsesor(),
                            a.nombres(),
                            a.apellidos(),
                            a.correoCorporativo(),
                            "HIPOTECARIO",
                            true,
                            LocalDateTime.now(),
                            idUbigeo
                    );
                })
                .collect(Collectors.toList());

        asesorRepository.saveAll(domainList);
    }
}
