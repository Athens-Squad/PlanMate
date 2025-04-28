package net.thechance.logic.use_cases.state

import com.thechance.logic.usecases.state.*

data class StatesUseCases(
    val createStateUseCase: CreateStateUseCase,
    val updateStateUseCase: UpdateStateUseCase,
    val deleteStateUseCase: DeleteStateUseCase,
    val getStateByIdUseCase: GetStateByIdUseCase,
    val getStatesByProjectIdUseCase: GetStatesByProjectIdUseCase
)
