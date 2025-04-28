package audit_log

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.AuditLog
import logic.repositories.AuditRepository
import net.thechance.logic.entities.EntityType
import net.thechance.logic.use_cases.audit_log.GetAuditLogsByTaskIdUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import java.time.LocalDateTime
import kotlin.math.E
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
    fun ` getAuditLogs() return audit logs for given task id`() {
          //given
        val taskId = "TASK-001"
        val expected = listOf(
            AuditLog(
                entityType = EntityType.TASK,
                entityId = taskId,
                description = "Task created",
                userId = "user1",
                createdAt = LocalDateTime.of(2025, 4, 28, 9, 0)
            ),
            AuditLog(
                entityType = EntityType.TASK,
                entityId = taskId,
                description = "Task moved from TODO to In Progress",
                userId = "user2",
                createdAt = LocalDateTime.of(2025, 4, 28, 10, 30)
            )
        )

        every { auditRepository.getAuditLogs() } returns expected

        //when

        val result = getAuditLogsByTaskIdUseCase.execute(taskId)

        //then
        assertThat(result).isEqualTo(expected)
        verify(exactly = 1) { auditRepository.getAuditLogs() }


    }



    @Test
    fun `getAuditLogs() return empty list when an invalid TaskId Given`() {
        // given
        val invalidTaskId = "TASK-XYZ123"


        every { auditRepository.getAuditLogs() } returns emptyList()

        //when
        val result = getAuditLogsByTaskIdUseCase.execute(invalidTaskId)

        //then
        assertThat(result).isEmpty()
        verify(exactly = 1) { auditRepository.getAuditLogs() }
    }

    @Test
    fun `getAuditLogs return empty list when TaskId is blank`() {
        // Given
        val blankTaskId = ""


        // When
        val result = getAuditLogsByTaskIdUseCase.execute(blankTaskId)

        // Then
        assertThat(result).isEmpty()
        verify(exactly = 0) { auditRepository.getAuditLogs() }


    }





    @Test
    fun `getAuditLogs() return empty list when repository throws exception`() {
        // Given
        val taskId = "Task-001"


        every { auditRepository.getAuditLogs() } throws Exception("Database error")

        // When
        val result = getAuditLogsByTaskIdUseCase.execute(taskId)

        // Then
        assertThat(result).isEmpty()
        verify(exactly = 1) { auditRepository.getAuditLogs() }
    }
    @Test
    fun ` getAuditLogs() return empty list when no logs match the task id`() {
        // Given
        val taskId = "TASK-001"
        val noMatchingLogs = listOf(
            AuditLog(
                entityType = EntityType.PROJECT,
                entityId = "PROJECT-001",
                description = "Project created",
                userId = "admin1",
                createdAt = LocalDateTime.of(2025, 4, 28, 8, 0)
            )
        )

        every { auditRepository.getAuditLogs() } returns noMatchingLogs

        // When
        val result = getAuditLogsByTaskIdUseCase.execute(taskId)

        // Then
        assertThat(result).isEmpty()
        verify(exactly = 1) { auditRepository.getAuditLogs() }
    }




}