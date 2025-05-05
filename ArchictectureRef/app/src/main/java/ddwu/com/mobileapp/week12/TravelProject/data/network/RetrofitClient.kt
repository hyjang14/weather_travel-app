package ddwu.com.mobileapp.week12.TravelProject.api

import IWeatherService
import com.google.gson.GsonBuilder
import ddwu.com.mobileapp.week12.TravelProject.data.network.ITravelService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://apis.data.go.kr/"
    private const val WEATHER_BASE_URL = "https://apis.data.go.kr/"

    val gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val travelService: ITravelService by lazy {
        retrofit.create(ITravelService::class.java)
    }

    val weatherService: IWeatherService by lazy {
        Retrofit.Builder()
            .baseUrl(WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IWeatherService::class.java)
    }
}
