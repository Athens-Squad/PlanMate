package data.states.repository

import data.progression_state.data_source.ProgressionStateDataSource
import logic.repositories.ProgressionStateRepository


class ProgressionStatesRepositoryImpl(
    private val progressionStatesDataSource: ProgressionStateDataSource
) : ProgressionStateRepository, ProgressionStateDataSource by progressionStatesDataSource {

}

