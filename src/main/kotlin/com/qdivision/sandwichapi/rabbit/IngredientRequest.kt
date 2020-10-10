package com.qdivision.sandwichapi.rabbit

import java.util.UUID

data class IngredientRequest(
    val id: UUID,
    val name: String
)