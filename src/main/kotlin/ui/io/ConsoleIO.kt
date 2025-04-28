package com.thechance.presentation.io

import net.thechance.ui.io.Printer
import net.thechance.ui.io.Reader

data class ConsoleIO (
    val printer: Printer,
    val reader: Reader
)