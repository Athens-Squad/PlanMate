package com.thechance.logic.usecases.state

import logic.entities.State
import logic.repositories.StatesRepository


class GetStatesByProjectIdUseCase(private val stateRepository: StatesRepository) {
    fun execute(projectId: String): List<State> {
        return emptyList()
    }
}
