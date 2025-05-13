package data.progression_state.data_source.remote.mongo

import com.google.common.truth.Truth.assertThat
import com.mongodb.kotlin.client.coroutine.MongoCollection
import helper.progression_state_helper.FakeProgressionStateData.fakeProgressionState1
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import net.thechance.data.progression_state.data_source.remote.mongo.dto.ProgressionStateDto
import data.progression_state.data_source.remote.mongo.mapper.toProgressionState
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProgressionStateDatabaseDataSourceTest {

	private lateinit var mockkProgressionStatesDocument: MongoCollection<ProgressionStateDto>
	private lateinit var progressionStateDatabaseDataSource: ProgressionStateDatabaseDataSource

	@BeforeEach
	fun setUp() {
		mockkProgressionStatesDocument = mockk(relaxed = true)
		progressionStateDatabaseDataSource = ProgressionStateDatabaseDataSource(
			progressionStatesCollection = mockkProgressionStatesDocument
		)
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun `should create ProgressionState successfully, when called`() = runTest {
		coEvery { mockkProgressionStatesDocument.find().toList() } returns listOf(
			fakeProgressionState1
		)

		progressionStateDatabaseDataSource.createProgressionState(fakeProgressionState1.toProgressionState())
		val expectedResult = progressionStateDatabaseDataSource.getProgressionStates()

		assertThat(expectedResult).containsExactly(fakeProgressionState1)
		coVerify(exactly = 1) {
			mockkProgressionStatesDocument.insertOne(fakeProgressionState1)
		}
	}

}