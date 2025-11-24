package com.example.demo.application.services;

import com.example.demo.application.dto.commands.RegistrarAsesorCommand;
import com.example.demo.application.dto.queries.AsesorDashBoardDto;
import com.example.demo.application.exceptions.RecursoNoEncontradoException;
import com.example.demo.application.interfaces.usecases.RegistrarAsesorService;
import com.example.demo.domain.model.Asesores;
import com.example.demo.domain.repository.AsesorRepository;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class RegistrarAsesorServiceImpl implements RegistrarAsesorService{

    private final AsesorRepository asesorRepository;

    public RegistrarAsesorServiceImpl(AsesorRepository asesorRepository) {
        this.asesorRepository = asesorRepository;
    }

    @Transactional
    @Override
    public AsesorDashBoardDto registrarAsesor(RegistrarAsesorCommand command) {

        log.info("SERVICE: Iniciando registro de Asesor. ID: [{}], Nombre: [{} {}], Ciudad Solicitada: [{}]",
                command.idAsesorAD(), command.nombres(), command.apellidos(), command.ciudad());

        String idUbigeo =  asesorRepository.buscarIdUbigeoPorCiudad(command.ciudad())

                .orElseThrow(() -> new RecursoNoEncontradoException("La ciudad indicada no existe en el catálogo: " + command.ciudad()));

        log.debug("SERVICE: Ciudad [{}] resuelta correctamente a ID Ubigeo [{}]", command.ciudad(), idUbigeo);

        //creamos nuestro asesor rico
        Asesores asesores = Asesores.registrar(
                command.idAsesorAD(),
                command.nombres(),
                command.apellidos(),
                command.correoCorporativo(),
                idUbigeo
        );

        log.debug("SERVICE: Entidad creada válida. Enviando a repositorio para persistencia...");
        asesorRepository.guardar(asesores);

        log.info("SERVICE: Asesor registrado exitosamente en DB Gestión. ID: [{}]", asesores.getIdAsesor());
        return new AsesorDashBoardDto(
                asesores.getIdAsesor(),
                asesores.getNombreCompleto(),
                command.ciudad(),
                asesores.getTipoReferido()
        );
    }
}
