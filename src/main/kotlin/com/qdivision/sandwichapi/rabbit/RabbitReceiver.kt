package com.qdivision.sandwichapi.rabbit

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Component
import java.util.UUID

data class IngredientMessageHandler(
    val id: UUID,
    val handler: (msg: IngredientMessage) -> Any?
)

@Component
class RabbitReceiver {

    private val handlers = mutableListOf<IngredientMessageHandler>()

    fun registerHandler(id: UUID, handler: (msg: IngredientMessage) -> Any?) {
        handlers.add(IngredientMessageHandler(id = id, handler = handler))
    }

    fun unregisterHandler(id: UUID) {
        handlers.removeIf { it.id == id }
    }

    fun receiveMessage(rawMessage: String) {
        val message = jacksonObjectMapper().readValue<IngredientMessage>(rawMessage)
        handlers.filter { it.id == message.id }.forEach { it.handler(message) }
    }

}