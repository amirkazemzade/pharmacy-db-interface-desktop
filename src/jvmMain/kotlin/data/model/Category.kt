package data.model

import java.sql.ResultSet

private const val idKey = "id"
private const val nameKey = "name"
private const val descriptionKey = "description"

data class Category(
    val id: Int,
    val name: String,
    val description: String?
) {
    constructor(result: ResultSet) : this(
        result.getInt(idKey),
        result.getString(nameKey),
        result.getString(descriptionKey),
    )

    fun values() = "$id, '$name', '${description ?: ""}'"

    fun parametricValues() = "$nameKey='$name', $descriptionKey='${description ?: ""}'"
}
