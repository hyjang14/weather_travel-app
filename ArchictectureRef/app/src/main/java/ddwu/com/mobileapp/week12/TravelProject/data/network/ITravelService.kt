package ddwu.com.mobileapp.week12.TravelProject.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITravelService {
    @GET("B551011/KorService1/locationBasedList1")
    fun getTravel(
        @Query("_type") type: String,
        @Query("serviceKey") key: String,
        @Query("mapX") mapX: Float,
        @Query("mapY") mapY: Float,
        @Query("radius") radius: Int,
        @Query("MobileApp") mobileApp: String,
        @Query("MobileOS") mobileOS: String,
        @Query("listYN") listYN: String,
        @Query("arrange") arrange: String,
        @Query("numOfRows") numOfRows: Int
    ): Call<Root>
}