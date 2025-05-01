package audit_log

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.entities.AuditLog
import logic.repositories.AuditRepository
import net.thechance.logic.entities.EntityType
import org.junit.jupiter.api.BeforeEach
import java.time.LocalDateTime
import kotlin.test.Test
import io.mockk.*
import logic.use_cases.audit_log.CreateAuditLogUseCase

class CreateAuditLogUseCaseTest {

    private lateinit var auditRepository: AuditRepository
    private lateinit var createAuditLogUseCase: CreateAuditLogUseCase

    @BeforeEach
    fun setUp() {
        auditRepository = mockk()
        createAuditLogUseCase = CreateAuditLogUseCase(auditRepository)
    }

    @Test
    fun ` create audit log successfully`() {
        val auditLog = AuditLog(
            id = "AUD-001",
            entityType = EntityType.TASK,
            entityId = "TASK-001",
            description = "Task moved from TODO to InProgress",
            userName = "user 1",
            createdAt = LocalDateTime.of(2025, 4, 28, 20, 0)
        )

        every { auditRepository.createAuditLog(auditLog) } returns Result.success(Unit)

        val result = createAuditLogUseCase.execute(auditLog)
        assertThat(result.isSuccess).isTrue()
        verify(exactly = 1) { auditRepository.createAuditLog(auditLog) }

    }

    @Test
    fun `createAuditLog not create audit log when description is missing`() {
        //given
        val invalidAuditLog = AuditLog(
            entityType = EntityType.TASK,
            entityId = "TASK-001",
            description = "",
            userName = "user 1",
            createdAt = LocalDateTime.of(2025, 4, 28, 9, 0)
        )

        // when
        every { auditRepository.createAuditLog(invalidAuditLog) } returns Result.success(Unit)

        // then
        createAuditLogUseCase.execute(invalidAuditLog)
        verify(exactly = 0) { auditRepository.createAuditLog(invalidAuditLog) }
    }
    @Test
    fun `createAuditLog not create audit log when entityId is missing`() {
        //given
        val invalidAuditLog = AuditLog(
            entityType = EntityType.TASK,
            entityId = "",
            description = "Task moved from TODO to InProgress",
            userName = "user 1",
            createdAt = LocalDateTime.of(2025, 4, 28, 9, 0)
        )

        // when
        every { auditRepository.createAuditLog(invalidAuditLog) } returns Result.success(Unit)

        // then
        createAuditLogUseCase.execute(invalidAuditLog)
        verify(exactly = 0) { auditRepository.createAuditLog(invalidAuditLog) }
    }
    @Test
    fun `createAuditLog not create log when  userId is missing`() {
        //given
        val invalidAuditLog = AuditLog(
            entityType = EntityType.TASK,
            entityId = "TASK-001",
            description = "Task moved from TODO to InProgress",
            userName = "",
            createdAt = LocalDateTime.of(2025, 4, 28, 9, 0)
        )

        // when
        every { auditRepository.createAuditLog(invalidAuditLog) } returns Result.success(Unit)

        // then
        createAuditLogUseCase.execute(invalidAuditLog)
        verify(exactly = 0) { auditRepository.createAuditLog(invalidAuditLog) }
    }

    @Test
    fun `should return false when audit repository throws exception`() {
        // Given:
        val auditLog = AuditLog(
            id = "AUD-002",
            entityType = EntityType.PROJECT,
            entityId = "PROJ-001",
            description = "Project state changed",
            userName = "user2",
            createdAt = LocalDateTime.of(2025, 4, 28, 10, 0)
        )

        every { auditRepository.createAuditLog(auditLog) } throws RuntimeException("Failed to create audit log")
        // When:
        val result = createAuditLogUseCase.execute(auditLog)
        // Then:
        assertThat(result.isFailure).isTrue()
        verify(exactly = 1) { auditRepository.createAuditLog(auditLog) }
    }
}