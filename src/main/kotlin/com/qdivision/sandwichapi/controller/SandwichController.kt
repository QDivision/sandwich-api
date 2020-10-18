package com.qdivision.sandwichapi.controller

import com.qdivision.sandwichapi.entity.SandwichEntity
import com.qdivision.sandwichapi.rabbit.IngredientRequest
import com.qdivision.sandwichapi.rabbit.RabbitReceiver
import com.qdivision.sandwichapi.rabbit.RabbitSender
import com.qdivision.sandwichapi.repository.SandwichRepository
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.async.DeferredResult
import java.lang.Exception
import java.util.UUID

@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidIngredientException(ingredient: String): Exception("No such ingredient exists: '$ingredient'")

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

        val ingredients = listOf(sandwich.bread) + sandwich.condiments + sandwich.layers
        val requests = ingredients.map { IngredientRequest(id = UUID.randomUUID(), name = it.name) }

        val results = mutableListOf<String?>()

        val onFinish = {
            val hit = results.find { it is String }
            if (hit != null) {
                result.setErrorResult(InvalidIngredientException(hit))
            } else {
                sandwichRepository.save(sandwich)
                result.setResult(Unit)
            }
        }

        requests.forEach { req ->
            receiver.registerHandler(req.id) {
                results.add(if (it.exists) null else req.name)
                receiver.unregisterHandler(req.id)
                if (results.size == requests.size) onFinish()
            }
            sender.send(req)
        }

        return result
    }

}