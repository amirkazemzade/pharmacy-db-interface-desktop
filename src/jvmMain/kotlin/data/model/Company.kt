package data.model

import java.sql.ResultSet

private const val idKey = "id"
private const val nameKey = "name"
private const val countryKey = "country"
private const val emailKey = "email"
private const val addressKey = "address"

data class Company(
    val id: Int,
    val name: String,
    val country: String,
    val email: String,
    val address: String?
) {
    constructor(result: ResultSet) : this(
        result.getInt(idKey),
        result.getString(nameKey),
        result.getString(countryKey),
        result.getString(emailKey),
        result.getString(addressKey),
    )

    fun values() = "$id, '$name', '$country', '$email', '${address ?: ""}'"

    fun parametricValues() =
        "$nameKey='$name', $countryKey='$country', $emailKey='$email', $addressKey='${address ?: ""}'"
}
