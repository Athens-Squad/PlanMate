package net.thechance

import logic.entities.Project
import logic.entities.Task
import logic.repositories.ProjectsRepository
import logic.repositories.TasksRepository
import net.thechance.di.appModule
import net.thechance.di.repositoriesModule
import org.koin.core.context.GlobalContext.get
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform.getKoin

fun main() {
    startKoin {
        modules(appModule, repositoriesModule)
    }
    val repo = get().get<TasksRepository>()

    val projectsRepo: ProjectsRepository = getKoin().get()

    val project = Project(
        id = "1",
        name = "Project 1",
        description = "Description 1",
        states = mutableListOf(),
        tasks = mutableListOf(),
        createdBy = "admin"
    )

    val updateProject = Project(
        id = "1",
        name = "Project 2",
        description = "new Description 1",
        states = mutableListOf(),
        tasks = mutableListOf(),
        createdBy = "new admin"
    )
    projectsRepo.createProject(project)
    projectsRepo.updateProject(updateProject)
    projectsRepo.getProjects().onSuccess {
        it.forEach { project ->
            println(project)
        }
    }


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