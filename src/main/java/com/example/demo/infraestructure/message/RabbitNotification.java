package com.example.demo.infraestructure.message;

import com.example.demo.application.event.PromotorCreateEvent;
import com.example.demo.domain.repository.PromotorCreate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


@Service
public class RabbitNotification implements PromotorCreate {



    private final RabbitTemplate rabbitTemplate;
    private final String exchange = "buscame_events_exchange";
    private final String routingKey = "promotor.created.event";

    public RabbitNotification(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void notificarNuevoPromotor(String correo, String passwordTemporal) {
        PromotorCreateEvent event = new PromotorCreateEvent(correo, passwordTemporal);
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
        System.out.println("Evento publicado envio para +" + correo);
    }
}
