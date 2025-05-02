package net.thechance.ui.options.states

import net.thechance.ui.options.Option

enum class EditStateOptions(
    override val optionNumber: Int,
    override val optionTitle: String
) : Option {
    NAME(
        1,
        "Edit State Name"
    )
}