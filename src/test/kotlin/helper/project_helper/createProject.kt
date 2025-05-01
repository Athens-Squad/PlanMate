package helper.project_helper

import logic.entities.ProgressionState
import logic.entities.Project
import logic.entities.Task

const val validRecordString = "project1, Plan Mate, this is my description, user1"

fun createProject(
    id: String = "project1",
    name: String = "Plan Mate",
    description: String = "this is my description",
    states: MutableList<ProgressionState> = mutableListOf(),
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