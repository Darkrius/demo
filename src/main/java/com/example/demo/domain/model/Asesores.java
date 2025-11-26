package com.example.demo.domain.model;

import java.time.LocalDateTime;

public class Asesores {

    private final String idAsesor;

    private final  String nombres;

    private final  String apellidos;

    private final  String tipoReferido;

    private    boolean estado;

    private final  LocalDateTime fechaUltimaAsignacion;

    private final  String idUbigeo;

    private final  String correoCorporativo;



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


    public String getNombres() {
        return nombres;
    }


    public String getApellidos() {
        return apellidos;
    }


    public String getTipoReferido() {
        return tipoReferido;
    }


    public boolean isEstado() {
        return estado;
    }

    public LocalDateTime getFechaUltimaAsignacion() {
        return fechaUltimaAsignacion;
    }


    public String getIdUbigeo() {
        return idUbigeo;
    }


    public String getCorreoCorporativo() {
        return correoCorporativo;
    }


    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    public static Asesores registrar(String idAsesorAD, String nombres, String apellidos, String correoCorporativo, String idUbigeo) {
        return new Asesores(idAsesorAD, nombres, apellidos, correoCorporativo, idUbigeo);
    }
}
