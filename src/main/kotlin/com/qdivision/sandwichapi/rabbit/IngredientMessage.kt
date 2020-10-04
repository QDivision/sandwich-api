package com.qdivision.sandwichapi.rabbit

import java.util.UUID

data class Ingredient(
    val name: String,
    val emoji: String
)

data class IngredientMessage(
    val id: UUID,
    val ingredient: Ingredient
)