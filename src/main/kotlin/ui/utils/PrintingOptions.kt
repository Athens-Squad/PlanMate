package net.thechance.ui.utils

enum class TextStyle(private val color: String) {
    TITLE("\u001B[36m"),       // Cyan
    ERROR("\u001B[31m"),       // Red
    SUCCESS("\u001B[32m"),     // Green
    INFO("\u001B[33m"),        // Yellow
    OPTION("\u001B[35m"),      // Purple
    LOADER("\u001B[34m"),      // Blue
    EXIT("\u001B[31m"),        // Red
    WELCOME("\u001B[36m"),     // Cyan
    NORMAL("\u001B[37m"),      // White
    GOODBYE("\u001B[33m") ;    // Yellow

    companion object {
        const val RESET = "\u001B[0m"
    }

    fun format(text: String): String =  "$color$text$RESET"
 }