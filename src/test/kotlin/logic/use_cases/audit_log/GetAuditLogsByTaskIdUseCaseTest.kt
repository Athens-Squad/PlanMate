@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.audit_log

import com.google.common.truth.Truth.assertThat
import helper.auditlog.createTestAuditLog
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.entities.AuditLog
import logic.entities.EntityType
import logic.repositories.AuditRepository
import net.thechance.logic.exceptions.InvalidEntityIdForAuditLog
import net.thechance.logic.use_cases.audit_log.auditLogValidations.AuditLogValidator
import org.junit.jupiter.api.BeforeEach
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class GetAuditLogsByTaskIdUseCaseTest {

    private lateinit var auditRepository: AuditRepository
    private lateinit var getAuditLogsByTaskIdUseCase: GetAuditLogsByTaskIdUseCase
    private lateinit var logValidator: AuditLogValidator

    @BeforeEach
    fun setUp() {
        auditRepository = mockk()
        logValidator = mockk()
        getAuditLogsByTaskIdUseCase = GetAuditLogsByTaskIdUseCase(auditRepository, logValidator)
    }

    @Test
    fun `getAuditLogs returns audit logs for given task id`() = runTest {
        val taskId = Uuid.random()
        val expected = listOf(
            createTestAuditLog(
                entityType = EntityType.TASK,
                entityId = taskId,
                description = "Task created",
                userName = "user1",
                createdAt = LocalDateTime.of(2025, 4, 28, 9, 0)
            ),
            createTestAuditLog(
                entityType = EntityType.TASK,
                entityId = taskId,
                description = "Moved to In Progress",
                userName = "user2",
                createdAt = LocalDateTime.of(2025, 4, 28, 10, 30)
            )
        )
        every { logValidator.validateAfterCreation(taskId) } returns true
        coEvery { auditRepository.getAuditLogs() } returns expected

        val result = getAuditLogsByTaskIdUseCase.execute(taskId)

        assertThat(result).isEqualTo(expected)
        coVerify(exactly = 1) { auditRepository.getAuditLogs() }
    }


    @Test
    fun `getAuditLogs returns empty list when invalid task id is given`() = runTest {
        val invalidTaskId = Uuid.random()
        every { logValidator.validateAfterCreation(invalidTaskId) } returns false
        coEvery { auditRepository.getAuditLogs() } returns emptyList()


        val result = getAuditLogsByTaskIdUseCase.execute(invalidTaskId)

        assertThat(result).isEmpty()
        coVerify(exactly = 1) { auditRepository.getAuditLogs() }
    }


    @Test
    fun `getAuditLogs returns empty list when no logs match task id`() = runTest {
        val taskId = Uuid.random()
        val unrelatedLogs = listOf(
            AuditLog(
                entityType = EntityType.PROJECT,
                entityId = Uuid.random(),
                description = "Project created",
                userName = "admin1",
                createdAt = LocalDateTime.of(2025, 4, 28, 8, 0)
            )
        )

        every { logValidator.validateAfterCreation(taskId) } returns true
        coEvery { auditRepository.getAuditLogs() } returns unrelatedLogs

        val result = getAuditLogsByTaskIdUseCase.execute(taskId)

        assertThat(result).isEmpty()
        coVerify(exactly = 1) { auditRepository.getAuditLogs() }
    }
}
