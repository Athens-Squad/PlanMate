package logic.use_cases.audit_log

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.entities.AuditLog
import logic.entities.EntityType
import logic.repositories.AuditRepository
import org.junit.jupiter.api.BeforeEach
import java.time.LocalDateTime
import kotlin.test.Test

class GetAuditLogsByTaskIdUseCaseTest {

    private lateinit var auditRepository: AuditRepository
    private lateinit var getAuditLogsByTaskIdUseCase: GetAuditLogsByTaskIdUseCase

    @BeforeEach
    fun setUp() {
        auditRepository = mockk()
        getAuditLogsByTaskIdUseCase = GetAuditLogsByTaskIdUseCase(auditRepository)
    }

    @Test
    fun `getAuditLogs returns audit logs for given task id`() = runTest {
        val taskId = "TASK-001"
        val expected = listOf(
            AuditLog(
                entityType = EntityType.TASK,
                entityId = taskId,
                description = "Task created",
                userName = "user1",
                createdAt = LocalDateTime.of(2025, 4, 28, 9, 0)
            ),
            AuditLog(
                entityType = EntityType.TASK,
                entityId = taskId,
                description = "Task moved from TODO to In Progress",
                userName = "user2",
                createdAt = LocalDateTime.of(2025, 4, 28, 10, 30)
            )
        )

        coEvery { auditRepository.getAuditLogs() } returns expected

        val result = getAuditLogsByTaskIdUseCase.execute(taskId)

        assertThat(result).isEqualTo(expected)
        coVerify(exactly = 1) { auditRepository.getAuditLogs() }
    }

    @Test
    fun `getAuditLogs returns empty list when invalid task id is given`() = runTest {
        val invalidTaskId = "TASK-XYZ123"
        coEvery { auditRepository.getAuditLogs() } returns emptyList()

        val result = getAuditLogsByTaskIdUseCase.execute(invalidTaskId)

        assertThat(result).isEmpty()
        coVerify(exactly = 1) { auditRepository.getAuditLogs() }
    }

    @Test
    fun `getAuditLogs returns empty list when task id is blank`() = runTest {
        val blankTaskId = ""

        val result = getAuditLogsByTaskIdUseCase.execute(blankTaskId)

        assertThat(result).isEmpty()
        coVerify(exactly = 0) { auditRepository.getAuditLogs() }
    }

    @Test
    fun `getAuditLogs returns empty list when repository throws exception`() = runTest {
        val taskId = "TASK-001"
        coEvery { auditRepository.getAuditLogs() } throws Exception("error")

        val result = getAuditLogsByTaskIdUseCase.execute(taskId)

        assertThat(result).isEmpty()
        coVerify(exactly = 1) { auditRepository.getAuditLogs() }
    }

    @Test
    fun `getAuditLogs returns empty list when no logs match task id`() = runTest {
        val taskId = "TASK-001"
        val unrelatedLogs = listOf(
            AuditLog(
                entityType = EntityType.PROJECT,
                entityId = "PROJECT-001",
                description = "Project created",
                userName = "admin1",
                createdAt = LocalDateTime.of(2025, 4, 28, 8, 0)
            )
        )

        coEvery { auditRepository.getAuditLogs() } returns unrelatedLogs

        val result = getAuditLogsByTaskIdUseCase.execute(taskId)

        assertThat(result).isEmpty()
        coVerify(exactly = 1) { auditRepository.getAuditLogs() }
    }
}
