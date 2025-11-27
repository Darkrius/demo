package com.example.demo.domain.repository;

import com.example.demo.domain.model.Inmobiliarias;
import com.example.demo.domain.model.Proyectos;


public interface InmobilariaRepository {


    Long guardarInmobiliaria(Inmobiliarias inmobiliaria);

    void guardarProyectos(Proyectos proyectos, Long idInmobiliaria);

}
