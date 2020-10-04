package com.qdivision.sandwichapi.controller

import com.qdivision.sandwichapi.entity.SandwichEntity
import com.qdivision.sandwichapi.rabbit.Ingredient
import com.qdivision.sandwichapi.rabbit.IngredientMessage
import com.qdivision.sandwichapi.rabbit.RabbitReceiver
import com.qdivision.sandwichapi.rabbit.RabbitSender
import com.qdivision.sandwichapi.repository.SandwichRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class SandwichController(
    val sandwichRepository: SandwichRepository,
    val receiver: RabbitReceiver,
    val sender: RabbitSender
) {

    @GetMapping("/sandwiches")
    fun getSandwiches() =
        sandwichRepository.findAll()

//    @PostMapping("/sandwiches")
//    fun postSandwich(@RequestBody sandwich: SandwichEntity) =
//        sandwichRepository.save(sandwich)

    @PostMapping("/sandwiches")
    fun postSandwich(@RequestBody sandwich: SandwichEntity) {
        val msg = IngredientMessage(id = UUID.randomUUID(), ingredient = Ingredient(
            name = sandwich.bread.name,
            emoji = sandwich.bread.emoji
        ))
        receiver.registerHandler(msg.id) {
            println()
            println()
            println()
            println("OMG I GOT A MESSAGE: $it")
            println()
            println()
            println()
        }
        sender.send(msg)
        sandwichRepository.save(sandwich)
    }


}