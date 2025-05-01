package data.aduit_log_csvfile.repository

import com.google.common.truth.Truth.assertThat
import data.aduit_log_csvfile.dummyAuditLog
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.AuditLog
import data.aduit_log_csvfile.data_source.AuditLogDataSource
import data.aduit_log_csvfile.repository.AuditLogRepositoryImpl
import logic.entities.EntityType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class AuditLogRepositoryImplTest{

  private val dataSource = mockk<AuditLogDataSource>()
  private lateinit var repository: AuditLogRepositoryImpl





  @BeforeEach
  fun setUp() {
   repository = AuditLogRepositoryImpl(dataSource)


  }

 @Test
 fun `createAuditLog delegates to data source`(){
     //given
    val auditLog=dummyAuditLog()
    every { dataSource.createAuditLog(auditLog) } returns Result.success(Unit)

     //when
     val result=repository.createAuditLog(auditLog)

      //then
        assertThat(result.isSuccess).isTrue()
        verify(exactly = 1) { dataSource.createAuditLog(auditLog) }


 }
    @Test
    fun `getAuditLogs returns data from data source`() {
        //given
        val sampleAuditLog= dummyAuditLog()
        every { dataSource.getAuditLogs() } returns Result.success(listOf(sampleAuditLog))

        //when
        val result = repository.getAuditLogs()

        //then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrThrow()).containsExactly(sampleAuditLog)
        verify(exactly = 1) { dataSource.getAuditLogs() }
    }

    @Test
    fun `clearLog delegates call to data source`() {
        every { dataSource.clearLog() } returns Result.success(Unit)

        val result = repository.clearLog()

        assertThat(result.isSuccess).isTrue()
        verify(exactly = 1) { dataSource.clearLog() }
    }






}