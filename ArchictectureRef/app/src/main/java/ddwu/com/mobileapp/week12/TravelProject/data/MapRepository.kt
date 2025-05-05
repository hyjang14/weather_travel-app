package ddwu.com.mobileapp.week12.TravelProject.data

import ddwu.com.mobileapp.week12.TravelProject.data.database.RefDao
import ddwu.com.mobileapp.week12.TravelProject.data.database.RefEntity
import ddwu.com.mobileapp.week12.TravelProject.data.network.RefService
import kotlinx.coroutines.flow.Flow     // Flow 사용 시 직접 추가

class MapRepository(private val refDao: RefDao, private val refService: RefService) {
    val allRefs : Flow<List<RefEntity>> = refDao.getAllRefs()

    suspend fun getNameById(id: Int) : String {
        return refDao.getNameById(id)
    }
}

