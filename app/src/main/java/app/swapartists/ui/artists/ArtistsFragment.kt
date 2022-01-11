package app.swapartists.ui.artists

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.annotation.StringRes
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import app.swapartists.R
import app.swapartists.data.model.ArtistModel
import app.swapartists.databinding.FragmentArtistsBinding
import app.swapartists.utilities.extension.visibleIf
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy

@ExperimentalCoroutinesApi
@FlowPreview
@AndroidEntryPoint
class ArtistsFragment : Fragment() {

    private var optionalBinding: FragmentArtistsBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = optionalBinding!!

    private val viewModel: ArtistsViewModel by viewModels()

    private val pagedAdapter by lazy { ArtistPagingAdapter(::onItemClick, ::onFavoriteClick) }
    private val footerAdapter: NetworkStateAdapter by lazy {
        NetworkStateAdapter { pagedAdapter.retry() }
    }
    private val concatAdapter: ConcatAdapter by lazy {
        pagedAdapter.withLoadStateFooter(footerAdapter)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.artists_menu, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = requireActivity()
            .getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            setupSearchViewTextListener(this)
        }

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
        subscribeToListSizeEvent()

        viewModel.isLoading.observe(viewLifecycleOwner, ::setIsLoadingVisibility)
        viewModel.infoMessageResID.observe(viewLifecycleOwner, ::showMessage)
        viewModel.isListEmpty.observe(viewLifecycleOwner) { binding.tvEmptyList.visibleIf(it) }
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
            viewModel.favoriteArtists.collectLatest {
                pagedAdapter.submitData(it)
            }
        }
    }

    private fun subscribeToListSizeEvent() {
        lifecycleScope.launchWhenCreated {
            pagedAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .collectLatest {
                    viewModel.setListCount(pagedAdapter.snapshot().size)
                }
        }
    }

    private fun onItemClick(item: ArtistModel) {
        val action = ArtistsFragmentDirections.actionArtistsToArtistDetails(item.artist.id)
        findNavController().navigate(action)
    }

    private fun onFavoriteClick(item: ArtistModel) {
        viewModel.onFavoriteClick(item.artist)
    }

    private fun setupSearchViewTextListener(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean = false
            override fun onQueryTextChange(text: String?): Boolean {
                viewModel.onQueryTextChanged(text)
                return true
            }
        })
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