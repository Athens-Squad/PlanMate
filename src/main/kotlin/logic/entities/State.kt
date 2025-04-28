package net.thechance.logic.entities


import java.util.UUID


data class State(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val projectId: String
)