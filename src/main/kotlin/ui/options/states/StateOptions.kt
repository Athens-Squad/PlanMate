package net.thechance.ui.options.states

import net.thechance.ui.options.Option

enum class StateOptions(
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