package ddwu.com.mobileapp.week12.TravelProject

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ddwu.com.mobileapp.week12.TravelProject.databinding.ActivityReviewBinding
import java.util.Calendar

class ReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSelectDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                binding.etDate.setText("$selectedYear-${selectedMonth + 1}-$selectedDay")
            }, year, month, day).show()
        }

        // 저장 버튼 클릭 시
        binding.btnSave.setOnClickListener {
            val placeName = binding.etPlaceName.text.toString()
            val rating = binding.ratingBar.rating
            val date = binding.etDate.text.toString()
            val diary = binding.etDiary.text.toString()

            if (placeName.isBlank() || date.isBlank() || diary.isBlank()) {
                Toast.makeText(this, "모든 필드를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                val review = Review(place = placeName, rating = rating, date = date, diary = diary)
                val dbHelper = ReviewDatabaseHelper(this)
                val result = dbHelper.insertReview(review)

                if (result != -1L) {
                    Toast.makeText(this, "후기 저장 완료!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "후기 저장 실패!", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    private fun saveReview(review: Review) {
        val sharedPreferences = getSharedPreferences("reviews", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()

        val reviewsJson = sharedPreferences.getString("review_list", "[]")
        val type = object : TypeToken<MutableList<Review>>() {}.type
        val reviewList: MutableList<Review> = gson.fromJson(reviewsJson, type)

        reviewList.add(review)

        editor.putString("review_list", gson.toJson(reviewList))
        editor.apply()
    }
}
