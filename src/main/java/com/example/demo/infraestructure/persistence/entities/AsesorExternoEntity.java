package com.example.demo.infraestructure.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "Asesores_Externos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AsesorExternoEntity {


    @Id
    private String idAsesorAD;
    private String nombres;
    private String apellidos;
    private String correoCorporativo;
    private String doi;
    private String ciudad;
}
