package com.example.demo.application.service.inmobiliaria;

import com.example.demo.application.interfaces.asesores.inmobiliaria.CrearInmobiliariaUseCase;
import com.example.demo.application.interfaces.asesores.inmobiliaria.CreateInmobiliariaCommand;
import com.example.demo.application.interfaces.asesores.proyecto.CreateProyectoCommand;
import com.example.demo.domain.entities.Inmobiliarias;
import com.example.demo.domain.entities.Proyecto;
import com.example.demo.domain.repository.InmobiliariaRepository;
import com.example.demo.domain.repository.ProyectoRepository;
import com.example.demo.domain.repository.SunatPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CrearInmobiliariaService implements CrearInmobiliariaUseCase {

    private final InmobiliariaRepository inmobiliariaRepository;
    private final ProyectoRepository proyectoRepository;
    private final SunatPort sunatPort;

    public CrearInmobiliariaService(InmobiliariaRepository inmobiliariaRepository, ProyectoRepository proyectoRepository, SunatPort sunatPort) {
        this.inmobiliariaRepository = inmobiliariaRepository;
        this.proyectoRepository = proyectoRepository;
        this.sunatPort = sunatPort;
    }


    @Transactional
    @Override
    public Inmobiliarias crearInmobiliaria(CreateInmobiliariaCommand command, String idAdminCreador) {

        if (inmobiliariaRepository.buscarPorRUc(command.ruc()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una inmobiliaria con RUC: " + command.ruc());
        }

        LocalDateTime now = LocalDateTime.now();
        Inmobiliarias entidad = new Inmobiliarias();
        entidad.setRuc(command.ruc());
        entidad.setRazonSocial(command.razonSocial());
        entidad.setEstado(true);
        entidad.setFechaCreacion(now);
        entidad.setFechaModificacion(now);
        entidad.setIdAdminCreador(idAdminCreador);

        Inmobiliarias guardar = inmobiliariaRepository.crear(entidad);


        List<CreateProyectoCommand> proyectosCmd = command.proyectos();
        if (proyectosCmd != null && !proyectosCmd.isEmpty()) {
            List<Proyecto> proyectos = proyectosCmd.stream().map(pc -> {
                Proyecto p = new Proyecto();
                p.setNombre(pc.nombre());
                p.setEstado(true);
                p.setIdInmobiliaria(guardar.getIdInmobiliaria());
                p.setFechaCreacion(now);
                p.setFechaModificacion(now);
                return p;
            }).collect(Collectors.toList());

            proyectoRepository.crearTodos(proyectos);
        }

        return guardar;

    }
}
