package data.model

import java.sql.ResultSet

private const val idKey = "id"
private const val nameKey = "name"
private const val needPrcKey = "need_prc"
private const val usageKey = "usage"
private const val sideEffectsKey = "side_effects"
private const val categoryIdKey = "category_id"


data class Pharm(
    val id: Int,
    val name: String,
    val needPrescription: Boolean,
    val usage: String?,
    val sideEffects: String?,
    val categoryId: Int
) {

    constructor(result: ResultSet) : this(
        result.getInt(idKey),
        result.getString(nameKey),
        result.getBoolean(needPrcKey),
        result.getString(usageKey),
        result.getString(sideEffectsKey),
        result.getInt(categoryIdKey),
    )

    fun values() = "$id, '$name', $needPrescription, '$usage', '$sideEffects', $categoryId"

    fun parametricValues() =
        "$nameKey='$name', $needPrcKey=$needPrescription, `$usageKey`='$usage', $sideEffectsKey='$sideEffects', " +
                "$categoryIdKey=$categoryId"
}







