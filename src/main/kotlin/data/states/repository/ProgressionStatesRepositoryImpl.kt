package data.states.repository

import logic.repositories.StatesRepository
import data.states.data_source.ProgressionStatesDataSource


class ProgressionStatesRepositoryImpl(private val progressionStatesDataSource: ProgressionStatesDataSource
):StatesRepository  , ProgressionStatesDataSource by progressionStatesDataSource{

}

