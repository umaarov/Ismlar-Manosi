package uz.umarov.ismlarmanosi.models

import java.io.Serializable

data class Properties(
    val gender: String,
    val letter: String,
    val meaning: String,
    val name: String,
    val origin: String
):Serializable