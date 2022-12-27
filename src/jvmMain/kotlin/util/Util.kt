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
