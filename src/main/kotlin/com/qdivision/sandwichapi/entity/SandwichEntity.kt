package com.qdivision.sandwichapi.entity

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

data class Bread(
    val name: String,
    val emoji: String
)

data class Condiment(
    val name: String,
    val emoji: String
)

data class Layer(
    val name: String,
    val emoji: String
)

@Entity
@Table(name = "sandwiches")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
data class SandwichEntity(
    @Id
    var name: String,

    @Type(type = "jsonb")
    var bread: Bread,

    @Type(type = "jsonb")
    var condiments: List<Condiment>,

    @Type(type = "jsonb")
    var layers: List<Layer>
)