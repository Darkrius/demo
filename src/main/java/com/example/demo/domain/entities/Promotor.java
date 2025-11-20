package com.example.demo.domain.entities;

import java.time.LocalDateTime;

public class Promotor {

    private Long idUsuario;
    private String nombres;
    private String apellidos;
    private String doi;
    private String correo;
    private Long idInmobiliaria;
    private String idAdminEncargado;
    private boolean estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;

    public Promotor() {
    }

    public Promotor(Long idUsuario, String nombres, String apellidos, String doi, String correo, Long idInmobiliaria, String idAdminEncargado, boolean estado, LocalDateTime fechaCreacion, LocalDateTime fechaModificacion) {
        this.idUsuario = idUsuario;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.doi = doi;
        this.correo = correo;
        this.idInmobiliaria = idInmobiliaria;
        this.idAdminEncargado = idAdminEncargado;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
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

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Long getIdInmobiliaria() {
        return idInmobiliaria;
    }

    public void setIdInmobiliaria(Long idInmobiliaria) {
        this.idInmobiliaria = idInmobiliaria;
    }

    public String getIdAdminEncargado() {
        return idAdminEncargado;
    }

    public void setIdAdminEncargado(String idAdminEncargado) {
        this.idAdminEncargado = idAdminEncargado;
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

    public void eliminar() {
        if (!estado) {
            throw new IllegalStateException("No se puede eliminar un promotor ya inactivo.");
        }
        this.estado = false;
        actualizarFechaModificacion();
    }

    public void actualizarFechaModificacion() {
        this.fechaModificacion = LocalDateTime.now();
    }


    private void validarEmail(String correo) {
        if (correo == null || !correo.contains("@") || !correo.contains(".")) {
            throw new IllegalArgumentException("Correo inválido");
        }
    }

}
