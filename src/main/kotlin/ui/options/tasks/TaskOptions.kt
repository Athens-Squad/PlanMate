package net.thechance.ui.options.tasks

import net.thechance.ui.options.Option

enum class TaskOptions(
    override val optionNumber: Int,
    override val optionTitle: String
) : Option {
    EDIT(
        1,
        "Edit Task."
    ),
    DELETE(
        2,
        "Delete This Task."
    ),
    SHOW_HISTORY(
        3,
        "Show Task History."
    ),
    BACK(
        4,
        "Back."
    )
}