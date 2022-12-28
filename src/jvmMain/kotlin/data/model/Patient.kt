package data.model

import java.sql.Date

data class Patient(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val nationalNumber: String,
    val phone: String,
    val birthDate: Date,
    val insId: Int
)
