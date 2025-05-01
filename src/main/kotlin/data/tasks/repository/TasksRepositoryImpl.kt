package net.thechance.data.tasks.repository


import logic.repositories.TasksRepository
import net.thechance.data.tasks.data_source.TasksDataSource

class TasksRepositoryImpl(
    private val tasksDataSource: TasksDataSource
): TasksRepository, TasksDataSource by tasksDataSource {

}