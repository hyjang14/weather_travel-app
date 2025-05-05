package ddwu.com.mobileapp.week12.TravelProject

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog

class ReviewDialogFragment(private val review: Review) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val message = """
            장소: ${review.place}
            별점: ${review.rating}
            날짜: ${review.date}
            일기: ${review.diary}
        """.trimIndent()

        return AlertDialog.Builder(requireContext())
            .setTitle("리뷰 상세 내용")
            .setMessage(message)
            .setPositiveButton("확인", null)
            .create()
    }
}
