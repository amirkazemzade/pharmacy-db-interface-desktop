package data.model

import java.sql.ResultSet

private const val idKey = "id"
private const val nameKey = "name"
private const val emailKey = "email"
private const val addressKey = "address"

data class Insurance(
    val id: Int,
    val name: String,
    val email: String?,
    val address: String?
) {
    constructor(result: ResultSet) : this(
        result.getInt(idKey),
        result.getString(nameKey),
        result.getString(emailKey),
        result.getString(addressKey),
    )

    fun values() = "$id, '$name', '${email ?: ""}', '${address ?: ""}'"

    fun parametricValues() =
        "$nameKey='$name', $emailKey='${email ?: ""}', $addressKey='${address ?: ""}'"
}
