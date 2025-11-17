package com.example.demo.domain.repository;

import java.time.LocalDateTime;

public record DashBoardInmobiliaria(Long idInmobiliaria,
                                    String ruc,
                                    String razonSocial,
                                    boolean estado,
                                    LocalDateTime fechaModificacion,
                                    int nProyectos) {
}
