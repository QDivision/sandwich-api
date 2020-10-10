package com.qdivision.sandwichapi.rabbit

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class RabbitSender(
    val rabbitTemplate: RabbitTemplate
) {

    fun send(rawMessage: IngredientMessage) {
        val exchangeName = RabbitConnection.topicExchangeName
        val routingKey = "food.sandwich"
        val message = jacksonObjectMapper().writeValueAsString(rawMessage)
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message)
    }

}