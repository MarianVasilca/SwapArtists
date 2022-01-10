package app.swapartists.ui.artist_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.swapartists.data.model.ArtistRelease
import app.swapartists.databinding.ItemArtistReleaseBinding

class ArtistReleaseAdapter :
    ListAdapter<ArtistRelease, ArtistReleaseViewHolder>(ITEM_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistReleaseViewHolder =
        ArtistReleaseViewHolder.create(parent)

    override fun onBindViewHolder(holder: ArtistReleaseViewHolder, position: Int) =
        holder.bind(getItem(position))

    companion object {
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<ArtistRelease>() {
            override fun areItemsTheSame(oldItem: ArtistRelease, newItem: ArtistRelease): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ArtistRelease,
                newItem: ArtistRelease
            ): Boolean = oldItem == newItem
        }
    }
}

class ArtistReleaseViewHolder(
    private val binding: ItemArtistReleaseBinding,
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var item: ArtistRelease

    fun bind(item: ArtistRelease) {
        this.item = item
        binding.tvTitle.text = item.title
        binding.tvDate.text = (item.date as? String) ?: ""
    }

    companion object {
        fun create(
            parent: ViewGroup
        ): ArtistReleaseViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemArtistReleaseBinding.inflate(inflater, parent, false)
            return ArtistReleaseViewHolder(binding)
        }
    }
}