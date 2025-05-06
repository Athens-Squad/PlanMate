package net.thechance.logic.use_cases.progression_state.progressionStateValidations

import net.thechance.data.progression_state.exceptions.DomainException

interface UseCaseValidator<T> {
	suspend fun validateBeforeCreation(entity: T): DomainException?
	suspend fun validateAfterCreation(entityId: String): DomainException?
}