@file:OptIn(ExperimentalUuidApi::class)

package net.thechance.ui.featuresui

import logic.entities.ProgressionState
import logic.use_cases.progression_state.ProgressionStatesUseCases
import net.thechance.ui.core.io.ConsoleIO
import net.thechance.ui.core.io.TextStyle
import net.thechance.ui.options.progression_states.EditProgressionStateOptions
import net.thechance.ui.options.progression_states.ProgressionStateOptions
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ProgressionStateUi(
    private val consoleIO: ConsoleIO,
    private val progressionStatesUseCases: ProgressionStatesUseCases
) {
    suspend fun manageStates(projectId: Uuid) {
        try {
            do {
                val progressionStates = progressionStatesUseCases
                    .getProgressionStatesByProjectIdUseCase
                    .execute(projectId)

                consoleIO.printer.printText("Select Option (1 to 4):", TextStyle.TITLE)
                consoleIO.printer.printOptions(ProgressionStateOptions.entries)
                val inputStateOption = consoleIO.reader.readNumberFromUser()

                when (inputStateOption) {
                    ProgressionStateOptions.CREATE.optionNumber -> createProgressionState(projectId)

                    ProgressionStateOptions.EDIT.optionNumber -> editProgressionState(progressionStates)

                    ProgressionStateOptions.DELETE.optionNumber -> deleteProgressionState(progressionStates)
                }
            } while (inputStateOption != ProgressionStateOptions.BACK.optionNumber &&
                inputStateOption != ProgressionStateOptions.DELETE.optionNumber
            )
        } catch (exception: Exception) {
            consoleIO.printer.printText(exception.message.toString(), TextStyle.ERROR)
        }
    }

    private suspend fun createProgressionState(projectId: Uuid) {
        consoleIO.printer.printText("Create State.", TextStyle.TITLE)
        val stateName = receiveStringInput("Enter State Name : ")

        progressionStatesUseCases.createProgressionStateUseCase.execute(
            ProgressionState(
                name = stateName,
                projectId = projectId
            )
        )
        consoleIO.printer.printText("created successful", TextStyle.SUCCESS)
    }

    private suspend fun editProgressionState(progressionStates: List<ProgressionState>) {
        consoleIO.printer.printText("Edit State", TextStyle.TITLE)

        printProgressionStates(progressionStates)
        consoleIO.printer.printText("Select Progression State : ", TextStyle.TITLE)
        val currentProgressionState = getProgressionState(progressionStates)

        consoleIO.printer.printText("Select your option (1) : ", TextStyle.TITLE)
        consoleIO.printer.printOptions(EditProgressionStateOptions.entries)
        val inputEditOption = consoleIO.reader.readNumberFromUser()

        val progressionStateName = receiveStringInput("Enter New State Name : ")

        when (inputEditOption) {
            EditProgressionStateOptions.NAME.optionNumber -> {
                progressionStatesUseCases.updateProgressionStateUseCase.execute(
                    updatedProgressionState = currentProgressionState.copy(
                        name = progressionStateName
                    )
                )
                consoleIO.printer.printText("Progression State Updated Successfully", TextStyle.SUCCESS)
            }

            else -> throw Exception("Invalid Input!")
        }
    }



    private suspend fun deleteProgressionState(progressionStates: List<ProgressionState>) {
        consoleIO.printer.printText("Delete State", TextStyle.TITLE)

        printProgressionStates(progressionStates)
        consoleIO.printer.printText("Select Progression State To Delete: ", TextStyle.TITLE)
        val currentState = getProgressionState(progressionStates)

        progressionStatesUseCases.deleteProgressionStateUseCase.execute(currentState.id)
        consoleIO.printer.printText("State Deleted Successfully", TextStyle.SUCCESS)
    }

    private fun receiveStringInput(message: String): String {
        consoleIO.printer.printText(message, TextStyle.OPTION)
        return consoleIO.reader.readStringFromUser()
    }

    private fun printProgressionStates(progressionStates: List<ProgressionState>) {
        consoleIO.printer.printText(progressionStates.joinToString { it.name + ", " }, TextStyle.OPTION)
    }

    private fun getProgressionState(
        progressionStates: List<ProgressionState>
    ): ProgressionState {
        val inputStateName = consoleIO.reader.readStringFromUser()
        return progressionStates.first { it.name == inputStateName }
    }
}