package uz.umarov.ismlarmanosi.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.umarov.ismlarmanosi.utils.NamesObject

class BaseViewModel : ViewModel() {
    private val _boysCount = MutableLiveData<Int>()
    val boysCount: LiveData<Int> = _boysCount

    private val _girlsCount = MutableLiveData<Int>()
    val girlsCount: LiveData<Int> = _girlsCount

    init {
        loadCounts()
    }

    private fun loadCounts() {
        _boysCount.value = NamesObject.boys().size
        _girlsCount.value = NamesObject.girls().size
    }
}
