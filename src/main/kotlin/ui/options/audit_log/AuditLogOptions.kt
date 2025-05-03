package net.thechance.ui.options.audit_log

import net.thechance.ui.options.Option

enum class AuditLogOptions(
    override val optionNumber: Int,
    override val optionTitle: String
): Option {
    CLEAR_LOG (
        1,
        "Clear History"
    ),
    BACK (
    2,
    "Back"
    )
}