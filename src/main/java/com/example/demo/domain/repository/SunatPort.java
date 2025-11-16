package com.example.demo.domain.repository;

import java.util.Optional;

public interface SunatPort {
    Optional<String> obtenerRazonSocialPorRuc(String ruc);

}
