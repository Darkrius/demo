package com.example.demo.domain.entities;

import java.time.LocalDateTime;

public class Inmobiliarias {

    private Long idInmobiliaria;

    private String ruc;
    private String razonSocial;
    private boolean estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    private String idAdminCreador;


    public Inmobiliarias() {
    }

    public Inmobiliarias(Long idInmobiliaria, String ruc, String razonSocial, boolean estado, LocalDateTime fechaCreacion, LocalDateTime fechaModificacion, String idAdminCreador) {
        this.idInmobiliaria = idInmobiliaria;
        this.ruc = ruc;
        this.razonSocial = razonSocial;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.idAdminCreador = idAdminCreador;
    }

    public Long getIdInmobiliaria() {
        return idInmobiliaria;
    }

    public void setIdInmobiliaria(Long idInmobiliaria) {
        this.idInmobiliaria = idInmobiliaria;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
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

    public String getIdAdminCreador() {
        return idAdminCreador;
    }

    public void setIdAdminCreador(String idAdminCreador) {
        this.idAdminCreador = idAdminCreador;
    }

    public  void eliminar(){
        if (!estado) {
            throw  new RuntimeException("No se puede eliminar una inmobiliaria que no esta activa");
        } this.estado = false;
    }

    private void actualizarFechaModificacion() {
        this.fechaModificacion = LocalDateTime.now();
    }
}
