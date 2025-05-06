package helper.progression_state_helper

import net.thechance.data.progression_state.dto.ProgressionStateDto
import java.util.UUID

object FakeProgressionStateData {

	val fakeProgressionState1: ProgressionStateDto = ProgressionStateDto(
		id = "1",
		name = "state1",
		projectId = "pr1"
	)
	val fakeProgressionState2: ProgressionStateDto = ProgressionStateDto(
		id = "2",
		name = "state2",
		projectId = "pr2"
	)
	val fakeProgressionState3: ProgressionStateDto = ProgressionStateDto(
		id = "3",
		name = "state3",
		projectId = "pr3"
	)
	val fakeProgressionStates = listOf(fakeProgressionState1, fakeProgressionState2, fakeProgressionState3)
}