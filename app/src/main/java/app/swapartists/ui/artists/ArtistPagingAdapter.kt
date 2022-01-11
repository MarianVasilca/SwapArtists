package app.swapartists.ui.artists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.swapartists.data.model.ArtistModel
import app.swapartists.databinding.ItemArtistBinding

class ArtistPagingAdapter constructor(
    private val itemClickCallback: ((ArtistModel) -> Unit),
    private val favoriteClickCallback: ((ArtistModel) -> Unit),
) : PagingDataAdapter<ArtistModel, ArtistViewHolder>(
    diffCallback = ITEM_COMPARATOR
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArtistViewHolder {
        return ArtistViewHolder.create(parent, itemClickCallback, favoriteClickCallback)
    }

    override fun onBindViewHolder(
        holder: ArtistViewHolder,
        position: Int
    ) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<ArtistModel>() {
            override fun areItemsTheSame(oldItem: ArtistModel, newItem: ArtistModel): Boolean {
                return oldItem.artist.id == newItem.artist.id
            }

            override fun areContentsTheSame(oldItem: ArtistModel, newItem: ArtistModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}

class ArtistViewHolder(
    private val binding: ItemArtistBinding,
    private val itemClickCallback: (ArtistModel) -> Unit,
    favoriteClickCallback: (ArtistModel) -> Unit
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var item: ArtistModel

    init {
        binding.card.setOnClickListener(this)
        binding.cbFavorite.setOnClickListener { favoriteClickCallback.invoke(item) }
    }

    override fun onClick(view: View?) {
        itemClickCallback.invoke(item)
    }

    fun bind(item: ArtistModel) {
        this.item = item
        binding.tvName.text = item.artist.name
        binding.tvDisambiguation.text = item.artist.disambiguation
        binding.cbFavorite.isChecked = item.isFavorite
    }

    companion object {
        fun create(
            parent: ViewGroup,
            itemClickCallback: (ArtistModel) -> Unit,
            favoriteClickCallback: (ArtistModel) -> Unit
        ): ArtistViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemArtistBinding.inflate(inflater, parent, false)
            return ArtistViewHolder(binding, itemClickCallback, favoriteClickCallback)
        }
    }

}