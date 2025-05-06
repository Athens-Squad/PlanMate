package data.aduit_log_csvfile.repository

import com.google.common.truth.Truth.assertThat
import data.aduit_log_csvfile.dummyAuditLog
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.entities.AuditLog
import net.thechance.data.aduit_log.data_source.AuditLogDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import logic.entities.EntityType
import net.thechance.data.aduit_log.repository.AuditLogRepositoryImpl
import org.hamcrest.core.Every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuditLogRepositoryImplTest {

    private val dataSource = mockk<AuditLogDataSource>()
    private lateinit var repository: AuditLogRepositoryImpl

    @BeforeEach
    fun setUp() {
        repository = AuditLogRepositoryImpl(dataSource)
    }

    @Test
    fun `createAuditLog delegates to data source`() = runTest {
        // Given
        val auditLog = dummyAuditLog()
        coEvery { dataSource.createAuditLog(auditLog) } returns Unit

        // When
        val result = repository.createAuditLog(auditLog)

        // Then
        coVerify(exactly = 1) { dataSource.createAuditLog(auditLog) }
    }

    @Test
    fun `getAuditLogs returns data from data source`() = runTest {
        // Given
        val sampleAuditLog = dummyAuditLog()
        coEvery { dataSource.getAuditLogs() } returns listOf(sampleAuditLog)

        // When
        val result = repository.getAuditLogs()

        // Then
        assertThat(result).containsExactly(sampleAuditLog)
        coVerify(exactly = 1) { dataSource.getAuditLogs() }
    }

    @Test
    fun `clearLog delegates call to data source`() = runTest {
        // Given
        coEvery { dataSource.clearLog() } returns Unit

        // When
        val result = repository.clearLog()

        // Then
        coVerify(exactly = 1) { dataSource.clearLog() }
    }
}
