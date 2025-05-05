package ddwu.com.mobileapp.week12.TravelProject

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ddwu.com.mobileapp.week12.TravelProject.api.RetrofitClient
import ddwu.com.mobileapp.week12.TravelProject.data.TravelItem
import ddwu.com.mobileapp.week12.TravelProject.data.network.Root
import ddwu.com.mobileapp.week12.TravelProject.ui.TravelAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TravelListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TravelAdapter
    private val travelList = mutableListOf<TravelItem>()
    private val originalList = mutableListOf<TravelItem>()
    private lateinit var searchEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travel_list)

        recyclerView = findViewById(R.id.recyclerView)
        adapter = TravelAdapter(travelList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        searchEditText = findViewById(R.id.searchEditText)

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterList(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // 전달받은 위도, 경도
        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)
        Log.d("TravelListActivity", "받은 위도: $latitude, 경도: $longitude")

        // API 호출 파라미터
        val serviceKey = "NRDO0f8r3dnkSmdyw8UVv/+hbg5Gb2DFiva7H990qeKjzR0W9H9uhIKnFi/Z6VW+YLAZGcf6nd9F0VhIyYU/yw=="
        val mapX = longitude.toFloat()
        val mapY = latitude.toFloat()
        val radius = 1000
        val mobileApp = "AppTest"
        val mobileOS = "ETC"
        val listYN = "Y"
        val arrange = "A"
        val numOfRows = 200

        // ITravelService API 호출
        RetrofitClient.travelService.getTravel(
            type = "json",
            key = serviceKey,
            mapX = mapX,
            mapY = mapY,
            radius = radius,
            mobileApp = mobileApp,
            mobileOS = mobileOS,
            listYN = listYN,
            arrange = arrange,
            numOfRows = numOfRows
        ).enqueue(object : Callback<Root> {
            override fun onResponse(call: Call<Root>, response: Response<Root>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        it.response.body.items?.item?.let { items ->
                            if (items.isNotEmpty()) {
                                val travelItems = items.map { item ->
                                    val imageUrl = item.firstimage ?: ""
                                    Log.d("TravelListActivity", "Image URL: $imageUrl")
                                    TravelItem(
                                        name = item.title ?: "관광지 이름이 없습니다.",
                                        address = item.addr1 ?: "주소가 없습니다.",
                                        tel = item.tel ?: "전화번호가 없습니다.",
                                        imageUrl = imageUrl ?: "",
                                        latitude = item.mapy.toDouble(),
                                        longitude = item.mapx.toDouble()
                                    )

                                }
                                originalList.clear()
                                originalList.addAll(travelItems)
                                travelList.clear()
                                travelList.addAll(travelItems)
                                adapter.notifyDataSetChanged()
                            } else {
                                Log.e("TravelListActivity", "No items found in the response")
                            }
                        }
                    }
                } else {
                    Log.e("TravelListActivity", "API 호출 실패: ${response.message()}")
                    val errorBody = response.errorBody()?.string()
                    Log.e("TravelListActivity", "Error Body: $errorBody")
                }
            }
            override fun onFailure(call: Call<Root>, t: Throwable) {
                Log.e("TravelListActivity", "API 호출 아예 실패: ${t.message}")
            }
        })
        // 리스트 아이템 클릭 리스너
        adapter.setOnItemClickListener { place ->
            onPlaceSelected(place)
        }
    }

    private fun filterList(query: String) {
        val filteredList = if (query.isEmpty()) {
            originalList
        } else {
            originalList.filter { it.name.contains(query, ignoreCase = true) }
        }
        travelList.clear()
        travelList.addAll(filteredList)
        adapter.notifyDataSetChanged()
    }

    private fun onPlaceSelected(place: TravelItem) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("latitude", place.latitude)
        intent.putExtra("longitude", place.longitude)
        intent.putExtra("name", place.name)
        startActivity(intent)
    }
}
