package net.thechance.ui.options.progression_states

import net.thechance.ui.options.Option

enum class ProgressionStateOptions(
    override val optionNumber: Int,
    override val optionTitle: String
) : Option {
    CREATE(
        1,
        "Create New State"
    ),
    EDIT(
        2,
        "Edit State"
    ),
    DELETE(
        3,
        "Delete State"
    ),
    BACK(
        4,
        "Back."
    )
}