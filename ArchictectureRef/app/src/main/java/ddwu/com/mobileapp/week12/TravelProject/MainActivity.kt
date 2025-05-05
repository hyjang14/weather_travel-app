package ddwu.com.mobileapp.week12.TravelProject

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ddwu.com.mobileapp.week12.TravelProject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivityTag"

    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    private var currentLatitude: Double? = null
    private var currentLongitude: Double? = null

    private lateinit var googleMap: GoogleMap
    private var isMapReady = false

    private val mapReadyCallback = object : OnMapReadyCallback {
        override fun onMapReady(map: GoogleMap) {
            googleMap = map
            isMapReady = true
            Log.d(TAG, "GoogleMap is ready")

            intent?.let {
                val latitude = it.getDoubleExtra("latitude", 0.0)
                val longitude = it.getDoubleExtra("longitude", 0.0)
                val name = it.getStringExtra("name")

                Log.d(TAG, "받은 위치: 위도 = $latitude, 경도 = $longitude, 관광지 이름 = $name")

                if (latitude != 0.0 && longitude != 0.0) {
                    Log.d(TAG, "googleMap이 준비되었으므로 마커를 추가합니다.")
                    addMarkerToMap(latitude, longitude, name ?: "관광지")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val mapFragment: SupportMapFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as SupportMapFragment
        mapFragment.getMapAsync(mapReadyCallback)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.Builder(3000)
            .setMinUpdateIntervalMillis(5000)
            .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val currentLocation: Location = locationResult.locations[0]
                Log.d(TAG, "위도: ${currentLocation.latitude}, 경도: ${currentLocation.longitude}")

                // 위치 정보 저장
                currentLatitude = currentLocation.latitude
                currentLongitude = currentLocation.longitude
                Log.d(TAG, "위도: ${currentLatitude}, 경도: ${currentLongitude}")

                val targetLoc: LatLng = LatLng(currentLocation.latitude, currentLocation.longitude)

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(targetLoc, 17F))
            }
        }

        // 1. 현재 위치 지도에 표시
        binding.btnMy.setOnClickListener {
            checkPermissions()
            startLocationRequest()
        }

        // 2. 근처 관광지 리스트
        binding.btnGetPlace.setOnClickListener {
            if (currentLatitude != null && currentLongitude != null) {
                val intent = Intent(this, TravelListActivity::class.java)
                intent.putExtra("latitude", currentLatitude)
                intent.putExtra("longitude", currentLongitude)
                startActivity(intent)
            } else {
                // 현재 위치 정보가 없을 경우
                showLocationWarning()
            }
        }

        // 3. 마커 추가
        intent?.let {
            val latitude = it.getDoubleExtra("latitude", 0.0)
            val longitude = it.getDoubleExtra("longitude", 0.0)
            val name = it.getStringExtra("name")

            Log.d(TAG, "받은 위치: 위도 = $latitude, 경도 = $longitude, 관광지 이름 = $name")

            if (latitude != 0.0 && longitude != 0.0) {
                if (isMapReady) {
                    Log.d(TAG, "googleMap이 준비되었으므로 마커를 추가합니다.")
                    addMarkerToMap(latitude, longitude, name ?: "관광지")
                } else {
                    Log.d(TAG, "googleMap이 아직 준비되지 않았습니다. 준비되면 마커를 추가합니다.")
                }
            }
        }

        // 4. 후기 작성
        binding.btnReview.setOnClickListener {
            val intent = Intent(this, ReviewActivity::class.java)
            startActivity(intent)
        }

        // 5. 나의 관광지 후기
        binding.btnReviewList.setOnClickListener {
            val intent = Intent(this, ReviewListActivity::class.java)
            startActivity(intent)
        }

        // 6. 현재 위치의 날씨 확인하기
        binding.btnWeather.setOnClickListener {
            if (currentLatitude != null && currentLongitude != null) {
                val intent = Intent(this, WeatherActivity::class.java)
                intent.putExtra("latitude", currentLatitude)
                intent.putExtra("longitude", currentLongitude)
                startActivity(intent)
            } else {
                // 현재 위치 정보가 없을 경우
                showLocationWarning()
            }
        }
    }

    private fun showLocationWarning() {
        Toast.makeText(this, "현재 위치를 표시해주세요", Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun startLocationRequest() {
        fusedLocationClient.requestLocationUpdates(locationRequest,
            locationCallback, Looper.getMainLooper())
    }

    // Permission 확인
    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        when {
            permissions.getOrDefault(ACCESS_FINE_LOCATION, false) -> Log.d(TAG, "정확한 위치 사용")
            permissions.getOrDefault(ACCESS_COARSE_LOCATION, false) -> Log.d(TAG, "근사 위치 사용")
            else -> Log.d(TAG, "권한 미승인")
        }
    }

    private fun checkPermissions() {
        if (checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && checkSelfPermission(ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "필요 권한 있음")
        } else {
            locationPermissionRequest.launch(
                arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
            )
        }
    }

    // 구글 지도에 마커 추가
    fun addMarkerToMap(latitude: Double, longitude: Double, title: String) {
        val location = LatLng(latitude, longitude)
        val markerOptions = MarkerOptions().position(location).title(title)
        googleMap.addMarker(markerOptions)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f)) // 마커 위치로 카메라 이동

        Log.d(TAG, "마커 추가됨: 위도 = $latitude, 경도 = $longitude, 제목 = $title")
    }
}

