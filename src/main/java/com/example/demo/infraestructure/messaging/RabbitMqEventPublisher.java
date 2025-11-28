package com.example.demo.infraestructure.messaging;

import com.example.demo.application.dto.PromotorCreadoEvent;
import com.example.demo.application.exceptions.EventPublishingException;
import com.example.demo.application.interfaces.external.IEventPublisher;
import com.example.demo.infraestructure.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class RabbitMqEventPublisher implements IEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMqEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publicarEventoCreacion(PromotorCreadoEvent evento) {
        log.info("INFRA MESSAGING: Publicando evento para promotor: {}", evento.correo());
        try {
            // Enviamos al Exchange
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_NAME,      // "buscame_events_exchange"
                    RabbitMQConfig.ROUTING_KEY_PROMOTOR, // "promotor.creado"
                    evento
            );

            log.debug("INFRA MESSAGING: Mensaje enviado exitosamente a RabbitMQ.");

        } catch (AmqpException e) {

            log.error("INFRA ERROR: Falló la conexión con RabbitMQ.", e);
            throw new EventPublishingException("Error de mensajería al notificar creación.", e);
        }
    }
}
