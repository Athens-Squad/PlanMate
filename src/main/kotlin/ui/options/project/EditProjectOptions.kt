package net.thechance.ui.options.project

import net.thechance.ui.options.Option

enum class EditProjectOptions(
    override val optionNumber: Int,
    override val optionTitle: String
): Option {
    NAME(
        1,
        "Edit Project Name"
    ),
    DESCRIPTION(
        2,
        "Edit Project Description"
    )
}