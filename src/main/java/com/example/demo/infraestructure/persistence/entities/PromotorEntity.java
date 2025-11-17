package com.example.demo.infraestructure.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "Promotores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PromotorEntity {


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
}
