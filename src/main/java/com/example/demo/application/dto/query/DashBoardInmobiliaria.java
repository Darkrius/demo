package com.example.demo.application.dto.query;

import java.time.LocalDateTime;

public record DashBoardInmobiliaria(Long idInmobiliaria,
                                    String ruc,
                                    String razonSocial,
                                    boolean estado,
                                    LocalDateTime fechaModificacion,
                                    int nProyectos) {
}
