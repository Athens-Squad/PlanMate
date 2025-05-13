@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.audit_log
import com.google.common.truth.Truth.assertThat
import helper.auditlog.createTestAuditLog
import io.mockk.*
import kotlinx.coroutines.test.runTest
import logic.entities.EntityType
import logic.repositories.AuditRepository
import net.thechance.logic.exceptions.InvalidAuditLogFieldsException
import net.thechance.logic.validators.auditLogValidations.AuditLogValidator
import java.time.LocalDateTime
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.uuid.ExperimentalUuidApi

class CreateAuditLogUseCaseTest {

    private lateinit var auditRepository: AuditRepository
    private lateinit var createAuditLogUseCase: CreateAuditLogUseCase
    private lateinit var logValidator: AuditLogValidator

    @BeforeTest
    fun setUp() {
        auditRepository = mockk()
        logValidator= mockk()
        createAuditLogUseCase = CreateAuditLogUseCase(auditRepository,logValidator)
    }

    @Test
    fun `create audit log successfully`() = runTest {

        //given
        val auditLog = createTestAuditLog(
            entityType = EntityType.TASK,
            description = "Task moved from TODO to InProgress",
            userName = "user 1",
            createdAt = LocalDateTime.of(2025, 4, 28, 20, 0)
        )
        every { logValidator.validateAuditLogFieldsNotBlank(auditLog) } returns true
        coEvery { auditRepository.createAuditLog(auditLog) } returns Unit

        //when
        createAuditLogUseCase.execute(auditLog)

       //then
        verify { logValidator.validateAuditLogFieldsNotBlank(auditLog) }
        coVerify(exactly = 1) { auditRepository.createAuditLog(auditLog) }
    }

    @Test
    fun `should not create audit log when description is missing`() = runTest {
        //given
        val invalidAuditLog = createTestAuditLog(description = "")
         coEvery { logValidator.validateAuditLogFieldsNotBlank(invalidAuditLog) } throws InvalidAuditLogFieldsException()

         //when
        assertFailsWith<InvalidAuditLogFieldsException> { createAuditLogUseCase.execute(invalidAuditLog) }


        //then
        verify { logValidator.validateAuditLogFieldsNotBlank(invalidAuditLog) }
        coVerify(exactly = 0) { auditRepository.createAuditLog(any()) }
    }


    @Test
    fun `should not create audit log when userName is missing`() = runTest {
        //given
        val invalidAuditLog = createTestAuditLog(userName = "")
        every { logValidator.validateAuditLogFieldsNotBlank(invalidAuditLog) } throws InvalidAuditLogFieldsException()

        //when
        assertFailsWith<InvalidAuditLogFieldsException> {
            createAuditLogUseCase.execute(invalidAuditLog)
        }
        //then
        verify {  logValidator.validateAuditLogFieldsNotBlank(invalidAuditLog) }
        coVerify(exactly = 0) { auditRepository.createAuditLog(any()) }
    }

    @Test
    fun `should throw exception when repository fails`() = runTest {
        //given
        val auditLog = createTestAuditLog(
            entityType = EntityType.PROJECT,
            description = "Project state changed",
            userName = "user2",
            createdAt = LocalDateTime.of(2025, 4, 28, 10, 0)
        )
        //when
         every { logValidator.validateAuditLogFieldsNotBlank(auditLog)} returns true
        coEvery { auditRepository.createAuditLog(auditLog) } throws RuntimeException("Failed to create audit log")

        //then
        val exception = assertFailsWith<RuntimeException> {
            createAuditLogUseCase.execute(auditLog)
        }

        assertThat(exception.message).isEqualTo("Failed to create audit log")
        verify {logValidator.validateAuditLogFieldsNotBlank(auditLog) }
        coVerify(exactly = 1) { auditRepository.createAuditLog(auditLog) }
    }
}
