package ui.featuresui

import logic.entities.ProgressionState
import logic.use_cases.state.StatesUseCases
import ui.io.ConsoleIO

class StatesUi(
    private val consoleIO: ConsoleIO,
    private val statesUseCases: StatesUseCases
) {
    fun manageStates(states: List<ProgressionState>) {

    }

    fun createState(state: ProgressionState): Result<Unit> {

    }

    fun editState(editedState: ProgressionState): Result<Unit> {

    }

    fun deleteState(state: ProgressionState): Result<Unit> {

    }

    fun getStates(projectName: String): Result<List<ProgressionState>> {

    }
}