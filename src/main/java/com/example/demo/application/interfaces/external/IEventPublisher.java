package com.example.demo.application.interfaces.external;

import com.example.demo.application.dto.PromotorCreadoEvent;

public interface IEventPublisher {

    void publicarEventoCreacion(PromotorCreadoEvent evento);
}
