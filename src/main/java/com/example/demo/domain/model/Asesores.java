package com.example.demo.domain.model;

import java.time.LocalDateTime;

public class Asesores {

    private String idAsesor;

    private String nombres;

    private String apellidos;

    private String tipoReferido;

    private  boolean estado;

    private LocalDateTime fechaUltimaAsignacion;

    private String idUbigeo;

    private String correoCorporativo;



    public Asesores(String idAsesor, String nombres, String apellidos, String correoCorporativo, String idUbigeo) {
        if (idAsesor == null || idAsesor.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del Asesor es obligatorio.");
        }
        if (nombres == null || apellidos == null) {
            throw new IllegalArgumentException("El nombre y apellido son obligatorios.");
        }
        if (idUbigeo == null || idUbigeo.length() != 6) {
            throw new IllegalArgumentException("El ID de Ubigeo debe ser válido (6 caracteres).");
        }
        this.idAsesor = idAsesor;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correoCorporativo = correoCorporativo;
        this.idUbigeo = idUbigeo;
        this.tipoReferido = "Hipotecario";
        this.estado = true;
        this.fechaUltimaAsignacion = LocalDateTime.now();
    }

    public void darDeBaja() {
        this.estado = false;
    }

    public void reactivar() {
        this.estado = true;
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

    public String getIdUbigeo() {
        return idUbigeo;
    }

    public void setIdUbigeo(String idUbigeo) {
        this.idUbigeo = idUbigeo;
    }

    public String getCorreoCorporativo() {
        return correoCorporativo;
    }

    public void setCorreoCorporativo(String correoCorporativo) {
        this.correoCorporativo = correoCorporativo;
    }

    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    public static Asesores registrar(String idAsesorAD, String nombres, String apellidos, String correoCorporativo, String idUbigeo) {
        return new Asesores(idAsesorAD, nombres, apellidos, correoCorporativo, idUbigeo);
    }
}
