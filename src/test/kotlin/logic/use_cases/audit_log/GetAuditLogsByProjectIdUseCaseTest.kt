@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.audit_log

import com.google.common.truth.Truth.assertThat
import helper.auditlog.createTestAuditLog
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
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class GetAuditLogsByProjectIdUseCaseTest {

    private lateinit var auditRepository: AuditRepository
    private lateinit var getAuditLogsByProjectIdUseCase: GetAuditLogsByProjectIdUseCase

    @BeforeEach
    fun setUp() {
        auditRepository = mockk()
        getAuditLogsByProjectIdUseCase = GetAuditLogsByProjectIdUseCase(auditRepository)
    }

  @Test
    fun `getAuditLogs returns audit logs for given project id`() = runTest {
        //given
        val projectId =Uuid.random()
        val expected = listOf(
            createTestAuditLog(
                entityType = EntityType.PROJECT,
                entityId = projectId,
                description = "Project created",
                userName = "admin1",
                createdAt = LocalDateTime.of(2025, 4, 28, 8, 0)
            ),
            createTestAuditLog(
                entityType = EntityType.PROJECT,
                entityId = projectId,
                description = "New state 'In QA' added",
                userName = "admin2",
                createdAt = LocalDateTime.of(2025, 4, 28, 11, 45)
            )
        )

        coEvery { auditRepository.getAuditLogs() } returns expected
        //when

        val result = getAuditLogsByProjectIdUseCase.execute(projectId)

         //then
        assertThat(result).isEqualTo(expected)
        coVerify(exactly = 1) { auditRepository.getAuditLogs() }
    }


    @Test
    fun `getAuditLogs returns empty list when invalid project id is given`() = runTest {
        val invalidProjectId = Uuid.random()
        coEvery { auditRepository.getAuditLogs() } returns emptyList()


        val result = getAuditLogsByProjectIdUseCase.execute(invalidProjectId)

        assertThat(result).isEmpty()
        coVerify(exactly = 1) { auditRepository.getAuditLogs() }
    }


    @Test
    fun `getAuditLogs returns empty list when no logs match the project id`() = runTest {
        val projectId = Uuid.random()
        val logs = listOf(
            AuditLog(
                entityType = EntityType.TASK,
                entityId = Uuid.random(),
                description = "Task created",
                userName = "admin1",
                createdAt = LocalDateTime.of(2025, 4, 28, 8, 0)
            )
        )
        coEvery { auditRepository.getAuditLogs() } returns logs

        val result = getAuditLogsByProjectIdUseCase.execute(projectId)

        assertThat(result).isEmpty()
        coVerify(exactly = 1) { auditRepository.getAuditLogs() }
    }
}
