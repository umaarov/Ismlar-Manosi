package uz.umarov.ismlarmanosi.vm

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.umarov.ismlarmanosi.models.Properties
import uz.umarov.ismlarmanosi.utils.NamesObject
import uz.umarov.ismlarmanosi.caching.MySharedPreferences

class NamesViewModel : ViewModel() {

    private val namesList = MutableLiveData<ArrayList<Properties>>()
    val currentList = MutableLiveData<ArrayList<Properties>>()

    fun loadNames(type: String, context: Context) {
        val list = ArrayList<Properties>()
        when (type) {
            "m" -> list.addAll(NamesObject.boys())
            "f" -> list.addAll(NamesObject.girls())
            "a" -> list.addAll(NamesObject.properties)
            "like" -> {
                MySharedPreferences.init(context)
                list.addAll(MySharedPreferences.objectString)
            }
        }
        namesList.postValue(list)
        currentList.postValue(list)
    }

    fun filterNames(query: String) {
        if (query.isEmpty()) {
            currentList.postValue(namesList.value)
        } else {
            val filteredList = ArrayList<Properties>()
            namesList.value?.forEach { properties ->
                if (properties.name.startsWith(query, ignoreCase = true)) {
                    filteredList.add(properties)
                }
            }
            currentList.postValue(filteredList)
        }
    }

    fun toggleLike(properties: Properties, context: Context) {
        MySharedPreferences.init(context)
        val list = MySharedPreferences.objectString
        if (!list.contains(properties)) {
            list.add(properties)
            MySharedPreferences.objectString = list
        } else {
            list.remove(properties)
            MySharedPreferences.objectString = list
        }
    }

    fun isLiked(properties: Properties, context: Context): Boolean {
        MySharedPreferences.init(context)
        return MySharedPreferences.objectString.contains(properties)
    }
}
