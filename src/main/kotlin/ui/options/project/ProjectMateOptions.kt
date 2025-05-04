package net.thechance.ui.options.project

import net.thechance.ui.options.Option

enum class ProjectMateOptions(
    override val optionNumber: Int,
    override val optionTitle: String
): Option {
    CREATE_TASK(
        1,
        "Create New Task"
    ),
    MANAGE_TASKS(
        2,
        "Manage Project Tasks."
    ),
    SHOW_HISTORY(
        3,
        "Show Project History."
    ),
    BACK(
        4,
        "Back."
    )
}