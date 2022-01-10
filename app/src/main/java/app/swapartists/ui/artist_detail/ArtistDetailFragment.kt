package app.swapartists.ui.artist_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import app.swapartists.databinding.FragmentArtistDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArtistDetailFragment : Fragment() {

    private var optionalBinding: FragmentArtistDetailBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = optionalBinding!!

    private val viewModel: ArtistDetailViewModel by viewModels()
    private val args: ArtistDetailFragmentArgs by navArgs()

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
        handleArgs()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        optionalBinding = null
    }

    private fun setupSubscribers() {
        subscribeToViewModelEvents()
    }

    private fun subscribeToViewModelEvents() {
    }

    private fun createBinding(inflater: LayoutInflater, container: ViewGroup?) {
        optionalBinding = FragmentArtistDetailBinding.inflate(inflater, container, false)
    }

    private fun handleArgs() {
        viewModel.setArtistID(artistID = args.artistID)
    }
}