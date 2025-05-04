package net.thechance.ui.options.tasks

import net.thechance.ui.options.Option

enum class EditTaskOptions (
    override val optionNumber: Int,
    override val optionTitle: String
): Option {
    NAME(
        1,
        "Edit Task Name"
    ),
    DESCRIPTION(
        2,
        "Edit Task Description"
    ),
    PROGRESSION_STATE(
        3,
        "Change Task State"
    )
}