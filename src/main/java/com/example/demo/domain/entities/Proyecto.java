package com.example.demo.domain.entities;

import java.time.LocalDateTime;

public class Proyecto {


    private Long idProyecto;
    private String nombre;
    private boolean estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    private Long idInmobiliaria;

    public Proyecto() {
    }

    public Proyecto(Long idProyecto, String nombre, LocalDateTime fechaModificacion, boolean estado, LocalDateTime fechaCreacion, Long idInmobiliaria) {
        this.idProyecto = idProyecto;
        this.nombre = nombre;
        this.fechaModificacion = fechaModificacion;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.idInmobiliaria = idInmobiliaria;
    }

    public Proyecto(Long idProyecto, boolean estado, String nombre, LocalDateTime fechaCreacion, LocalDateTime fechaModificacion, Long idInmobiliaria) {
        this.idProyecto = idProyecto;
        this.estado = estado;
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.idInmobiliaria = idInmobiliaria;
    }

    public Long getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(Long idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Long getIdInmobiliaria() {
        return idInmobiliaria;
    }

    public void setIdInmobiliaria(Long idInmobiliaria) {
        this.idInmobiliaria = idInmobiliaria;
    }
}
