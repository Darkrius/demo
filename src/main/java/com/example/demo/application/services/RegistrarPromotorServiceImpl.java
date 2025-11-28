package com.example.demo.application.services;

import com.example.demo.application.dto.PromotorCreadoEvent;
import com.example.demo.application.dto.commands.RegistrarPromotorCommand;
import com.example.demo.application.dto.queries.PromotorDashBoardDto;
import com.example.demo.application.exceptions.*;
import com.example.demo.application.interfaces.external.IEventPublisher;
import com.example.demo.application.interfaces.usecases.RegistrarPromotorService;
import com.example.demo.domain.model.DatosPersonales;
import com.example.demo.domain.model.Promotor;
import com.example.demo.domain.repository.PromotorRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RegistrarPromotorServiceImpl implements RegistrarPromotorService {


    private final PromotorRepository promotorRepository;
    private final IEventPublisher iEventPublisher;

    public RegistrarPromotorServiceImpl(PromotorRepository promotorRepository, IEventPublisher iEventPublisher) {
        this.promotorRepository = promotorRepository;
        this.iEventPublisher = iEventPublisher;
    }


    @Transactional
    @Override
    public PromotorDashBoardDto registrar(RegistrarPromotorCommand registrarPromotorCommand, String idAdminCreador) {
        log.info("SERVICE: Iniciando registro de Promotor. DOI: [{}]", registrarPromotorCommand.doi());

        try {


            String tipoAsignado = determinarTipoPorAdmin(idAdminCreador);

            var datosPersonales = new DatosPersonales(
                    registrarPromotorCommand.nombre(),
                    registrarPromotorCommand.apellido(),
                    registrarPromotorCommand.doi(),
                    registrarPromotorCommand.correo()
            );

            Promotor nuevoPromotor = Promotor.registrar(
                    datosPersonales,
                    idAdminCreador,
                    registrarPromotorCommand.idInmobiliaria(),
                    tipoAsignado,
                    registrarPromotorCommand.proyectosAsignados()
            );


            long idGenerado = promotorRepository.guardarPromotor(nuevoPromotor);

            nuevoPromotor.asignarId(idGenerado);

            log.info("SERVICE: Promotor guardado con ID: [{}]", idGenerado);

            // 6. PERSISTENCIA DE HIJOS (Relaciones)
            // Recorremos la lista de proyectos que validamos en el dominio y guardamos la relación
            if (!nuevoPromotor.getProyectosAsignados().isEmpty()) {
                for (Long idProyecto : nuevoPromotor.getProyectosAsignados()) {
                    promotorRepository.guardarProyectosPromotor(idGenerado, idProyecto);
                }
                log.debug("SERVICE: Se asignaron [{}] proyectos al promotor.", nuevoPromotor.getProyectosAsignados().size());
            }

            PromotorCreadoEvent evento = new PromotorCreadoEvent(
                    nuevoPromotor.getIdUsuario(),
                    nuevoPromotor.getNombres(),
                    nuevoPromotor.getApellidos(),
                    nuevoPromotor.getCorreo(),
                    nuevoPromotor.getTipoPromotor()
            );

            // Publicamos. Si esto falla (lanza excepción), el @Transactional
            // borrará al promotor de la BD.
            iEventPublisher.publicarEventoCreacion(evento);

            log.info("SERVICE: Evento de creación enviado a RabbitMQ exitosamente.");

            // 7. RETORNO (Mapeo a DTO)
            // Construimos la respuesta para que el Frontend actualice la tabla sin recargar
            return new PromotorDashBoardDto(
                    nuevoPromotor.getIdUsuario(),
                    nuevoPromotor.getNombreCompleto(),
                    "Cargando...", // Nota: Al guardar no tenemos el nombre de la Inmobiliaria (requiere JOIN), ponemos placeholder o null
                    nuevoPromotor.isEstado(),
                    nuevoPromotor.getFechaModificacion()
            );

        } catch (IllegalArgumentException e) {
            log.warn("SERVICE WARN: Validación: {}", e.getMessage());
            throw new ReglasNegocioException(e.getMessage());

        } catch (EntidadDuplicadaException e) {
            log.warn("SERVICE WARN: Duplicado detectado: {}", registrarPromotorCommand.doi());
            throw e;

        } catch (PersistenceException | StorageException e) {
            throw e;

        } catch (Exception e) {
            log.error("SERVICE ERROR CRÍTICO: Fallo inesperado al registrar promotor [{}].", registrarPromotorCommand.doi(), e);
            throw new ErrorInesperadoException("Error interno del servidor.", e);
        }
    }

    private String determinarTipoPorAdmin(String idAdmin) {
        return "1".equals(idAdmin) ? "HIPOTECARIO" : "GENERAL";
    }
}
