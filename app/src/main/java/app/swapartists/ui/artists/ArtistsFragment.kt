package app.swapartists.ui.artists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import app.swapartists.databinding.FragmentArtistsBinding
import app.swapartists.utilities.extension.visibleIf
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest

@ExperimentalCoroutinesApi
@FlowPreview
@AndroidEntryPoint
class ArtistsFragment : Fragment() {

    private var optionalBinding: FragmentArtistsBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = optionalBinding!!

    private val viewModel: ArtistsViewModel by viewModels()

    private val pagedAdapter by lazy { ArtistPagingAdapter() }
    private val footerAdapter: NetworkStateAdapter by lazy {
        NetworkStateAdapter { pagedAdapter.retry() }
    }
    private val concatAdapter: ConcatAdapter by lazy {
        pagedAdapter.withLoadStateFooter(footerAdapter)
    }

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
        setupUI()
        setupSubscribers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        optionalBinding = null
    }

    private fun setupUI() {
        binding.rvArtists.adapter = concatAdapter
    }

    private fun setupSubscribers() {
        subscribeToViewModelEvents()
    }

    private fun subscribeToViewModelEvents() {
        subscribeToItemsEvent()
        subscribeToLoadStateEvent()

        viewModel.isLoading.observe(viewLifecycleOwner, ::setIsLoadingVisibility)
        viewModel.infoMessageResID.observe(viewLifecycleOwner, ::showMessage)
    }

    private fun subscribeToLoadStateEvent() {
        lifecycleScope.launchWhenCreated {
            pagedAdapter.loadStateFlow.collectLatest { loadStates ->
                viewModel.onLoadStatesUpdated(loadStates)
            }
        }
    }

    private fun subscribeToItemsEvent() {
        lifecycleScope.launchWhenCreated {
            viewModel.artists.collectLatest {
                pagedAdapter.submitData(it)
            }
        }
    }

    private fun setIsLoadingVisibility(isLoading: Boolean) {
        binding.loadingProgressBar.visibleIf(isLoading)
    }

    private fun createBinding(inflater: LayoutInflater, container: ViewGroup?) {
        optionalBinding = FragmentArtistsBinding.inflate(inflater, container, false)
    }

    private fun showMessage(@StringRes resID: Int) {
        Snackbar.make(requireView(), resID, Snackbar.LENGTH_LONG).show()
    }
}