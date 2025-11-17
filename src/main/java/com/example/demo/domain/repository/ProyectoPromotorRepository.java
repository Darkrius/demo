package com.example.demo.domain.repository;

import java.util.List;

public interface ProyectoPromotorRepository {


    void asginarPromotor(Long idPromotor, List<Long> idProyectos);
}
