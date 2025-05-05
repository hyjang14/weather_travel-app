package ddwu.com.mobileapp.week12.TravelProject.data.network

data class WeatherResponse(
    val response: WeatherResponseBody
)

data class WeatherResponseBody(
    val header: WeatherResponseHeader,
    val body: WeatherResponseBodyData
)

data class WeatherResponseHeader(
    val resultCode: String,
    val resultMsg: String
)

data class WeatherResponseBodyData(
    val dataType: String,
    val items: WeatherDataItems,
    val pageNo: Long,
    val numOfRows: Long,
    val totalCount: Long
)

data class WeatherDataItems(
    val item: List<WeatherDataItem>
)

data class WeatherDataItem(
    val baseDate: String,
    val baseTime: String,
    val category: String,
    val fcstDate: String,
    val fcstTime: String,
    val fcstValue: String,
    val nx: Long,
    val ny: Long
)
