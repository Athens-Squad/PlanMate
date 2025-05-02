package data.states.repository

import logic.repositories.StatesRepository
import data.states.data_source.StatesDataSource


class StatesRepositoryImpl( private val statesDataSource: StatesDataSource
):StatesRepository  , StatesDataSource by statesDataSource{

}

