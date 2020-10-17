package com.qdivision.sandwichapi.controller

import com.qdivision.sandwichapi.entity.SandwichEntity
import com.qdivision.sandwichapi.rabbit.IngredientRequest
import com.qdivision.sandwichapi.rabbit.RabbitReceiver
import com.qdivision.sandwichapi.rabbit.RabbitSender
import com.qdivision.sandwichapi.repository.SandwichRepository
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.async.DeferredResult
import java.util.UUID

@RestController
@CrossOrigin(origins = ["http://localhost:4003"])
class SandwichController(
    val sandwichRepository: SandwichRepository,
    val receiver: RabbitReceiver,
    val sender: RabbitSender
) {

    @GetMapping("/sandwiches")
    fun getSandwiches() =
        sandwichRepository.findAll()

    @PostMapping("/sandwiches")
    fun postSandwich(@RequestBody sandwich: SandwichEntity): DeferredResult<Unit> {
        val result = DeferredResult<Unit>()

        val msg = IngredientRequest(id = UUID.randomUUID(), name = sandwich.bread.name)
        receiver.registerHandler(msg.id) {
            result.setResult(Unit)
            receiver.unregisterHandler(msg.id)
        }
        sender.send(msg)
        sandwichRepository.save(sandwich)

        return result
    }

}