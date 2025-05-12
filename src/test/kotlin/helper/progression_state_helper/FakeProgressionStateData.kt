/*package helper.progression_state_helper

import net.thechance.data.progression_state.data_source.remote.mongo.dto.ProgressionStateDto
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
object FakeProgressionStateData {

	val fakeProgressionState1: ProgressionStateDto = ProgressionStateDto(
		id = Uuid.random(),
		name = "state1",
		projectId = Uuid.random()
	)
	private val fakeProgressionState2: ProgressionStateDto = ProgressionStateDto(
		id = Uuid.random(),
		name = "state2",
		projectId = Uuid.random()
	)
	private val fakeProgressionState3: ProgressionStateDto = ProgressionStateDto(
		id = Uuid.random(),
		name = "state3",
		projectId = Uuid.random()
	)
	val fakeProgressionStates = listOf(fakeProgressionState1, fakeProgressionState2, fakeProgressionState3)
}

 */