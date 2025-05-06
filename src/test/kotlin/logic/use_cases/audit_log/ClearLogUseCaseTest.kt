package logic.use_cases.audit_log

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.repositories.AuditRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class ClearLogUseCaseTest {

    private lateinit var auditRepository: AuditRepository
    private lateinit var clearLogUseCase: ClearLogUseCase

    @BeforeTest
    fun setUp() {
        auditRepository = mockk()
        clearLogUseCase = ClearLogUseCase(auditRepository)
    }

    @Test
    fun `should clear all audit logs`() = runTest {
        // Given
        coEvery { auditRepository.clearLog() } returns Unit

        // When
        clearLogUseCase.execute()

        // Then
        coVerify { auditRepository.clearLog() }
    }

    @Test
    fun `handle when there are no logs to clear`() = runTest {
        // Given: Same behavior, still calls clear
        coEvery { auditRepository.clearLog() } returns Unit

        // When
        clearLogUseCase.execute()

        // Then
        coVerify { auditRepository.clearLog() }
    }

    @Test
    fun `handle failure when clearLog fails`() = runTest {
        // Given
        coEvery { auditRepository.clearLog() } throws Exception("Failed to clear logs")

        // When & Then
        val exception = assertFailsWith<Exception> {
            clearLogUseCase.execute()
        }
        assert(exception.message == "Failed to clear logs")

        coVerify { auditRepository.clearLog() }
    }
}
