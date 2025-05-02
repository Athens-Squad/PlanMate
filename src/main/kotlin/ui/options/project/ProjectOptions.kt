package net.thechance.ui.options.project

import net.thechance.ui.options.Option

enum class ProjectOptions(
    override val optionNumber: Int,
    override val optionTitle: String
): Option {
    EDIT(
        1,
        "Edit Project."
    ),
    MANAGE_STATES(
        2,
        "Manage Project States."
    ),
    MANAGE_TASKS(
        3,
        "Manage Project Tasks."
    ),
    SHOW_HISTORY(
        4,
        "Show Project History."
    ),
    DELETE(
        5,
        "Delete This Project."
    ),
    BACK(
        6,
        "Back."
    )
}