package data.aduit_log_csvfile.data_source

import com.google.common.truth.Truth.assertThat
import data.aduit_log_csvfile.dummyAuditLog
import data.utils.csv_file_handle.CsvFileHandler
import data.utils.csv_file_handle.CsvFileParser
import io.mockk.*
import kotlinx.coroutines.test.runTest
import logic.entities.AuditLog
import net.thechance.data.aduit_log.data_source.AuditLogDataSource
import net.thechance.data.aduit_log.data_source.local.dto.AuditLogFileDataSource
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class AuditLogFileDataSourceTest {

    private lateinit var dataSource: AuditLogDataSource
    private val fileHandler = mockk<CsvFileHandler>()
    private val parser = mockk<CsvFileParser<AuditLog>>()

    @BeforeEach
    fun setUp() {
        dataSource = AuditLogFileDataSource(fileHandler, parser)
    }

    @Test
    fun `createAuditLog converts and appends record`() = runTest {
        // given
        val log = dummyAuditLog()
        every { parser.toCsvRecord(log) } returns "csv_string"
        every { fileHandler.appendRecord("csv_string") } just Runs

        // when
        dataSource.createAuditLog(log)

        // then
        verify { fileHandler.appendRecord("csv_string") }
    }

    @Test
    fun `getAuditLogs reads and parses records`() = runTest {
        // given
        val record = "some,csv,line"
        val log = dummyAuditLog()

        every { fileHandler.readRecords() } returns listOf(record)
        every { parser.parseRecord(record) } returns log

        // when
        val result = dataSource.getAuditLogs()

        // then
        assertThat(result).containsExactly(log)
    }
}
