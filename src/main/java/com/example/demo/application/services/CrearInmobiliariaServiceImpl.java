package com.example.demo.application.services;

import com.example.demo.application.dto.commands.RegistrarInmobiliariaCommand;
import com.example.demo.application.dto.queries.InmobiliariaDashBoardDto;
import com.example.demo.application.exceptions.EntidadDuplicadaException;
import com.example.demo.application.exceptions.ReglasNegocioException;
import com.example.demo.application.exceptions.StorageException;
import com.example.demo.application.interfaces.external.IStorageService;
import com.example.demo.application.interfaces.usecases.CrearInmobiliariaService;
import com.example.demo.domain.model.Inmobiliarias;
import com.example.demo.domain.model.Proyectos;
import com.example.demo.domain.repository.InmobilariaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
@Slf4j
public class CrearInmobiliariaServiceImpl implements CrearInmobiliariaService {


    private final InmobilariaRepository inmobiliariaRepository;
    private final IStorageService storageService;

    public CrearInmobiliariaServiceImpl(InmobilariaRepository inmobiliariaRepository, IStorageService storageService) {
        this.inmobiliariaRepository = inmobiliariaRepository;
        this.storageService = storageService;
    }


    @Override
    public InmobiliariaDashBoardDto crearInmobiliaria(RegistrarInmobiliariaCommand command, String idAdminCreador) {



        try {
            String urlLogo = null;

            if (command.logo() != null && command.logo().contentStream() != null) {
                try {
                    urlLogo = storageService.subirArchivo(command.logo(), "logos-inmobiliarias");
                    log.debug("SERVICE: Logo subido. URL: [{}]", urlLogo);
                } catch (Exception ex) {
                    throw new StorageException("Error al subir el logo. Intente nuevamente.", ex);
                }
            } else {
                log.debug("SERVICE: No se adjuntó logo. Se guardará sin imagen.");
            }

            //creamos nuestro inmobiliario
            Inmobiliarias nuevaInmobiliaria = Inmobiliarias.crear(
                    command.ruc(),
                    command.razonSocial(),
                    idAdminCreador,
                    urlLogo
            );

            //Capturamos el ID de la inmobiliaria generada
            long idGenerado = inmobiliariaRepository.guardarInmobiliaria(nuevaInmobiliaria);

            nuevaInmobiliaria.asignarId(idGenerado);

            log.info("SERVICE: Inmobiliaria persistida con ID: [{}]", idGenerado);


            //recorremos la creacion de proyectos
            if (command.nombreProyectos() != null && !command.nombreProyectos().isEmpty()) {
                log.debug("SERVICE: Procesando [{}] proyectos asociados...", command.nombreProyectos().size());

                for (String nombreProyecto : command.nombreProyectos()) {
                    Proyectos proyecto = Proyectos.crear(nombreProyecto);
                    inmobiliariaRepository.guardarProyectos(proyecto, idGenerado);
                }
                log.debug("SERVICE: Proyectos guardados correctamente.");
            }

            return new InmobiliariaDashBoardDto(
                    nuevaInmobiliaria.getIdInmobiliaria(),
                    nuevaInmobiliaria.getRuc(),
                    nuevaInmobiliaria.getRazonSocial(),
                    (command.nombreProyectos() != null) ? command.nombreProyectos().size() : 0,
                    nuevaInmobiliaria.isEstado(),
                    nuevaInmobiliaria.getFechaModificacion()
            );

        } catch (IllegalArgumentException e) {
            log.warn("SERVICE WARN: Validación: {}", e.getMessage());
            throw new ReglasNegocioException(e.getMessage());

        } catch (EntidadDuplicadaException | StorageException | ReglasNegocioException e) {
            log.warn("SERVICE WARN: Error controlado: {}", e.getMessage());
            throw e;

        }

    }
}
