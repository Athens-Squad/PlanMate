package ui.io

class Reader {
    fun readStringFromUser(): String {
        return readlnOrNull().toString()
    }

    fun readNumberFromUser(): Int {
        val input = readlnOrNull()
        return input?.toIntOrNull() ?: throw Exception("$input")
    }
}