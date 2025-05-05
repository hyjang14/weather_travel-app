package ddwu.com.mobileapp.week12.TravelProject

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ddwu.com.mobileapp.week12.TravelProject.databinding.ActivityReviewListBinding

class ReviewListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReviewListBinding
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var dbHelper: ReviewDatabaseHelper
    private lateinit var reviewList: MutableList<Review>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = ReviewDatabaseHelper(this)

        // SQLite에서 리뷰 목록 가져오기
        reviewList = dbHelper.getAllReviews().toMutableList()

        reviewAdapter = ReviewAdapter(
            reviewList,
            onItemLongClick = { review ->
                // 삭제 다이얼로그
                val dialog = AlertDialog.Builder(this)
                    .setTitle("후기 삭제")
                    .setMessage("정말로 이 후기를 삭제하시겠습니까?")
                    .setPositiveButton("삭제") { _, _ ->
                        review.id?.let { id ->
                            val deleted = dbHelper.deleteReview(id)
                            if (deleted) {
                                reviewList.remove(review)
                                reviewAdapter.notifyDataSetChanged()
                                Toast.makeText(this, "후기가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "삭제 실패", Toast.LENGTH_SHORT).show()
                            }
                        } ?: run {
                            Toast.makeText(this, "존재하지 않는 후기입니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("취소", null)
                    .create()

                dialog.show()
            }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ReviewListActivity)
            adapter = reviewAdapter
        }
    }

}
