package com.example.demo.domain.entities;

import java.time.LocalDateTime;

public class Asesores {

    private String idAsesor;

    private String nombres;

    private String apellidos;

    private String tipoReferido = "HIPOTECARIO";

    private boolean estado = true;

    private LocalDateTime fechaUltimaAsignacion = LocalDateTime.now();

    private String correoCorporativo;

    public Asesores() {
    }

    public Asesores(String idAsesor, String nombres, String apellidos, String tipoReferido, boolean estado, LocalDateTime fechaUltimaAsignacion, String correoCorporativo) {
        this.idAsesor = idAsesor;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.tipoReferido = tipoReferido;
        this.estado = estado;
        this.fechaUltimaAsignacion = fechaUltimaAsignacion;
        this.correoCorporativo = correoCorporativo;
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

    public String getcorreoCorporativo() {
        return correoCorporativo;
    }

    public void setcorreoCorporativo(String correoCorporativo) {
        this.correoCorporativo = correoCorporativo;
    }

    public void eliminarAsesor(){
        if (!estado){
            throw new RuntimeException("Este asesor ya fue eliminado");
        } this.estado = false;
    }


}
