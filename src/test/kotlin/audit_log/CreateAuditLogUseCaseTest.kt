package logic.use_cases.audit_log

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
            userId = "user 1",
            createdAt = LocalDateTime.of(2025, 4, 28, 20, 0)
        )

        every { auditRepository.createAuditLog(auditLog) } just Runs

        val result = createAuditLogUseCase.execute(auditLog)
        assertThat(result).isTrue()
        verify(exactly = 1) { auditRepository.createAuditLog(auditLog) }

    }

    @Test
    fun `createAuditLog not create audit log if description is missing`() {
        //given
        val invalidAuditLog = AuditLog(
            entityType = EntityType.TASK,
            entityId = "TASK-001",
            description = "",
            userId = "user 1",
            createdAt = LocalDateTime.of(2025, 4, 28, 9, 0)
        )

        // when
        every { auditRepository.createAuditLog(invalidAuditLog) } just Runs

        // then
        createAuditLogUseCase.execute(invalidAuditLog)
        verify(exactly = 0) { auditRepository.createAuditLog(invalidAuditLog) }
    }
    @Test
    fun `createAuditLog not create audit log if entityId is missing`() {
        //given
        val invalidAuditLog = AuditLog(
            entityType = EntityType.TASK,
            entityId = "",
            description = "Task moved from TODO to InProgress",
            userId = "user 1",
            createdAt = LocalDateTime.of(2025, 4, 28, 9, 0)
        )

        // when
        every { auditRepository.createAuditLog(invalidAuditLog) } just Runs

        // then
        createAuditLogUseCase.execute(invalidAuditLog)
        verify(exactly = 0) { auditRepository.createAuditLog(invalidAuditLog) }
    }
    @Test
    fun `createAuditLog not create audit userId is missing`() {
        //given
        val invalidAuditLog = AuditLog(
            entityType = EntityType.TASK,
            entityId = "TASK-001",
            description = "Task moved from TODO to InProgress",
            userId = "",
            createdAt = LocalDateTime.of(2025, 4, 28, 9, 0)
        )

        // when
        every { auditRepository.createAuditLog(invalidAuditLog) } just Runs

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
            userId = "user2",
            createdAt = LocalDateTime.of(2025, 4, 28, 10, 0)
        )

        every { auditRepository.createAuditLog(auditLog) } throws RuntimeException("Database failure")
        // When:
        val result = createAuditLogUseCase.execute(auditLog)
        // Then:
        assertThat(result).isFalse()
        verify(exactly = 1) { auditRepository.createAuditLog(auditLog) }
    }
}