package com.qdivision.sandwichapi.rabbit

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class RabbitSender(
    val rabbitTemplate: RabbitTemplate
) {

    fun send(rawMessage: IngredientRequest) {
        val exchangeName = RabbitConnection.topicExchangeName
        val routingKey = RabbitConnection.requestRoutingKey
        val message = jacksonObjectMapper().writeValueAsString(rawMessage)
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message)
    }

}