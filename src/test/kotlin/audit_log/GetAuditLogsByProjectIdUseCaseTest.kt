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
import net.thechance.logic.use_cases.audit_log.GetAuditLogsByProjectIdUseCase


class GetAuditLogsByProjectIdUseCaseTest{
 private lateinit var auditRepository: AuditRepository
 private lateinit var getAuditLogsByProjectIdUseCase: GetAuditLogsByProjectIdUseCase

 @BeforeEach
 fun setUp() {
  auditRepository = mockk()
  getAuditLogsByProjectIdUseCase = GetAuditLogsByProjectIdUseCase(auditRepository)
 }

 @Test
 fun `return audit logs for given project id`() {
  //given

  val projectId = "PROJECT-001"
  val expected = listOf(
   AuditLog(
    entityType = EntityType.PROJECT,
    entityId = projectId,
    description = "Project created",
    userId = "admin1",
    createdAt = LocalDateTime.of(2025, 4, 28, 8, 0)
   ),
   AuditLog(
    entityType = EntityType.PROJECT,
    entityId = projectId,
    description = "New state 'In QA' added",
    userId = "admin2",
    createdAt = LocalDateTime.of(2025, 4, 28, 11, 45)
   )
  )

  every { auditRepository.getAuditLogs() } returns expected

  //when
  val result = getAuditLogsByProjectIdUseCase.execute(projectId)

   //then
  assertThat(result).isEqualTo(expected)
  verify(exactly = 1) { auditRepository.getAuditLogs() }
 }




 @Test
 fun `getAuditLogs() return empty list when an invalid ProjectId Given`()  {
  //given
  val invalidProjectId = "PROJECT-XYZ123"

  every { auditRepository.getAuditLogs() } returns emptyList()

  //when
  val result = getAuditLogsByProjectIdUseCase.execute(invalidProjectId)

  // then
  assertThat(result).isEmpty()
  verify(exactly = 1) { auditRepository.getAuditLogs() }
 }






 }