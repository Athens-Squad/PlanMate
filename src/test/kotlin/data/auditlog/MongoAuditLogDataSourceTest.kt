@file:OptIn(ExperimentalUuidApi::class)

package data.auditlog

import com.google.common.truth.Truth.assertThat
import com.mongodb.client.result.DeleteResult
import com.mongodb.kotlin.client.coroutine.FindFlow
import com.mongodb.kotlin.client.coroutine.MongoCollection
import helper.auditlog.createTestAuditLog
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList

import kotlinx.coroutines.test.runTest
import logic.entities.EntityType
import net.thechance.data.aduit_log.data_source.remote.mongo.MongoAuditLogDataSource
import net.thechance.data.aduit_log.data_source.remote.mongo.dto.AuditLogDto
import net.thechance.data.aduit_log.data_source.remote.mongo.mapper.toAuditLog
import net.thechance.data.aduit_log.data_source.remote.mongo.mapper.toAuditLogDto
import org.bson.Document
import org.bson.conversions.Bson
import org.junit.jupiter.api.BeforeEach
import java.time.LocalDateTime
import java.util.*
import kotlin.test.Test
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class MongoAuditLogDataSourceTest {
    private lateinit var auditLogMongoCollection: MongoCollection<AuditLogDto>
    private lateinit var mongoAuditLogDataSource: MongoAuditLogDataSource

    @BeforeEach
    fun setUp() {
        auditLogMongoCollection = mockk(relaxed = true)
        mongoAuditLogDataSource = MongoAuditLogDataSource(auditLogMongoCollection)
    }


    @Test
    fun `createAuditLog insert audit log into collection`() = runTest {

       val auditLog =  createTestAuditLog(description = "Test insert audit log")
        coEvery { auditLogMongoCollection.insertOne(any()) } returns mockk()
        mongoAuditLogDataSource.createAuditLog(auditLog)


        coEvery {
            auditLogMongoCollection.insertOne(
                match {
                    it.description == "Test insert audit log" &&
                            it.entityType == auditLog.entityType.name &&
                            it.userName == auditLog.userName &&
                            it.entityId == auditLog.entityId
                }
            )
        }
    }


    @Test
    fun `getAuditLogs  return audit logs from collection`()= runTest {

        // given
        val auditLog = dummyAuditLog()
        val dto = auditLog.toAuditLogDto()


        val findFlowMock = mockk<FindFlow<AuditLogDto>>()

        coEvery { findFlowMock.toList() } returns listOf(dto)
        coEvery { auditLogMongoCollection.find().collect(any()) } coAnswers {
           val collect=arg<FlowCollector<AuditLogDto>>(0)
            collect.emit(dto)
        }


        // when
        val result = mongoAuditLogDataSource.getAuditLogs()
         println("result $result")


        // then
        assertThat(result).hasSize(1)
        assertThat(result[0]).isEqualTo(auditLog)

    }

    @Test
    fun `clearLog should delete all documents from MongoDB`() = runTest {

        coEvery { auditLogMongoCollection.deleteMany(any()) } answers {
            val filterArg = firstArg<Bson>()
            assertEquals(Document(), filterArg)
            mockk()
        }

        mongoAuditLogDataSource.clearLog()

    }

}