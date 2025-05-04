package net.thechance.ui.options.project

import net.thechance.ui.options.Option

enum class ProjectOptions(
    override val optionNumber: Int,
    override val optionTitle: String
): Option {
    CREATE_TASK(
        1,
        "Create New Task"
    ),
    EDIT(
        2,
        "Edit Project."
    ),
    MANAGE_STATES(
        3,
        "Manage Project States."
    ),
    MANAGE_TASKS(
        4,
        "Manage Project Tasks."
    ),
    SHOW_HISTORY(
        5,
        "Show Project History."
    ),
    DELETE(
        6,
        "Delete This Project."
    ),
    BACK(
        7,
        "Back."
    )
}


