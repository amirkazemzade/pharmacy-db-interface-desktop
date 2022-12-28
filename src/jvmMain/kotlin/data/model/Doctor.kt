package data.model

import java.sql.ResultSet

private const val idKey = "id"
private const val firstNameKey = "first_name"
private const val lastNameKey = "last_name"
private const val emailKey = "email"
private const val phoneKey = "phone"
private const val addressKey = "address"
private const val licenceIdKey = "license_id"

data class Doctor(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String?,
    val phone: String?,
    val address: String?,
    val licenceId: String
) {

    constructor(result: ResultSet) : this(
        result.getInt(idKey),
        result.getString(firstNameKey),
        result.getString(lastNameKey),
        result.getString(emailKey),
        result.getString(phoneKey),
        result.getString(addressKey),
        result.getString(licenceIdKey),
    )

    fun values() = "$id, '$firstName', '$lastName', '${email ?: ""}', '${phone ?: ""}', '${address ?: ""}', $licenceId"

    fun parametricValues() =
        "$firstNameKey='$firstName', $lastNameKey='$lastName', $emailKey='$email', " +
                "$phoneKey='${phone ?: ""}', $addressKey='${address ?: ""}', $licenceIdKey=$licenceId"
}
