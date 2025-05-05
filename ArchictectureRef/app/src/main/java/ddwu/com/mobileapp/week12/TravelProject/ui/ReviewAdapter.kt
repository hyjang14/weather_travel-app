package ddwu.com.mobileapp.week12.TravelProject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ddwu.com.mobileapp.week12.TravelProject.databinding.ItemReviewBinding

class ReviewAdapter(
    private val reviewList: MutableList<Review>,
    private val onItemLongClick: (Review) -> Unit
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviewList[position]
        holder.binding.tvPlaceName.text = review.place
        holder.binding.ratingBar.rating = review.rating
        holder.binding.tvDate.text = review.date
        holder.binding.tvDiary.text = review.diary

        // 리뷰 삭제
        holder.itemView.setOnLongClickListener {
            onItemLongClick(review)
            true
        }
    }


    override fun getItemCount(): Int = reviewList.size
}
