package net.thechance.logic.use_cases.state

import com.thechance.logic.usecases.state.*
import logic.use_cases.state.CreateStateUseCase
import logic.use_cases.state.GetStateByIdUseCase
import logic.use_cases.state.GetStatesByProjectIdUseCase
import logic.use_cases.state.UpdateStateUseCase

data class StatesUseCases(
    val createStateUseCase: CreateStateUseCase,
    val updateStateUseCase: UpdateStateUseCase,
    val deleteStateUseCase: DeleteStateUseCase,
    val getStateByIdUseCase: GetStateByIdUseCase,
    val getStatesByProjectIdUseCase: GetStatesByProjectIdUseCase
)
