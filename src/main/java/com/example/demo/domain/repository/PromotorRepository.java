package com.example.demo.domain.repository;

import com.example.demo.domain.model.Promotor;

public interface PromotorRepository {

    Long guardarPromotor(Promotor promotor);

    void guardarProyectosPromotor(Long idUsuario, Long idProyecto);


}
