package ddwu.com.mobileapp.week12.TravelProject.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import ddwu.com.mobileapp.week12.TravelProject.data.MapRepository
import ddwu.com.mobileapp.week12.TravelProject.data.database.RefEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapViewModel (val mapRepository: MapRepository) : ViewModel() {
    val allRefs : LiveData<List<RefEntity>> = mapRepository.allRefs.asLiveData()

    private var _name = MutableLiveData<String>()
    val nameData = _name

    fun findName(id: Int) = viewModelScope.launch {
        var result : String
        withContext(Dispatchers.IO) {
            result = mapRepository.getNameById(id)
        }
        _name.value = result
    }
}