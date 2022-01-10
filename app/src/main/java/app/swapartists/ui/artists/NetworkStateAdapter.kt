package app.swapartists.ui.artists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import app.swapartists.databinding.ItemPagingNetworkStateBinding
import app.swapartists.utilities.extension.visibleIf

class NetworkStateAdapter constructor(
    private val retryCallback: (() -> Unit)
) : LoadStateAdapter<NetworkStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): NetworkStateViewHolder {
        return NetworkStateViewHolder.create(parent, retryCallback)
    }

    override fun onBindViewHolder(
        holder: NetworkStateViewHolder,
        loadState: LoadState
    ) {
        holder.bind(loadState)
    }
}

class NetworkStateViewHolder(
    private val binding: ItemPagingNetworkStateBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(loadState: LoadState) {
        binding.pbLoading.visibleIf(loadState is LoadState.Loading)
        binding.bRetry.visibleIf(loadState is LoadState.Error)
    }

    companion object {
        fun create(
            parent: ViewGroup,
            retryCallback: (() -> Unit)
        ): NetworkStateViewHolder {

            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemPagingNetworkStateBinding.inflate(
                inflater, parent, false
            )

            binding.bRetry.setOnClickListener { retryCallback.invoke() }

            return NetworkStateViewHolder(binding)
        }
    }
}