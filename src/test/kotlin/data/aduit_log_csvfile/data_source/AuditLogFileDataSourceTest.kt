package data.aduit_log_csvfile.data_source

import com.google.common.truth.Truth.assertThat
import data.aduit_log_csvfile.dummyAuditLog
import io.mockk.*
import logic.entities.AuditLog
import net.thechance.data.aduit_log_csvfile.data_source.AuditLogDataSource
import net.thechance.data.aduit_log_csvfile.data_source.AuditLogFileDataSource
import net.thechance.data.csv_file_handle.CsvFileHandler
import net.thechance.data.csv_file_handle.CsvFileParser
import org.junit.jupiter.api.Assertions.*
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
    fun `createAuditLog converts and appends record`() {
        //given
        val log = dummyAuditLog()
        every { parser.toCsvRecord(log) } returns "csv_string"
        every { fileHandler.appendRecord("csv_string") } just Runs

        //when
        val result = dataSource.createAuditLog(log)
        //then
        assertThat(result.isSuccess).isTrue()
        verify { fileHandler.appendRecord("csv_string") }


    }


    @Test
    fun `getAuditLogs reads and parses records`() {
        //given
        val record = "some,csv,line"
        val log = dummyAuditLog()

        every { fileHandler.readRecords() } returns listOf(record)
        every { parser.parseRecord(record) } returns log


        //when
        val result = dataSource.getAuditLogs()
        //then
        assertThat(result.getOrThrow()).containsExactly(log)

    }


}