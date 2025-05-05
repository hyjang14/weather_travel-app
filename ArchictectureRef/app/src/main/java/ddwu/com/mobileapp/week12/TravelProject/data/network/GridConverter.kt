package ddwu.com.mobileapp.week12.TravelProject.data.network

import kotlin.math.*

object GridConverter {
    private const val RE = 6371.00877
    private const val GRID = 5.0
    private const val SLAT1 = 30.0
    private const val SLAT2 = 60.0
    private const val OLON = 126.0
    private const val OLAT = 38.0
    private const val XO = 43.0
    private const val YO = 136.0

    private const val DEGRAD = Math.PI / 180.0

    fun latLngToGrid(lat: Double, lng: Double): Grid {
        val re = RE / GRID
        val slat1 = SLAT1 * DEGRAD
        val slat2 = SLAT2 * DEGRAD
        val olon = OLON * DEGRAD
        val olat = OLAT * DEGRAD

        var sn = tan(Math.PI * 0.25 + slat2 * 0.5) / tan(Math.PI * 0.25 + slat1 * 0.5)
        sn = ln(cos(slat1) / cos(slat2)) / ln(sn)
        var sf = tan(Math.PI * 0.25 + slat1 * 0.5)
        sf = sf.pow(sn) * cos(slat1) / sn
        var ro = tan(Math.PI * 0.25 + olat * 0.5)
        ro = re * sf / ro.pow(sn)

        val ra = tan(Math.PI * 0.25 + lat * DEGRAD * 0.5)
        val gridX = (re * sf / ra.pow(sn) * sin((lng * DEGRAD - olon) * sn) + XO + 0.5).toInt()
        val gridY = (ro - re * sf / ra.pow(sn) * cos((lng * DEGRAD - olon) * sn) + YO + 0.5).toInt()

        return Grid(gridX, gridY)
    }

    data class Grid(val x: Int, val y: Int)
}
