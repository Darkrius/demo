package com.example.demo.application.interfaces.asesores.promotor;

import com.example.demo.domain.entities.Promotor;

public interface CrearPromotorUseCase {

    Promotor crear (CrearPromotorCommand command, String idAdminCreador);
}
