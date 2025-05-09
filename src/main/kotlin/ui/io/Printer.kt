package ui.io

import net.thechance.ui.options.AuthenticationOptions
import net.thechance.ui.options.Option
import net.thechance.ui.utils.TextStyle
import ui.utils.Colors

class Printer(
    private val colors: Colors
) {

    fun printText(
        text: String,
        textStyle : TextStyle = TextStyle.NORMAL,
        withNewLine: Boolean = true
    ) {
        val coloredText = textStyle.format(text)
        if (withNewLine) {
            println(coloredText)
        } else {
            print(coloredText)
        }
    }


    fun printOptions(
        options: List<Option>
    ) {
        options.forEach { option ->
            printText("${option.optionNumber} : ${option.optionTitle}", TextStyle.OPTION)
        }
    }

    fun printDivider() {
        println("-".repeat(25))
    }


}