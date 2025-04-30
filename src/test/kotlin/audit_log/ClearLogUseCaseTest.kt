package audit_log

import io.mockk.*
import logic.repositories.AuditRepository
import net.thechance.logic.use_cases.audit_log.ClearLogUseCase
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class ClearLogUseCaseTest{

  private lateinit var auditRepository: AuditRepository
  private lateinit var clearLogUseCase: ClearLogUseCase

  @BeforeEach
  fun setUp(){
   auditRepository= mockk()
   clearLogUseCase=ClearLogUseCase(auditRepository)
  }
 @Test
 fun `should clear all audit logs`() {
  //given
  every {  auditRepository.clearLog()  } returns Result.success(Unit)
 //when
  clearLogUseCase.execute()
  //then
  verify { auditRepository.clearLog() }


 }


    @Test
    fun ` handle  when there are no logs to clear`() {
        // Given:
        every { auditRepository.clearLog() } returns Result.success(Unit)

        // When
        clearLogUseCase.execute()

        // Then
        verify { auditRepository.clearLog() }
    }


    @Test
    fun ` handle failure when clearLog fails`() {
        // Given
        every { auditRepository.clearLog() } throws Exception("Failed to clear logs")

        // When & Then
        try {
            clearLogUseCase.execute()
        } catch (e: Exception) {

            assert(e.message == "Failed to clear logs")
        }

        verify { auditRepository.clearLog() }
    }



}