package util

import java.sql.Date


fun String.isNumeric(): Boolean = this.matches(Regex("\\d+"))

fun String.isDate(): Boolean {
    val date: Date?
    try {
        date = Date.valueOf(this)
    } catch (e: IllegalArgumentException) {
        return false
    }
    return date != null
}

fun String.toDate(): Date = Date.valueOf(this)

fun String.isIntBool(): Boolean {
    val value: Int
    try {
        value = this.toInt()
    } catch (e: NumberFormatException) {
        return false
    }
    return (value == 0 || value == 1)
}

fun Int.toBoolean(): Boolean {
    if (this == 0) return false
    if (this == 1) return true
    throw IllegalArgumentException()
}

fun Boolean.toInt(): Int = if (this) 1 else 0
