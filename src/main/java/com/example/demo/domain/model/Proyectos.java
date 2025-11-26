package com.example.demo.domain.model;

import java.time.LocalDateTime;

public class Proyectos {

    private Long idProyecto;

    private String nombre;

    private boolean estado;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaModificacion;

    private Long idInmobiliaria;


    private Proyectos(String nombre) {
        validad(nombre);
        this.nombre = nombre;
        this.estado = true;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaModificacion = LocalDateTime.now();
    }

    public static Proyectos crear(String nombre) {
        return new Proyectos(nombre);
    }

    public String getNombre() { return nombre; }
    public boolean isEstado() { return estado; }

    public void validad (String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del proyecto es obligatorio.");
        }
    }

}
