package com.example.demo.application.service.promotor;

import com.example.demo.application.dto.ProvisionPromotorRequest;
import com.example.demo.application.dto.ProvisionPromotorResponse;
import com.example.demo.application.interfaces.asesores.promotor.CrearPromotorCommand;
import com.example.demo.application.interfaces.asesores.promotor.CrearPromotorUseCase;
import com.example.demo.domain.entities.Inmobiliarias;
import com.example.demo.domain.entities.Promotor;
import com.example.demo.domain.repository.*;
import com.example.demo.infraestructure.external.AuthServicePortFeing;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CrearPromotorService implements CrearPromotorUseCase {


    private final InmobiliariaRepository inmobiliariaRepository;
    private final AuthServicePort authServicePort;
    private final PromotorCreate promotorCreate;
    private final PromotorRepository promotorRepository;
    private final AuthServicePortFeing authServicePortFeing;
    private final ProyectoPromotorRepository proyectoPromotorRepository;

    public CrearPromotorService(InmobiliariaRepository inmobiliariaRepository, AuthServicePort authServicePort, PromotorCreate promotorCreate, PromotorRepository promotorRepository, AuthServicePortFeing authServicePortFeing, ProyectoPromotorRepository proyectoPromotorRepository) {
        this.inmobiliariaRepository = inmobiliariaRepository;
        this.authServicePort = authServicePort;
        this.promotorCreate = promotorCreate;
        this.promotorRepository = promotorRepository;
        this.authServicePortFeing = authServicePortFeing;
        this.proyectoPromotorRepository = proyectoPromotorRepository;
    }


    @Override
    @Transactional
    public Promotor crear(CrearPromotorCommand command, String idAdminCreador) {

        Inmobiliarias inmobiliarias = inmobiliariaRepository.buscarPorId(command.idInmobiliaria())
                .orElseThrow(() -> new IllegalArgumentException("No existe una inmobiliaria con el id: " + command.idInmobiliaria()));


        String contrasenaTemporal = RandomStringUtils.randomAlphabetic(8);
        System.out.println("Su contrasena temporal es: " + contrasenaTemporal);
        ProvisionPromotorRequest authRequest = new ProvisionPromotorRequest(
                command.correo(), contrasenaTemporal
        );
        Long idUsuarioAuth;
        try {
            ProvisionPromotorResponse authResponse = authServicePortFeing.provisionarUsuario(authRequest);
            idUsuarioAuth = authResponse.idUsuario();
            if (idUsuarioAuth == null) {
                throw new RuntimeException("Auth-Service devolvió un ID nulo.");
            }
            System.out.println("DEBUG: Usuario provisionado en Auth-Service. ID: " + idUsuarioAuth);
        } catch (Exception e) {
            System.err.println("ERROR: Falló la llamada a authServicePort: " + e.getMessage());
            throw new RuntimeException("Fallo crítico al provisionar usuario en auth-service", e);
        }
        try {
            authServicePort.provisionarUsuario(command.correo(), contrasenaTemporal);
            System.out.println("DEBUG (Inmobiliaria): Llamada a authServicePort completada SIN EXCEPCIÓN para: " + command.correo());
        } catch (Exception e) {
            System.err.println("ERROR (Inmobiliaria): Falló la llamada a authServicePort: " + e.getMessage());
            throw new RuntimeException("Fallo crítico al provisionar usuario en auth-service", e);
        }

        Promotor promotorCreado = new Promotor();
        promotorCreado.setIdPromotor(idUsuarioAuth);
        promotorCreado.setNombres(command.nombre());
        promotorCreado.setApellidos(command.apellidos());
        promotorCreado.setDoi(command.doi());
        promotorCreado.setCorreo(command.correo());
        promotorCreado.setFechaModificacion(LocalDateTime.now());
        promotorCreado.setFechaModificacion(LocalDateTime.now());
        promotorCreado.setEstado(true);
        promotorCreado.setIdAdminEncargado(idAdminCreador);
        promotorCreado.setIdInmobiliaria(command.idInmobiliaria());
        Promotor promotorGuardado = promotorRepository.crear(promotorCreado);

        List<Long> idProyectos = command.idProyectos();
        if (idProyectos != null && !idProyectos.isEmpty()) {
            proyectoPromotorRepository.asginarPromotor(promotorGuardado.getIdPromotor(), idProyectos);
        }

        try {
            promotorCreate.notificarNuevoPromotor(command.correo(), contrasenaTemporal);
        } catch (Exception e) {
           System.err.println("ADVERTENCIA: El promotor fue creado, pero falló el envío de la notificación a la cola: " + e.getMessage());
        }

        return promotorCreado;

    }
}
