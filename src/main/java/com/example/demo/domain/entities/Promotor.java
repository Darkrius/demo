package com.example.demo.domain.entities;

import java.time.LocalDateTime;

public class Promotor {

    private Long idPromotor;
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

    public Promotor(Long idPromotor, String nombres, String apellidos, String doi, String correo, Long idInmobiliaria, String idAdminEncargado, boolean estado, LocalDateTime fechaCreacion, LocalDateTime fechaModificacion) {
        this.idPromotor = idPromotor;
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

    public Long getIdPromotor() {
        return idPromotor;
    }

    public void setIdPromotor(Long idPromotor) {
        this.idPromotor = idPromotor;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
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

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public void eliminarPromotor(){
        if (!estado){
            throw new RuntimeException("No se puede eliminar un promotor que no esta activo");
        }this.estado = false;
    }

    public void actualizarFechaModificacion(){
        this.fechaModificacion = LocalDateTime.now();
    }

    public void correoUnico(){
        if (this.idPromotor != null){
            throw new RuntimeException("El correo ya existe");
        }
    }

    public void doiUnico(){
        if (this.doi != null){
            throw new RuntimeException("El doi ya existe");
        }
    }
}
