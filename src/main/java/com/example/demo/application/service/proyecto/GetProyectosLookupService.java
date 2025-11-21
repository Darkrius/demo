package com.example.demo.application.service.proyecto;

import com.example.demo.application.interfaces.asesores.proyecto.GetProyectosLookupUseCase;
import com.example.demo.domain.entities.Proyecto;
import com.example.demo.domain.repository.ProyectoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetProyectosLookupService implements GetProyectosLookupUseCase {

    private final ProyectoRepository proyectoRepository;

    public GetProyectosLookupService(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    @Override
    public List<Proyecto> execute(Long idInmobiliaria) {
        return proyectoRepository.getProyectosLookupByInmobiliaria(idInmobiliaria);
    }
}
