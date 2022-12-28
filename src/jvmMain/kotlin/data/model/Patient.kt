package data.model

import java.sql.Date
import java.sql.ResultSet


private const val idKey = "id"
private const val firstNameKey = "first_name"
private const val lastNameKey = "last_name"
private const val nationalNumberKey = "nat_num"
private const val phoneKey = "phone"
private const val birthDateKey = "birth_date"
private const val insuranceIdKey = "ins_id"

data class Patient(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val nationalNumber: String,
    val phone: String?,
    val birthDate: Date,
    val insuranceId: Int
) {

    constructor(result: ResultSet) : this(
        result.getInt(idKey),
        result.getString(firstNameKey),
        result.getString(lastNameKey),
        result.getString(nationalNumberKey),
        result.getString(phoneKey),
        result.getDate(birthDateKey),
        result.getInt(insuranceIdKey),
    )

    fun values() = "$id, '$firstName', '$lastName', '$nationalNumber', '${phone ?: ""}', '$birthDate', $insuranceId"

    fun parametricValues() =
        "$firstNameKey='$firstName', $lastNameKey='$lastName', $nationalNumberKey='$nationalNumber', " +
                "$phoneKey='${phone ?: ""}', $birthDateKey='$birthDate', $insuranceIdKey=$insuranceId"
}
