package com.example.demo.domain.entities;

public class AsesorExterno {

    private String idAsesorAD;
    private String nombres;
    private String apellidos;
    private String correoCorporativo;
    private String ciudad;
    private String doi;

    public AsesorExterno() {
    }

    public AsesorExterno(String idAsesorAD, String nombres, String apellidos, String correoCorporativo, String ciudad, String doi) {
        this.idAsesorAD = idAsesorAD;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correoCorporativo = correoCorporativo;
        this.ciudad = ciudad;
        this.doi = doi;
    }

    public String getIdAsesorAD() {
        return idAsesorAD;
    }

    public void setIdAsesorAD(String idAsesorAD) {
        this.idAsesorAD = idAsesorAD;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getCorreoCorporativo() {
        return correoCorporativo;
    }

    public void setCorreoCorporativo(String correoCorporativo) {
        this.correoCorporativo = correoCorporativo;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }
}
