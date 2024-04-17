package uz.umarov.ismlarmanosi.caching

import android.content.Context
import android.content.SharedPreferences
import uz.umarov.ismlarmanosi.models.Properties
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object MySharedPreferences {
    private const val NAME = "ToCache"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var objectString: ArrayList<Properties>
        get() = gsonStringToArray(preferences.getString("object", "[]")!!)
        set(value) = preferences.edit {
            it.putString("object", arrayToGsonString(value))
        }

    private fun arrayToGsonString(arrayList: ArrayList<Properties>): String {
        return Gson().toJson(arrayList)
    }

    private fun gsonStringToArray(gsonString: String): ArrayList<Properties> {
        val typeToken = object : TypeToken<ArrayList<Properties>>() {}.type
        return Gson().fromJson(gsonString, typeToken)
    }
}