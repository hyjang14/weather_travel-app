package ddwu.com.mobileapp.week12.TravelProject

import android.app.Application
import ddwu.com.mobileapp.week12.TravelProject.data.MapRepository
import ddwu.com.mobileapp.week12.TravelProject.data.database.RefDatabase
import ddwu.com.mobileapp.week12.TravelProject.data.network.RefService

class MapApplication : Application() {
    val refDao by lazy {
        RefDatabase.getDatabase(this).refDao()
    }
    val refService by lazy {
        RefService()
    }
    val mapRepository by lazy {
        MapRepository(refDao, refService)
    }
}