package data.model

import java.sql.Date

data class Presciption(
    val id: Int,
    val date: Date,
    val totalPrice: Long,
    val isPaid: Boolean,
    val docId: Int,
    val patId: Int,
)
