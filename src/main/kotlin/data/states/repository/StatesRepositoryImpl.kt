package net.thechance.data.states.repository

import logic.repositories.StatesRepository
import net.thechance.data.states.data_source.StatesDataSource
import net.thechance.data.tasks.data_source.TasksDataSource
import net.thechance.logic.entities.State

class StatesRepositoryImpl( private val statesDataSource: StatesDataSource
):StatesRepository  , StatesDataSource by statesDataSource{

}

