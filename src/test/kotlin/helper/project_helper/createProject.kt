package helper.project_helper

import logic.entities.Project
import logic.entities.Task
import net.thechance.logic.entities.State

const val validRecordString = "project1, Plan Mate, this is my description, user1"

fun createProject(
    id: String = "project1",
    name: String = "Plan Mate",
    description: String = "this is my description",
    states: MutableList<State> = mutableListOf(),
    tasks: MutableList<Task> = mutableListOf(),
    createdBy: String = "user1"
) = Project(
    id = id,
    name = name,
    description = description,
    states = states,
    tasks = tasks,
    createdBy = createdBy
)