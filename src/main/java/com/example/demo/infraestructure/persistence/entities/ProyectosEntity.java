package com.example.demo.infraestructure.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Proyectos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProyectosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProyecto;

    private String nombre;

    private boolean estado;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaModificacion;

    private Long idInmobiliaria;

}
