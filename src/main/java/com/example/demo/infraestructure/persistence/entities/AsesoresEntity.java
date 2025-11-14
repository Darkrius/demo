package com.example.demo.infraestructure.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Asesores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AsesoresEntity {

    @Id
    private String idAsesor;
    private String nombres;
    private String apellidos;
    private String correoCorporativo;
    private String tipoReferido;
    private boolean estado;
    private LocalDateTime fechaUltimaAsignacion;
    private Long idUbigeo;
}
