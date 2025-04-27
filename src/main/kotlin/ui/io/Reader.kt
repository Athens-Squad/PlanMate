package com.thechance.presentation.io

import com.thechance.data.MealsDataException

class Reader {
    fun readStringFromUser(): String {
        return readlnOrNull().toString()
    }


    fun readNumberFromUser(): Int {
        val input = readlnOrNull()
        return input?.toIntOrNull() ?: throw MealsDataException.InvalidNumericFormatException("$input")
    }
}