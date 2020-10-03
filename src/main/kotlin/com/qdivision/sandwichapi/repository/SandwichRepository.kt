package com.qdivision.sandwichapi.repository

import com.qdivision.sandwichapi.entity.SandwichEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SandwichRepository: CrudRepository<SandwichEntity, String>