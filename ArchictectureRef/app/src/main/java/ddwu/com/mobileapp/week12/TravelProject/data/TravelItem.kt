package ddwu.com.mobileapp.week12.TravelProject.data

data class TravelItem(
    val name: String,
    val address: String,
    val tel: String,
    val imageUrl: String,
    val latitude: Double, // 위도 (mapy)
    val longitude: Double // 경도 (mapx)
)