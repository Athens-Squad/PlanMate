@file:OptIn(ExperimentalUuidApi::class, ExperimentalUuidApi::class)

package helper.task_helper

import logic.entities.AuditLog
import logic.entities.Task
import logic.entities.EntityType
import logic.entities.ProgressionState
import net.thechance.data.tasks.data_source.remote.mongo.dto.TaskDto
import java.time.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object FakeTask {
    @OptIn(ExperimentalUuidApi::class)
    val fakeTask = createTask()
    @OptIn(ExperimentalUuidApi::class)
    val fakeAuditLog = createAuditLog()
    val fakeUserName = getUserName()
    @OptIn(ExperimentalUuidApi::class)
    val  fakeTaskDto = createTaskDto()

    @OptIn(ExperimentalUuidApi::class)
    private fun createTask(
        id : Uuid = Uuid.random(),
        title: String = "Task Title",
        description: String = "Task Description",
        currentStateId: Uuid = Uuid.random(),
        currentStateName: String = "State Name",
        projectId: Uuid = Uuid.random()
    ) = Task(
        id = id,
        name = title,
        description = description,
        currentProgressionState = ProgressionState(
            id = currentStateId,
            name = currentStateName,
            projectId = projectId
        ),
        projectId = projectId
    )

    private fun createAuditLog(
        id: Uuid = Uuid.parse("6408e8da-f8a0-402d-a26f-6389212b0d1b"),
        entityType: EntityType = EntityType.TASK,
        entityId: Uuid = Uuid.random(),
        description: String = "Task created successfully.",
        userName: String = getUserName(),
        createdAt: LocalDateTime = LocalDateTime.of(
            2025,
            4,
            30,
            4,
            0,
            0,
            0
        )
    ): AuditLog = AuditLog(
        id = id,
        entityType = entityType,
        entityId = entityId,
        description = description,
        userName = userName,
        createdAt = createdAt
    )

    @OptIn(ExperimentalUuidApi::class)

    private fun createTaskDto (
        id : Uuid = Uuid.random(),
        name :String = " Create New TaskDto",
        description: String = "Task Description ",
        currentStateId: Uuid = Uuid.random(),
        currentStateName: String = "State Name",
        projectId: Uuid = Uuid.random()
        ) = TaskDto(
            id = id ,
            name = name ,
            description =description,
        currentProgressionState = ProgressionState(
            id =currentStateId,
            name = currentStateName,
            projectId = projectId
        ),
            projectId = projectId
    )

    private fun getUserName() = "Bilal_Azzam"
}