package data.model

import java.sql.Date
import java.sql.ResultSet

data class Med(
    val id: Int,
    val pharmId: Int,
    val compId: Int,
    val inv: Int,
    val price: Long,
    val expirationDate: Date,
    val medName: String,
) {
    constructor(result: ResultSet) : this(
        result.getInt("id"),
        result.getInt("pharm_id"),
        result.getInt("comp_id"),
        result.getInt("inv"),
        result.getLong("price"),
        result.getDate("expiration_date"),
        result.getString("med_name"),
    )

    fun values() = "$id, $pharmId, $compId, $inv, $price, '$expirationDate', '$medName'"

    fun parametricValues() =
        "pharm_id=$pharmId, comp_id=$compId, inv=$inv, price=$price, " +
                "expiration_date='$expirationDate', med_name='$medName'"
}