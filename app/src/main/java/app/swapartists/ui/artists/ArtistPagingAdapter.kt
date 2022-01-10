package app.swapartists.ui.artists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.swapartists.data.model.ArtistNode
import app.swapartists.databinding.ItemArtistBinding

class ArtistPagingAdapter : PagingDataAdapter<ArtistNode, ArtistViewHolder>(
    diffCallback = ITEM_COMPARATOR
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArtistViewHolder {
        return ArtistViewHolder.create(parent)
    }

    override fun onBindViewHolder(
        holder: ArtistViewHolder,
        position: Int
    ) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<ArtistNode>() {
            override fun areItemsTheSame(oldItem: ArtistNode, newItem: ArtistNode): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ArtistNode, newItem: ArtistNode): Boolean {
                return oldItem == newItem
            }
        }
    }
}

class ArtistViewHolder(
    private val binding: ItemArtistBinding
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var item: ArtistNode

    fun bind(item: ArtistNode) {
        this.item = item
        binding.tvName.text = item.name
        binding.tvDisambiguation.text = item.disambiguation
    }

    companion object {
        fun create(
            parent: ViewGroup
        ): ArtistViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemArtistBinding.inflate(inflater, parent, false)
            return ArtistViewHolder(binding)
        }
    }

}