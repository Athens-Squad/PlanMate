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
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import net.thechance.data.aduit_log.data_source.remote.mongo.MongoAuditLogDataSource
import net.thechance.data.aduit_log.data_source.remote.mongo.dto.AuditLogDto
import net.thechance.data.aduit_log.data_source.remote.mongo.mapper.toAuditLogDto
import org.bson.Document
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.uuid.ExperimentalUuidApi

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
        val auditLog = createTestAuditLog(description = "Audit log for retrieval")
        val dto = auditLog.toAuditLogDto()
        val findFlowMock = mockk<FindFlow<AuditLogDto>>(relaxed = true)

        coEvery { auditLogMongoCollection.find() } returns findFlowMock
        coEvery { findFlowMock.toList() } returns listOf(dto)

        // when
        val result = mongoAuditLogDataSource.getAuditLogs()

        // then
        assertThat(result).hasSize(1)
        assertThat(result[0]).isEqualTo(auditLog)

        coVerify { auditLogMongoCollection.find() }
        coVerify { findFlowMock.toList() }
    }

    @Test
    fun `clearLog  delete all documents in collection`() = runTest {
        //given
        val deleteResultMock = mockk<DeleteResult>()
        coEvery { auditLogMongoCollection.deleteMany(any<Document>()) } returns deleteResultMock

        // when
        mongoAuditLogDataSource.clearLog()

        // then
        coVerify { auditLogMongoCollection.deleteMany(any<Document>()) }

    }

}