package net.thechance

import logic.entities.Task
import logic.repositories.TasksRepository
import net.thechance.di.appModule
import net.thechance.di.repositoriesModule
import org.koin.core.context.GlobalContext.get
import org.koin.core.context.startKoin

fun main() {
    startKoin {
        modules(appModule, repositoriesModule)
    }
    val repo = get().get<TasksRepository>()

    val task = Task(
        title = "Task 1",
        description = "Description 1",
        currentState = net.thechance.logic.entities.State(
            id = "1",
            name = "State 1",
            projectId = "p1"
        ),
        projectId = "p1"
    )
    repo.createTask(task)

    repo.getTasks().onSuccess {
        it.forEach {
            println(it)
        }
    }
}