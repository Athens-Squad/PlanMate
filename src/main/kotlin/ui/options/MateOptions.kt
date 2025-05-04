package net.thechance.ui.options

enum class MateOptions(
    override val optionNumber: Int,
    override val optionTitle: String
): Option {
    SHOW_ALL_PROJECTS(
        1,
        "Show All Projects."
    ),
    EXIT(
        2,
        "Exit."
    )
}