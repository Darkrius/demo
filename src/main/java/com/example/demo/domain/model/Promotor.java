package com.example.demo.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Promotor {

    private Long idUsuario;

    private String nombres;

    private String apellidos;

    private String doi;

    private String correo;

    private String idAdminEncargado;

    private Long idInmobiliaria;

    private String tipoPromotor;

    private boolean estado;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaModificacion;

    private List<Long> proyectosAsignados = new ArrayList<>();

    private LocalDateTime fechaEliminacion;

    public Promotor(DatosPersonales datos,
                     String idAdminEncargado,
                     Long idInmobiliaria,
                     String tipoPromotor,
                     List<Long> proyectos) {

        validarInmobiliaria(idInmobiliaria);
        validarAdmin(idAdminEncargado);


        this.nombres = datos.nombres();
        this.apellidos = datos.apellidos();
        this.doi = datos.doi();
        this.correo = datos.correo();

        this.idAdminEncargado = idAdminEncargado;
        this.idInmobiliaria = idInmobiliaria;

        if (proyectos != null) {
            this.proyectosAsignados.addAll(proyectos);
        }

        this.tipoPromotor = (tipoPromotor != null && !tipoPromotor.isBlank()) ? tipoPromotor : "HIPOTECARIO";

        this.estado = true;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaModificacion = LocalDateTime.now();
        this.fechaEliminacion = null;
    }

    public static Promotor registrar(DatosPersonales datos,
                                     String idAdmin,
                                     Long idInmo,
                                     String tipo,
                                     List<Long> proyectos) {
        return new Promotor(datos, idAdmin, idInmo, tipo, proyectos);
    }

    private void validarInmobiliaria(Long idInmobiliaria) {
        if (idInmobiliaria == null) {
            throw new IllegalArgumentException("El promotor debe pertenecer a una inmobiliaria.");
        }
    }

    private void validarAdmin(String idAdmin) {
        if (idAdmin == null || idAdmin.isBlank()) {
            throw new IllegalArgumentException("Se requiere un admin creador.");
        }
    }

    public void agregarProyecto(Long idProyecto) {
        if (idProyecto != null && !this.proyectosAsignados.contains(idProyecto)) {
            this.proyectosAsignados.add(idProyecto);
        }
    }


    public void asignarId(Long id) {
        this.idUsuario = id;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getDoi() {
        return doi;
    }

    public Long getIdInmobiliaria() {
        return idInmobiliaria;
    }

    public String getIdAdminEncargado() {
        return idAdminEncargado;
    }

    public String getCorreo() {
        return correo;
    }

    public String getTipoPromotor() {
        return tipoPromotor;
    }

    public boolean isEstado() {
        return estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public List<Long> getProyectosAsignados() {
        return proyectosAsignados;
    }


    public LocalDateTime getFechaEliminacion() {
        return fechaEliminacion;
    }

    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    public void darDeBaja() {
        this.estado = false;
        this.fechaModificacion = LocalDateTime.now();
        this.fechaEliminacion = LocalDateTime.now();
    }

    public void iniciarVacaciones() {
        this.estado = false;
        this.fechaModificacion = LocalDateTime.now();
    }

    //probablemente nunca lo usemos pero lo quiero mantener
    public void reincorporarPromotor() {
        this.estado = true;
        this.fechaModificacion = LocalDateTime.now();
        this.fechaEliminacion = null;
    }

}
