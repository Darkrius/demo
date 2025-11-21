package com.example.demo.application.interfaces.asesores;

import com.example.demo.application.dto.query.PromotorDashBoard;

import java.util.List;

public interface PromotorDashboardQueryPort {

      long contarPorAdmin (String idAdminCreador);

  List<PromotorDashBoard> listarPorAdmin(String idAdminCreador, int page, int size);
}
