package ddwu.com.mobileapp.week12.TravelProject

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ddwu.com.mobileapp.week12.TravelProject.api.RetrofitClient
import ddwu.com.mobileapp.week12.TravelProject.data.network.GridConverter
import ddwu.com.mobileapp.week12.TravelProject.data.network.WeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {

    private lateinit var tvWeatherInfo: TextView
    private lateinit var dateText: TextView
    private lateinit var timeText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        tvWeatherInfo = findViewById(R.id.weatherInfo)
        dateText = findViewById(R.id.dateText)
        timeText = findViewById(R.id.timeText)

        // MainActivity의 Intent로부터 위도와 경도 데이터 가져오기
        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)

        // 위도, 경도를 격자로 변환
        val grid = GridConverter.latLngToGrid(latitude, longitude)

        Log.d("WeatherActivity", "x: ${grid.x}, y: ${grid.y}")

        // 날짜 및 시간 포맷 설정
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
        val timeFormat = SimpleDateFormat("HH:mm", Locale.KOREA)
        val currentDate = Date()

        val formattedDate = dateFormat.format(currentDate) // 2024-11-14 형식
        val formattedTime = timeFormat.format(currentDate) // 23:39 형식

        dateText.text = formattedDate
        timeText.text = formattedTime

        fetchWeatherData(grid.x, grid.y)
    }

    private fun fetchWeatherData(gridX: Int, gridY: Int) {
        val serviceKey = "NRDO0f8r3dnkSmdyw8UVv/+hbg5Gb2DFiva7H990qeKjzR0W9H9uhIKnFi/Z6VW+YLAZGcf6nd9F0VhIyYU/yw=="

        // 날짜 및 시간 포맷 설정
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.KOREA)
        val timeFormat = SimpleDateFormat("HH00", Locale.KOREA)
        val currentDate = Date()

        val cal = Calendar.getInstance().apply { time = currentDate }
        val currentTime = cal.get(Calendar.HOUR_OF_DAY)

        var dateString = dateFormat.format(currentDate)
        var timeString = timeFormat.format(currentDate)

        // 날짜를 하루 전으로 설정해서 파라미터 base값 맞추기
        cal.add(Calendar.DATE, -1)
        var baseDate = dateFormat.format(cal.time)
        var baseTime = "2300"

        Log.d("WeatherActivity", "날짜: $dateString, 시간: $timeString")
        Log.d("WeatherActivity", "베이스 날짜: $baseDate, 베이스 시간: $baseTime")

        RetrofitClient.weatherService.getWeatherData(
            serviceKey = serviceKey,
            pageNo = 1,
            numOfRows = 290,
            dataType = "JSON",
            baseDate = baseDate,
            baseTime = baseTime,
            nx = gridX,
            ny = gridY
        ).enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Log.d("WeatherActivity", "응답 본문: ${responseBody}")
                        val header = responseBody.response.header
                        if (header.resultCode == "00") {
                            val items = responseBody.response.body.items.item
                            if (items.isNotEmpty()) {
                                val filteredItems = items.filter {
                                    // fcstDate와 fcstTime이 동일한 항목만 선택
                                    it.fcstDate == dateString && it.fcstTime == timeString
                                }.filter {
                                    it.category == "POP" || it.category == "TMP" || it.category == "SNO"
                                }.map { item ->
                                    when (item.category) {
                                        "POP" -> {
                                            val popValue = item.fcstValue.toInt()
                                            // 강수확률에 따라 이모지 변경
                                            val weatherIcon = when {
                                                popValue in 0..30 -> "🌞"
                                                popValue in 31..50 -> "☁️"
                                                popValue in 51..70 -> "🌦️"
                                                popValue in 71..100 -> "🌧️"
                                                else -> ""
                                            }

                                            findViewById<TextView>(R.id.icon).text = weatherIcon

                                            "강수확률: ${item.fcstValue}%"
                                        }
                                        "TMP" -> "기온: ${item.fcstValue}℃"
                                        "SNO" -> "적설: ${item.fcstValue}"
                                        else -> ""
                                    }
                                }.joinToString("\n")

                                tvWeatherInfo.text = filteredItems

                                Log.d("WeatherActivity", "날씨 정보: $filteredItems")
                            } else {
                                Log.e("WeatherActivity", "날씨 정보가 없음")
                                tvWeatherInfo.text = "날씨 정보가 없습니다."
                            }
                        } else {
                            Log.e("WeatherActivity", "잘못된 응답 코드: ${header.resultCode}")
                            tvWeatherInfo.text = "응답 오류: 잘못된 코드"
                        }
                    } else {
                        Log.e("WeatherActivity", "응답 본문이 null입니다.")
                        tvWeatherInfo.text = "응답 본문이 null입니다."
                    }
                } else {
                    Log.e("WeatherActivity", "API 호출 실패: ${response.message()}")
                    tvWeatherInfo.text = "응답 실패: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.e("WeatherActivity", "API 호출 실패: ${t.message}")
                tvWeatherInfo.text = "네트워크 오류: ${t.message}"
            }
        })
    }
}
