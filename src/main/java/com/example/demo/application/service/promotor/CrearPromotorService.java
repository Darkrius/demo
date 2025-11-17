package com.example.demo.application.service.promotor;

import com.example.demo.application.interfaces.asesores.promotor.CrearPromotorCommand;
import com.example.demo.application.interfaces.asesores.promotor.CrearPromotorUseCase;
import com.example.demo.domain.entities.Inmobiliarias;
import com.example.demo.domain.entities.Promotor;
import com.example.demo.domain.repository.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CrearPromotorService implements CrearPromotorUseCase {


    private final InmobiliariaRepository inmobiliariaRepository;
    private final AuthServicePort authServicePort;
    private final PromotorCreate promotorCreate;

    public CrearPromotorService(InmobiliariaRepository inmobiliariaRepository, AuthServicePort authServicePort, PromotorCreate promotorCreate) {
        this.inmobiliariaRepository = inmobiliariaRepository;
        this.authServicePort = authServicePort;
        this.promotorCreate = promotorCreate;
    }


    @Override
    public Promotor crear(CrearPromotorCommand command, String idAdminCreador) {

        //hemos creado un servicio a medias, aun no sabemos de donde vendran algunos datos datos
        //ademas de como vamos a tratar a los proyectos porque aun no los hemos asignado

        Inmobiliarias inmobiliarias = inmobiliariaRepository.buscarPorId(command.idInmobiliaria())
                .orElseThrow(() -> new IllegalArgumentException("No existe una inmobiliaria con el id: " + command.idInmobiliaria()));


        String contrasenaTemporal = RandomStringUtils.randomAlphabetic(8);
        System.out.println("Su contrasena temporal es: " + contrasenaTemporal);
        try {
            authServicePort.provisionarUsuario(command.correo(), contrasenaTemporal);
            System.out.println("DEBUG (Inmobiliaria): Llamada a authServicePort completada SIN EXCEPCIÓN para: " + command.correo());
        } catch (Exception e) {
            System.err.println("ERROR (Inmobiliaria): Falló la llamada a authServicePort: " + e.getMessage());
            throw new RuntimeException("Fallo crítico al provisionar usuario en auth-service", e);
        }

        Promotor promotorCreado = new Promotor();
        promotorCreado.setNombres(command.nombre());
        promotorCreado.setApellidos(command.apellidos());
        promotorCreado.setDoi(command.doi());
        promotorCreado.setCorreo(command.correo());
        promotorCreado.setFechaModificacion(LocalDateTime.now());
        promotorCreado.setFechaModificacion(LocalDateTime.now());
        promotorCreado.setEstado(true);
        promotorCreado.setIdAdminEncargado(idAdminCreador);
        promotorCreado.setIdInmobiliaria(command.idInmobiliaria());

        try {
            promotorCreate.notificarNuevoPromotor(command.correo(), contrasenaTemporal);
        } catch (Exception e) {
           System.err.println("ADVERTENCIA: El promotor fue creado, pero falló el envío de la notificación a la cola: " + e.getMessage());
        }

        return promotorCreado;

    }
}
