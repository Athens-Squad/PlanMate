@file:OptIn(ExperimentalUuidApi::class)

package data.auditlog.data_source

import com.google.common.truth.Truth.assertThat
import data.aduit_log.data_source.localCsvFile.AuditLogFileDataSource
import data.aduit_log.data_source.localCsvFile.dto.AuditLogCsvDto
import data.aduit_log.data_source.localCsvFile.mapper.toAuditLog
import data.aduit_log.data_source.localCsvFile.mapper.toAuditLogCsvDto
import data.auditlog.dummyAuditLog
import data.utils.csv_file_handle.CsvFileHandler
import data.utils.csv_file_handle.CsvFileParser
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.uuid.ExperimentalUuidApi

class AuditLogFileDataSourceTest {

    private lateinit var dataSource: AuditLogFileDataSource
    private val fileHandler = mockk<CsvFileHandler>()
    private val parser = mockk<CsvFileParser<AuditLogCsvDto>>()

    @BeforeEach
    fun setUp() {
        dataSource = AuditLogFileDataSource(fileHandler, parser)
    }

    @Test
    fun `createAuditLog converts and appends record`() = runTest {
        // given
        val log = dummyAuditLog()
        val dto = log.toAuditLogCsvDto()

        every { parser.toCsvRecord(dto) } returns "csv_string"
        every { fileHandler.appendRecord("csv_string") } just Runs

        // when
        dataSource.createAuditLog(log)

        // then
        verify {
             parser.toCsvRecord(dto)
            fileHandler.appendRecord("csv_string") }
    }



    @Test
    fun `getAuditLogs reads and parses records`() = runTest {
        // given
        val csvLines = listOf("csv_1", "csv_2")
        val dto1 = dummyAuditLog().copy(description = "first").toAuditLogCsvDto()
        val dto2 = dummyAuditLog().copy(description = "second").toAuditLogCsvDto()


        every { fileHandler.readRecords() } returns csvLines
        every { parser.parseRecord("csv_1") } returns dto1
        every { parser.parseRecord("csv_2") } returns dto2


        // when
        val result = dataSource.getAuditLogs()

        // then
        assertThat(result).containsExactlyElementsIn(listOf(dto1.toAuditLog(), dto2.toAuditLog()))

        verify {
            fileHandler.readRecords()
            parser.parseRecord("csv_1")
            parser.parseRecord("csv_2")
         }

    }

    @Test
    fun `clearLog writes an empty list to the file`() = runTest {
        // Given
        val fileHandler = mockk<CsvFileHandler>(relaxed = true)
        val parser = mockk<CsvFileParser<AuditLogCsvDto>>()

        val dataSource = AuditLogFileDataSource(fileHandler, parser)

        // When
        dataSource.clearLog()

        // Then
        verify { fileHandler.writeRecords(emptyList()) }
    }
}
