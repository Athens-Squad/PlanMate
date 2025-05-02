package ui.featuresui

import logic.entities.ProgressionState
import logic.use_cases.state.StatesUseCases
import net.thechance.ui.options.project.EditProjectOptions
import net.thechance.ui.options.states.EditStateOptions
import net.thechance.ui.options.states.StateOptions
import ui.io.ConsoleIO

class StatesUi(
    private val consoleIO: ConsoleIO,
    private val statesUseCases: StatesUseCases
) {
    fun manageStates(states: List<ProgressionState>, projectId: String) {
        do {
            consoleIO.printer.printTitle("Select Option (1 to 4):")
            consoleIO.printer.printOptions(StateOptions.entries)
            val inputStateOption = consoleIO.reader.readNumberFromUser()

            when (inputStateOption) {
                StateOptions.CREATE.optionNumber -> createState(projectId)
                StateOptions.EDIT.optionNumber -> editState(states)

                StateOptions.DELETE.optionNumber -> {
                    deleteState(states)
                        .onSuccess {
                            consoleIO.printer.printCorrectOutput("State Deleted Successfully")
                        }
                        .onFailure {
                            consoleIO.printer.printError(it.message.toString())
                        }
                }
            }
        } while (inputStateOption != StateOptions.BACK.optionNumber &&
            inputStateOption != StateOptions.DELETE.optionNumber
        )
    }

    private fun createState(projectId: String): Result<Unit> {
        consoleIO.printer.printTitle("Create State.")

        val stateName = receiveStringInput("Enter State Name : ")

        return statesUseCases.createStateUseCase.execute(
            ProgressionState(
                name = stateName,
                projectId = projectId
            )
        )
    }

    private fun editState(states: List<ProgressionState>): Result<Unit> {
        consoleIO.printer.printTitle("Edit State")

        consoleIO.printer.printOption(
            states.map {
                it.name
            }.toString()
        )

        val inputState = consoleIO.reader.readStringFromUser()

        consoleIO.printer.printTitle("Select your option (1) : ")

        consoleIO.printer.printOptions(EditStateOptions.entries)
        val inputEditOption = consoleIO.reader.readNumberFromUser()

        val currentState = getState(inputState, states).getOrThrow()

        val stateName = receiveStringInput("Enter New State Name : ")

        return when (inputEditOption) {
            EditStateOptions.NAME.optionNumber -> {
                statesUseCases.updateStateUseCase.execute(
                    state = currentState,
                    updatedState = currentState.copy(
                        name = stateName
                    )
                )
            }

            else -> Result.failure(Exception("Invalid Input!"))
        }
    }

    private fun deleteState(states: List<ProgressionState>): Result<Unit> {
        consoleIO.printer.printTitle("Delete State")

        consoleIO.printer.printOption(
            states.map {
                it.name
            }.toString()
        )

        val inputState = consoleIO.reader.readStringFromUser()


        getStateId(inputState, states)
            .onSuccess { stateId ->
                return statesUseCases
                    .deleteStateUseCase
                    .execute(stateId = stateId)
            }
        return Result.failure(NoSuchElementException())
    }

    fun getStates(projectId: String): Result<List<ProgressionState>> {
        return statesUseCases.getStatesByProjectIdUseCase.execute(projectId)
    }


    private fun receiveStringInput(message: String): String {
        consoleIO.printer.printOption(message)
        return consoleIO.reader.readStringFromUser()
    }

    private fun getStateId(inputStateName: String, states: List<ProgressionState>): Result<String> {
        return runCatching {
            states.first { it.name == inputStateName }.id
        }
    }

    private fun getState(inputStateName: String, states: List<ProgressionState>): Result<ProgressionState> {
        return runCatching {
            states.first { it.name == inputStateName }
        }
    }

}