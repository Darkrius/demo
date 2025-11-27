package com.example.demo.domain.dto;

import java.time.LocalDateTime;

public record InmobiliariaDashBoard(Long idInmobiliaria,
                                    String ruc,
                                    String razonSocial,
                                    int nProyectos,
                                    boolean estado,
                                    LocalDateTime fechaModificacion,
                                    long totalRegistros) {
}
