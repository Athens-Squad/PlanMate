package data.tasks.repository


import data.tasks.data_source.TasksDataSource
import logic.repositories.TasksRepository

class TasksRepositoryImpl(
    private val tasksDataSource: TasksDataSource
) : TasksRepository, TasksDataSource by tasksDataSource