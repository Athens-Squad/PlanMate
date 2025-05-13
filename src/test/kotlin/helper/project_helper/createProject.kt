package helper.project_helper

import logic.entities.ProgressionState
import logic.entities.Project
import logic.entities.Task
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

const val validRecordString = "project1, Plan Mate, this is my description, user1"

@OptIn(ExperimentalUuidApi::class)
fun createProject(
    id: Uuid = Uuid.random(),
    name: String = "Plan Mate",
    description: String = "this is my description",
    createdBy: String = "user1"
) = Project(
    id = id,
    name = name,
    description = description,
    createdByUserName = createdBy
)