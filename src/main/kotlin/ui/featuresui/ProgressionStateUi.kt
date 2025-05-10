@file:OptIn(ExperimentalUuidApi::class)

package ui.featuresui

import logic.entities.ProgressionState
import logic.use_cases.progression_state.ProgressionStatesUseCases
import net.thechance.ui.options.progression_states.EditProgressionStateOptions
import net.thechance.ui.options.progression_states.ProgressionStateOptions
import net.thechance.ui.utils.TextStyle
import ui.io.ConsoleIO
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ProgressionStateUi(
    private val consoleIO: ConsoleIO,
    private val progressionStatesUseCases: ProgressionStatesUseCases
) {
    suspend fun manageStates(projectId: Uuid) {
        do {
            val progressionStates = progressionStatesUseCases
                .getProgressionStatesByProjectIdUseCase
                .execute(projectId)

            consoleIO.printer.printText("Select Option (1 to 4):",TextStyle.TITLE)
            consoleIO.printer.printOptions(ProgressionStateOptions.entries)
            val inputStateOption = consoleIO.reader.readNumberFromUser()

            when (inputStateOption) {
                ProgressionStateOptions.CREATE.optionNumber -> {
                    createProgressionState(projectId).also {
                        consoleIO.printer.printText("created successful",TextStyle.SUCCESS)
                        return
                    }
                }
                ProgressionStateOptions.EDIT.optionNumber -> editProgressionState(progressionStates)

                ProgressionStateOptions.DELETE.optionNumber -> {
                    deleteProgressionState(progressionStates)
                        .also {
                            consoleIO.printer.printText("State Deleted Successfully",TextStyle.SUCCESS)
                        }
                }
            }
        } while (inputStateOption != ProgressionStateOptions.BACK.optionNumber &&
            inputStateOption != ProgressionStateOptions.DELETE.optionNumber
        )
    }

    private suspend fun createProgressionState(projectId: Uuid) {
        consoleIO.printer.printText("Create State.",TextStyle.TITLE)

        val stateName = receiveStringInput("Enter State Name : ")

        return progressionStatesUseCases.createProgressionStateUseCase.execute(
            ProgressionState(
                name = stateName,
                projectId = projectId
            )
        )
    }

    private suspend fun editProgressionState(progressionStates: List<ProgressionState>) {
        consoleIO.printer.printText("Edit State",TextStyle.TITLE)

        consoleIO.printer.printText(
            progressionStates.map {
                it.name
            }.toString(),
            TextStyle.OPTION
        )

        val inputProgressionState = consoleIO.reader.readStringFromUser()

        consoleIO.printer.printText("Select your option (1) : ",TextStyle.TITLE)

        consoleIO.printer.printOptions(EditProgressionStateOptions.entries)
        val inputEditOption = consoleIO.reader.readNumberFromUser()

        val currentProgressionState = getProgressionState(inputProgressionState, progressionStates)
        val progressionStateName = receiveStringInput("Enter New State Name : ")

        when (inputEditOption) {
            EditProgressionStateOptions.NAME.optionNumber -> {
	            progressionStatesUseCases.updateProgressionStateUseCase.execute(
                    updatedProgressionState = currentProgressionState.copy(
						name = progressionStateName
					)
                )
            }

            else -> throw Exception("Invalid Input!")
        }
    }

    private suspend fun deleteProgressionState(progressionStates: List<ProgressionState>) {
        consoleIO.printer.printText("Delete State",TextStyle.TITLE)

        consoleIO.printer.printText(
            progressionStates.map {
                it.name
            }.toString(),
            TextStyle.OPTION
        )

        val inputState = consoleIO.reader.readStringFromUser()


        getProgressionStateId(inputState, progressionStates)
	        .also { progressionStateId ->
		        progressionStatesUseCases.deleteProgressionStateUseCase.execute(progressionStateId)
	        }
    }

    suspend fun getProgressionStatesByProjectId(projectId: Uuid):List<ProgressionState> {
        return progressionStatesUseCases.getProgressionStatesByProjectIdUseCase.execute(projectId)
    }


    private fun receiveStringInput(message: String): String {
        consoleIO.printer.printText(message,TextStyle.OPTION)
        return consoleIO.reader.readStringFromUser()
    }

    private fun getProgressionStateId(inputStateName: String, progressionStates: List<ProgressionState>): Uuid {
        return progressionStates.first { it.name == inputStateName }.id
    }

    private fun getProgressionState(inputStateName: String, progressionStates: List<ProgressionState>): ProgressionState  {
        return  progressionStates.first { it.name == inputStateName }
    }
}