package helper.progression_state_helper

import net.thechance.data.progression_state.dto.ProgressionStateDto
import java.util.UUID

object FakeProgressionStateData {

	val fakeProgressionState1: ProgressionStateDto = ProgressionStateDto(
		id = UUID.fromString("1"),
		name = "state1",
		projectId = UUID.fromString("pr1")
	)
	val fakeProgressionState2: ProgressionStateDto = ProgressionStateDto(
		id = UUID.fromString("2"),
		name = "state2",
		projectId = UUID.fromString("pr2")
	)
	val fakeProgressionState3: ProgressionStateDto = ProgressionStateDto(
		id = UUID.fromString("3"),
		name = "state3",
		projectId = UUID.fromString("pr3")
	)
	val fakeProgressionStates = listOf(fakeProgressionState1, fakeProgressionState2, fakeProgressionState3)
}