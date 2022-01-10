package app.swapartists.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import app.swapartists.databinding.FragmentFavoritesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var optionalBinding: FragmentFavoritesBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = optionalBinding!!

    private val viewModel: FavoritesViewModel by viewModels()
    private val adapter by lazy { FavoriteArtistAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        createBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSubscribers()
        setupUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        optionalBinding = null
    }

    private fun createBinding(inflater: LayoutInflater, container: ViewGroup?) {
        optionalBinding = FragmentFavoritesBinding.inflate(inflater, container, false)
    }

    private fun setupUI() {
        binding.rvFavoriteArtists.adapter = adapter
    }

    private fun setupSubscribers() {
        subscribeToViewModelEvents()
    }

    private fun subscribeToViewModelEvents() {
        subscribeToItemsEvent()
    }

    private fun subscribeToItemsEvent() {
        lifecycleScope.launchWhenCreated {
            viewModel.items.collectLatest {
                adapter.submitList(it)
            }
        }
    }
}