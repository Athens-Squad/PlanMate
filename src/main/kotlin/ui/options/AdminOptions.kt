package net.thechance.ui.options

enum class AdminOptions(
    override val optionNumber: Int,
    override val optionTitle: String
): Option {
    SHOW_ALL_PROJECTS(
        1,
        "Show All Projects."
    ),
    CREATE_PROJECT(
        2,
        "Create New Project."
    ),
    CREATE_MATE(
        3,
        "Create New Mate."
    ),
    EXIT(
        4,
        "Exit."
    )
}