package com.qdivision.sandwichapi.controller

import com.qdivision.sandwichapi.entity.SandwichEntity
import com.qdivision.sandwichapi.repository.SandwichRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SandwichController(val sandwichRepository: SandwichRepository) {

    @GetMapping("/sandwiches")
    fun getSandwiches() =
        sandwichRepository.findAll()

    @PostMapping("/sandwiches")
    fun postSandwich(@RequestBody sandwich: SandwichEntity) =
        sandwichRepository.save(sandwich)

}