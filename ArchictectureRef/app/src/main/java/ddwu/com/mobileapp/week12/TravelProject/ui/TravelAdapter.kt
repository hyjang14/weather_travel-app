package ddwu.com.mobileapp.week12.TravelProject.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ddwu.com.mobileapp.week12.TravelProject.R
import ddwu.com.mobileapp.week12.TravelProject.data.TravelItem
import ddwu.com.mobileapp.week12.TravelProject.databinding.ItemTravelBinding

class TravelAdapter(private val items: MutableList<TravelItem>) : RecyclerView.Adapter<TravelAdapter.TravelViewHolder>() {

    private var onItemClickListener: ((TravelItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (TravelItem) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelViewHolder {
        val binding = ItemTravelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TravelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TravelViewHolder, position: Int) {
        holder.bind(items[position])

        holder.itemView.setOnClickListener {
            val place = items[position]
            onItemClickListener?.invoke(place)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class TravelViewHolder(private val binding: ItemTravelBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TravelItem) {
            binding.titleTextView.text = item.name
            binding.locationTextView.text = item.address
            binding.telTextView.text = item.tel

            // 이미지 로드
            Glide.with(binding.root.context)
                .load(item.imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(binding.imageView)
            Log.d("TravelAdapter", "이미지 로드: ${item.imageUrl}")

            // 클릭 리스너 설정
            binding.root.setOnClickListener {
                onItemClickListener?.invoke(item)
            }
        }
    }

    fun updateList(newList: List<TravelItem>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }
}
