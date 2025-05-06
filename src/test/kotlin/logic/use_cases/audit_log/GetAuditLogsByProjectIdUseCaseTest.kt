package logic.use_cases.audit_log

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.entities.AuditLog
import logic.repositories.AuditRepository
import logic.entities.EntityType
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import java.time.LocalDateTime

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
  val projectId = "PROJECT-001"
  val expected = listOf(
   AuditLog(
    entityType = EntityType.PROJECT,
    entityId = projectId,
    description = "Project created",
    userName = "admin1",
    createdAt = LocalDateTime.of(2025, 4, 28, 8, 0)
   ),
   AuditLog(
    entityType = EntityType.PROJECT,
    entityId = projectId,
    description = "New state 'In QA' added",
    userName = "admin2",
    createdAt = LocalDateTime.of(2025, 4, 28, 11, 45)
   )
  )

  coEvery { auditRepository.getAuditLogs() } returns expected

  val result = getAuditLogsByProjectIdUseCase.execute(projectId)

  assertThat(result).isEqualTo(expected)
  coVerify(exactly = 1) { auditRepository.getAuditLogs() }
 }

 @Test
 fun `getAuditLogs returns empty list when repository throws exception`() = runTest {
  val projectId = "PROJECT-001"
  coEvery { auditRepository.getAuditLogs() } throws Exception("error")

  val result = getAuditLogsByProjectIdUseCase.execute(projectId)

  assertThat(result).isEmpty()
  coVerify(exactly = 1) { auditRepository.getAuditLogs() }
 }

 @Test
 fun `getAuditLogs returns empty list when invalid project id is given`() = runTest {
  val invalidProjectId = "PROJECT-XYZ123"
  coEvery { auditRepository.getAuditLogs() } returns emptyList()

  val result = getAuditLogsByProjectIdUseCase.execute(invalidProjectId)

  assertThat(result).isEmpty()
  coVerify(exactly = 1) { auditRepository.getAuditLogs() }
 }

 @Test
 fun `getAuditLogs returns empty list when project id is blank`() = runTest {
  val blankProjectId = ""

  val result = getAuditLogsByProjectIdUseCase.execute(blankProjectId)

  assertThat(result).isEmpty()
  coVerify(exactly = 0) { auditRepository.getAuditLogs() }
 }

 @Test
 fun `getAuditLogs returns empty list when no logs match the project id`() = runTest {
  val taskId = "TASK-001"
  val logs = listOf(
   AuditLog(
    entityType = EntityType.TASK,
    entityId = "Task001",
    description = "Task created",
    userName = "admin1",
    createdAt = LocalDateTime.of(2025, 4, 28, 8, 0)
   )
  )

  coEvery { auditRepository.getAuditLogs() } returns logs

  val result = getAuditLogsByProjectIdUseCase.execute(taskId)

  assertThat(result).isEmpty()
  coVerify(exactly = 1) { auditRepository.getAuditLogs() }
 }
}
