package com.example.demo.application.dto;

public record PromotorCreadoEvent(Long idUsuario,
                                  String nombres,
                                  String apellidos,
                                  String correo,
                                  String tipoPromotor) {
}
