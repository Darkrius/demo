package com.example.demo.infraestructure.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "Inmobiliarias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class InmobiliariasEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInmobiliaria;

    private String ruc;

    private String razonSocial;

    private boolean estado;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaModificacion;

    private String idAdminCreador;

    private String logoUrl;
}
