package com.qdivision.sandwichapi.rabbit

import com.fasterxml.jackson.databind.ObjectMapper
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

    fun receiveMessage(message: String) {
//        val msg = ObjectMapper().readValue(message, IngredientMessage::class.java)
        val msg = jacksonObjectMapper().readValue<IngredientMessage>(message)

//    fun receiveMessage(message: IngredientMessage) {
        println("MESSAGE: $msg")
        handlers.filter { it.id == msg.id }.forEach { it.handler(msg) }
    }

}