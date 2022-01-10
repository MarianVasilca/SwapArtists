package app.swapartists.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.swapartists.data.model.FavoriteArtist
import app.swapartists.databinding.ItemFavoriteArtistBinding

class FavoriteArtistAdapter : ListAdapter<FavoriteArtist, FavoriteArtistViewHolder>(
    ITEM_COMPARATOR
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteArtistViewHolder {
        return FavoriteArtistViewHolder.create(parent)
    }

    override fun onBindViewHolder(
        holder: FavoriteArtistViewHolder,
        position: Int
    ) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<FavoriteArtist>() {
            override fun areItemsTheSame(
                oldItem: FavoriteArtist,
                newItem: FavoriteArtist
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: FavoriteArtist,
                newItem: FavoriteArtist
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

class FavoriteArtistViewHolder(
    private val binding: ItemFavoriteArtistBinding
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var item: FavoriteArtist

    fun bind(item: FavoriteArtist) {
        this.item = item
        binding.tvName.text = item.name
        binding.tvDisambiguation.text = item.disambiguation
    }

    companion object {
        fun create(
            parent: ViewGroup
        ): FavoriteArtistViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemFavoriteArtistBinding.inflate(inflater, parent, false)
            return FavoriteArtistViewHolder(binding)
        }
    }

}