package logic.use_cases.audit_log

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.entities.AuditLog
import logic.repositories.AuditRepository
import logic.entities.EntityType
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import java.time.LocalDateTime

class CreateAuditLogUseCaseTest {

    private lateinit var auditRepository: AuditRepository
    private lateinit var createAuditLogUseCase: CreateAuditLogUseCase

    @BeforeTest
    fun setUp() {
        auditRepository = mockk()
        createAuditLogUseCase = CreateAuditLogUseCase(auditRepository)
    }

    @Test
    fun `create audit log successfully`() = runTest {
        val auditLog = AuditLog(
            id = "AUD-001",
            entityType = EntityType.TASK,
            entityId = "TASK-001",
            description = "Task moved from TODO to InProgress",
            userName = "user 1",
            createdAt = LocalDateTime.of(2025, 4, 28, 20, 0)
        )

        coEvery { auditRepository.createAuditLog(auditLog) } returns Unit

        createAuditLogUseCase.execute(auditLog)

        coVerify(exactly = 1) { auditRepository.createAuditLog(auditLog) }
    }

    @Test
    fun `should not create audit log when description is missing`() = runTest {
        val invalidAuditLog = AuditLog(
            entityType = EntityType.TASK,
            entityId = "TASK-001",
            description = "",
            userName = "user 1",
            createdAt = LocalDateTime.of(2025, 4, 28, 9, 0)
        )

        createAuditLogUseCase.execute(invalidAuditLog)

        coVerify(exactly = 0) { auditRepository.createAuditLog(any()) }
    }

    @Test
    fun `should not create audit log when entityId is missing`() = runTest {
        val invalidAuditLog = AuditLog(
            entityType = EntityType.TASK,
            entityId = "",
            description = "Task moved from TODO to InProgress",
            userName = "user 1",
            createdAt = LocalDateTime.of(2025, 4, 28, 9, 0)
        )

        createAuditLogUseCase.execute(invalidAuditLog)

        coVerify(exactly = 0) { auditRepository.createAuditLog(any()) }
    }

    @Test
    fun `should not create audit log when userName is missing`() = runTest {
        val invalidAuditLog = AuditLog(
            entityType = EntityType.TASK,
            entityId = "TASK-001",
            description = "Task moved from TODO to InProgress",
            userName = "",
            createdAt = LocalDateTime.of(2025, 4, 28, 9, 0)
        )

        createAuditLogUseCase.execute(invalidAuditLog)

        coVerify(exactly = 0) { auditRepository.createAuditLog(any()) }
    }

    @Test
    fun `should throw exception when repository fails`() = runTest {
        val auditLog = AuditLog(
            id = "AUD-002",
            entityType = EntityType.PROJECT,
            entityId = "PROJ-001",
            description = "Project state changed",
            userName = "user2",
            createdAt = LocalDateTime.of(2025, 4, 28, 10, 0)
        )

        coEvery { auditRepository.createAuditLog(auditLog) } throws RuntimeException("Failed to create audit log")

        val exception = assertFailsWith<RuntimeException> {
            createAuditLogUseCase.execute(auditLog)
        }

        assertThat(exception.message).isEqualTo("Failed to create audit log")
        coVerify(exactly = 1) { auditRepository.createAuditLog(auditLog) }
    }
}
