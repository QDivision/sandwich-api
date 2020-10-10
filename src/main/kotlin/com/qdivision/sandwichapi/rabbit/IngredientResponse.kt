package com.qdivision.sandwichapi.rabbit

import java.util.UUID

data class IngredientResponse(
    val id: UUID,
    val exists: Boolean
)