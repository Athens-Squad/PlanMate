package net.thechance.logic.use_cases.state

import logic.use_cases.state.*

data class StatesUseCases(
    val createStateUseCase: CreateStateUseCase,
    val updateStateUseCase: UpdateStateUseCase,
    val deleteStateUseCase: DeleteStateUseCase,
    val getStateByIdUseCase: GetStateByIdUseCase,
    val getStatesByProjectIdUseCase: GetStatesByProjectIdUseCase
)
