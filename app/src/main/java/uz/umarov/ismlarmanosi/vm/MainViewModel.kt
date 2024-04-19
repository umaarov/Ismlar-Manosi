package uz.umarov.ismlarmanosi.vm

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uz.umarov.ismlarmanosi.models.Names
import java.io.IOException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import uz.umarov.ismlarmanosi.models.Properties


class MainViewModel : ViewModel() {

    fun loadNames(context: Context, language: String) = liveData {
        val fileName = if (language == "uz") "names.json" else "names_en.json"
        val data = getJsonDataFromAsset(context, fileName)
        if (data != null) {
            val gson = Gson()
            val listType = object : TypeToken<Names>() {}.type
            val namesList: Names = gson.fromJson(data, listType)
            val properties = ArrayList<Properties>(namesList.map { it.properties })
            emit(properties)
        } else {
            emit(ArrayList())
        }
    }

    private fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
