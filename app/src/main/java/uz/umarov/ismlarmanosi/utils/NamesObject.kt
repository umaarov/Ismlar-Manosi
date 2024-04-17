package uz.umarov.ismlarmanosi.utils

import uz.umarov.ismlarmanosi.models.Properties
import java.util.*
import kotlin.collections.ArrayList

object NamesObject {
    var properties = ArrayList<Properties>()

    fun boys(): ArrayList<Properties> {
        val boys = ArrayList<Properties>()

        for (i in 0 until properties.size) {
            if (properties[i].gender.lowercase(Locale.getDefault()) == "m") {
                boys.add(properties[i])
            }
        }
        return boys
    }

    fun girls(): ArrayList<Properties> {
        val girls = ArrayList<Properties>()

        for (i in 0 until properties.size) {
            if (properties[i].gender.lowercase(Locale.getDefault()) == "f") {
                girls.add(properties[i])
            }
        }
        return girls
    }

}