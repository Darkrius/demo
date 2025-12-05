package com.example.demo.infraestructure.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "Promotores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PromotorEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private LocalDateTime fechaEliminacion;

}
