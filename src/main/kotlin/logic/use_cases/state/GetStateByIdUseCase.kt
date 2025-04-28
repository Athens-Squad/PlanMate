package com.thechance.logic.usecases.state

import logic.entities.State
import logic.repositories.StatesRepository


class GetStateByIdUseCase(private val stateRepository: StatesRepository) {
    fun execute(stateId: String): State? {
        return null
    }
}