package net.thechance.ui.options.progression_states

import net.thechance.ui.options.Option

enum class EditProgressionStateOptions(
    override val optionNumber: Int,
    override val optionTitle: String
) : Option {
    NAME(
        1,
        "Edit State Name"
    )
}