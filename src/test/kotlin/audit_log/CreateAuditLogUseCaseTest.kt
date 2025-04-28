package logic.use_cases.audit_log

import com.google.common.truth.Truth.assertThat
import com.thechance.logic.usecases.audit.CreateAuditLogUseCase
import io.mockk.every
import io.mockk.mockk
import logic.entities.AuditLog
import logic.repositories.AuditRepository
import net.thechance.logic.entities.EntityType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import java.time.LocalDateTime
import kotlin.test.Test
import io.mockk.*

class CreateAuditLogUseCaseTest{

  private lateinit var auditRepository: AuditRepository
  private lateinit var createAuditLogUseCase: CreateAuditLogUseCase

  @BeforeEach
  fun setUp(){
   auditRepository= mockk()
   createAuditLogUseCase= CreateAuditLogUseCase(auditRepository)
  }

 @Test
 fun ` create audit log successfully`() {
  val auditLog = AuditLog(
   id = "AUD-001",
   entityType = EntityType.TASK,
   entityId = "TASK-001",
   description = "Task moved from TODO to InProgress",
   userId = "user 1",
   createdAt = LocalDateTime.of(2025,4,28,20,0)
  )

    every { auditRepository.createAuditLog(auditLog) } just Runs

  val result = createAuditLogUseCase.execute(auditLog)
  assertThat(result).isTrue()
  verify(exactly = 1) { auditRepository.createAuditLog(auditLog) }

 }

    @Test
    fun `createAuditLog not create audit log if any fields are missing`() {
        //given
        val invalidAuditLog = AuditLog(
            entityType = EntityType.TASK,
            entityId = "",
            description = "",
            userId = "user1",
            createdAt = LocalDateTime.of(2025, 4, 28, 9, 0)
        )

       // when
        every { auditRepository.createAuditLog(invalidAuditLog) } just Runs

         // then
        createAuditLogUseCase.execute(invalidAuditLog)
        verify(exactly = 1) { auditRepository.createAuditLog(invalidAuditLog) }
    }


}