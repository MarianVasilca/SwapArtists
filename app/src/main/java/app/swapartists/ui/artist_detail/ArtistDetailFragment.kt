package app.swapartists.ui.artist_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import app.swapartists.R
import app.swapartists.data.model.ArtistDetails
import app.swapartists.data.model.ArtistRelease
import app.swapartists.databinding.FragmentArtistDetailBinding
import app.swapartists.utilities.extension.loadImage
import app.swapartists.utilities.extension.visibleIf
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ArtistDetailFragment : Fragment() {

    private var optionalBinding: FragmentArtistDetailBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = optionalBinding!!

    private val viewModel: ArtistDetailViewModel by viewModels()
    private val args: ArtistDetailFragmentArgs by navArgs()

    private val adapter by lazy { ArtistReleaseAdapter() }

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
        handleArgs()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        optionalBinding = null
    }

    private fun setupSubscribers() {
        subscribeToViewModelEvents()
        subscribeToUiEvents()
    }

    private fun subscribeToViewModelEvents() {
        viewModel.isLoading.observe(viewLifecycleOwner, ::setIsLoadingVisibility)
        viewModel.isError.observe(viewLifecycleOwner) { onErrorResponse() }
        viewModel.artist.observe(viewLifecycleOwner) { setArtistUi(it) }

        lifecycleScope.launchWhenCreated {
            viewModel.favoriteArtist.collectLatest {
                binding.cbFavorite.isChecked = it != null
            }
        }
    }

    private fun subscribeToUiEvents() {
        binding.cbFavorite.setOnClickListener { viewModel.onFavoriteClick() }
    }

    private fun setupUI() {
        binding.rvReleases.adapter = adapter
    }

    private fun createBinding(inflater: LayoutInflater, container: ViewGroup?) {
        optionalBinding = FragmentArtistDetailBinding.inflate(inflater, container, false)
    }

    private fun handleArgs() {
        viewModel.setArtistID(artistID = args.artistID)
    }

    private fun setIsLoadingVisibility(isLoading: Boolean) {
        binding.loadingProgressBar.visibleIf(isLoading)
    }

    private fun onErrorResponse() {
        Snackbar.make(requireView(), R.string.error_results_text, Snackbar.LENGTH_LONG)
            .show()
    }

    private fun setArtistUi(artist: ArtistDetails?) {
        binding.tvName.text = artist?.name ?: getString(R.string.artist_detail_name_unknown)

        binding.tvDisambiguation.text = artist?.disambiguation
        binding.tvDisambiguation.visibleIf(!artist?.disambiguation.isNullOrBlank())

        binding.tvRating.text = artist?.rating?.value?.toString()
            ?: getString(R.string.artist_detail_rating_unknown)

        loadImage(artist)
        setupAdapterData(artist?.releases?.nodes)
    }

    private fun setupAdapterData(nodes: List<ArtistRelease?>?) {
        val items = nodes?.filterNotNull() ?: emptyList()
        adapter.submitList(items)
    }

    private fun loadImage(artist: ArtistDetails?) {
        val uri = artist?.fanArt?.thumbnails?.firstNotNullOfOrNull { it?.url as? String }
        binding.ivHeader.loadImage(uri, R.drawable.ic_music_placeholder)
    }

}