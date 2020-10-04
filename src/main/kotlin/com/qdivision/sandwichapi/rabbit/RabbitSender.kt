package com.qdivision.sandwichapi.rabbit

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class RabbitSender(
    val rabbitTemplate: RabbitTemplate
) {

    fun send(message: IngredientMessage) {
        val msg = ObjectMapper().writeValueAsString(message)

//    fun send(message: Any) {
        println("Sending message...")
        val exchangeName = RabbitConnection.topicExchangeName
        val routingKey = "food.sandwich"
        rabbitTemplate.convertAndSend(exchangeName, routingKey, msg)
    }

}