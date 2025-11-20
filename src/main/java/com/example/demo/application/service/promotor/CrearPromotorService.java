package com.example.demo.application.service.promotor;

import com.example.demo.application.dto.ProvisionPromotorRequest;
import com.example.demo.application.dto.ProvisionPromotorResponse;
import com.example.demo.application.interfaces.asesores.promotor.CrearPromotorCommand;
import com.example.demo.application.interfaces.asesores.promotor.CrearPromotorUseCase;
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

        if (!inmobiliariaRepository.existePorId(command.idInmobiliaria())) {
            throw new RuntimeException("La inmobiliaria con ID " + command.idInmobiliaria() + " no existe.");
        }

        String contrasenaTemporal = RandomStringUtils.randomAlphabetic(8);
        System.out.println("Su contrasena temporal es: " + contrasenaTemporal);

        // Llamar al microservicio auth PRIMERO (antes de cualquier inserción local)
        ProvisionPromotorRequest authRequest = new ProvisionPromotorRequest(command.correo(), contrasenaTemporal);
        ProvisionPromotorResponse authResponse = authServicePortFeing.provisionarUsuario(authRequest);
        Long idUsuarioAuth = authResponse.idUsuario();
        System.out.println("DEBUG: Usuario provisionado en Auth-Service. ID: " + idUsuarioAuth);

        Promotor promotorCreado = new Promotor();
        promotorCreado.setNombres(command.nombres());
        promotorCreado.setApellidos(command.apellidos());
        promotorCreado.setDoi(command.doi());
        promotorCreado.setCorreo(command.correo());
        promotorCreado.setFechaCreacion(LocalDateTime.now());
        promotorCreado.setFechaModificacion(LocalDateTime.now());
        promotorCreado.setEstado(true);
        promotorCreado.setIdAdminEncargado(idAdminCreador);
        promotorCreado.setIdInmobiliaria(command.idInmobiliaria());
        promotorCreado.setIdUsuario(idUsuarioAuth); // Asignar ID de auth aquí

        // Guardar en base de datos local
        Promotor promotorGuardado = promotorRepository.crear(promotorCreado);

        // Asignar proyectos
        List<Long> idProyectos = command.idProyectos();
        if (idProyectos != null && !idProyectos.isEmpty()) {
            proyectoPromotorRepository.asginarPromotor(promotorGuardado.getIdUsuario(), idProyectos);
        }

        // Notificación (con try-catch para no revertir si falla)
        try {
            promotorCreate.notificarNuevoPromotor(command.correo(), contrasenaTemporal);
        } catch (Exception e) {
            System.err.println("ADVERTENCIA: El promotor fue creado, pero falló el envío de la notificación a la cola: " + e.getMessage());
        }

        return promotorGuardado;
    }
}
