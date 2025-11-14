package com.example.demo.domain.entities;

import java.time.LocalDateTime;

public class Asesores {

    private String idAsesor;

    private String nombres;

    private String apellidos;

    private String correoCorporativo;

    private String tipoReferido;

    private boolean estado;

    private LocalDateTime fechaUltimaAsignacion = LocalDateTime.now();

    private Long idUbigeo;

    public Asesores() {
    }

    public Asesores(String idAsesor, String nombres, String apellidos, String correoCorporativo, String tipoReferido, boolean estado, LocalDateTime fechaUltimaAsignacion, Long idUbigeo) {
        this.idAsesor = idAsesor;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correoCorporativo = correoCorporativo;
        this.tipoReferido = tipoReferido;
        this.estado = estado;
        this.fechaUltimaAsignacion = fechaUltimaAsignacion;
        this.idUbigeo = idUbigeo;
    }

    public Long getIdUbigeo() {
        return idUbigeo;
    }

    public void setIdUbigeo(Long idUbigeo) {
        this.idUbigeo = idUbigeo;
    }

    public String getIdAsesor() {
        return idAsesor;
    }

    public void setIdAsesor(String idAsesor) {
        this.idAsesor = idAsesor;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreoCorporativo() {
        return correoCorporativo;
    }

    public void setCorreoCorporativo(String correoCorporativo) {
        this.correoCorporativo = correoCorporativo;
    }

    public String getTipoReferido() {
        return tipoReferido;
    }

    public void setTipoReferido(String tipoReferido) {
        this.tipoReferido = tipoReferido;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaUltimaAsignacion() {
        return fechaUltimaAsignacion;
    }

    public void setFechaUltimaAsignacion(LocalDateTime fechaUltimaAsignacion) {
        this.fechaUltimaAsignacion = fechaUltimaAsignacion;
    }
}
