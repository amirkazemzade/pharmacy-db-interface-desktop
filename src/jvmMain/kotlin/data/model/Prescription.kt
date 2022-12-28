package data.model

import java.sql.Date
import java.sql.ResultSet

private const val idKey = "id"
private const val dateKey = "date"
private const val totalPriceKey = "total_price"
private const val isPaidKey = "is_paid"
private const val doctorIdKey = "doc_id"
private const val patientIdKey = "pat_id"

data class Prescription(
    val id: Int,
    val date: Date,
    val totalPrice: Long = 0,
    val isPaid: Boolean = false,
    val doctorId: Int,
    val patientId: Int,
) {

    constructor(result: ResultSet) : this(
        result.getInt(idKey),
        result.getDate(dateKey),
        result.getLong(totalPriceKey),
        result.getBoolean(isPaidKey),
        result.getInt(doctorIdKey),
        result.getInt(patientIdKey),
    )

    fun values() = "$id, '$date', $totalPrice, $isPaid, $doctorId, $patientId"

    fun parametricValues() =
        "$dateKey='$date', $totalPriceKey=$totalPrice, $isPaidKey=$isPaid, $doctorIdKey=$doctorId, $patientIdKey=$patientId"
}
