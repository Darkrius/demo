package com.example.demo.domain.model;

import java.time.LocalDateTime;

public class Inmobiliarias {

    private Long idInmobiliaria;

    private String ruc;

    private String razonSocial;

    private boolean estado;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaModificacion;

    private String idAdminCreador;

    private String logoUrl;

    public Inmobiliarias() {
    }

    public Inmobiliarias( String ruc, String razonSocial,  String idAdminCreador, String logoUrl) {

        validarRuc(ruc);
        validarDatosObligatorios(razonSocial, idAdminCreador);

        this.ruc = ruc;
        this.razonSocial = razonSocial;
        this.idAdminCreador = idAdminCreador;
        this.logoUrl = logoUrl;
        this.estado = true;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaModificacion = LocalDateTime.now();

    }



    public static Inmobiliarias crear(String ruc, String razonSocial, String idAdmin, String logoUrl) {
        return new Inmobiliarias(ruc, razonSocial, idAdmin, logoUrl);
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

    public String getRazonSocial() {
        return razonSocial;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public boolean isEstado() {
        return estado;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public String getIdAdminCreador() {
        return idAdminCreador;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    private void validarRuc(String ruc) {
        if (ruc == null) {
            throw new IllegalArgumentException("El RUC no puede ser nulo.");
        }
        if (ruc.length() != 11 || !ruc.matches("\\d+")) {
            throw new IllegalArgumentException("El RUC debe tener exactamente 11 dígitos numéricos.");
        }
    }

    private void validarDatosObligatorios(String razonSocial, String idAdmin) {
        if (razonSocial == null || razonSocial.trim().isEmpty()) {
            throw new IllegalArgumentException("La Razón Social es obligatoria.");
        }
        if (idAdmin == null || idAdmin.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del administrador es obligatorio.");
        }
    }

    public void asignarId(Long id) {
        this.idInmobiliaria = id;
    }



}
