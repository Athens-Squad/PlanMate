package helper.task_helper

import logic.entities.AuditLog
import logic.entities.Task
import net.thechance.logic.entities.EntityType
import net.thechance.logic.entities.State
import java.time.LocalDateTime

object FakeTask {
    val fakeTask = createTask()
    val fakeAuditLog = createAuditLog()
    val fakeUserName = getUserName()
    private fun createTask(
        id: String = "t1",
        title: String = "Task Title",
        description: String = "Task Description",
        currentStateId: String = "s1",
        currentStateName: String = "State Name",
        projectId: String = "p1"
    ) = Task(
        id = id,
        title = title,
        description = description,
        currentState = State(
            id = currentStateId,
            name = currentStateName,
            projectId = projectId
        ),
        projectId = projectId
    )

    private fun createAuditLog(
        id: String = "al1",
        entityType: EntityType = EntityType.TASK,
        entityId: String = "t1",
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
        userId = userName,
        createdAt = createdAt
    )

    private fun getUserName() = "Bilal_Azzam"
}