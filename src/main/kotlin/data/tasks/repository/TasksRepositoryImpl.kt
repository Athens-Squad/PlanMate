package data.tasks.repository


import logic.repositories.TasksRepository
import data.tasks.data_source.TasksDataSource

class TasksRepositoryImpl(
    private val tasksDataSource: TasksDataSource
): TasksRepository, TasksDataSource by tasksDataSource {

}