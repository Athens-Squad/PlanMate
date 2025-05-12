@file:OptIn(ExperimentalUuidApi::class, ExperimentalUuidApi::class)

package helper.task_helper

import logic.entities.AuditLog
import logic.entities.Task
import logic.entities.EntityType
import logic.entities.ProgressionState
import java.time.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object FakeTask {
    @OptIn(ExperimentalUuidApi::class)
    val fakeTask = createTask()
    val fakeAuditLog = createAuditLog()
    val fakeUserName = getUserName()
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
        id: Uuid = Uuid.random(),
        entityType: EntityType = EntityType.TASK,
        entityId: Uuid = Uuid.random(),
        description: String = "audit log desc",
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

    private fun getUserName() = "Bilal_Azzam"
}