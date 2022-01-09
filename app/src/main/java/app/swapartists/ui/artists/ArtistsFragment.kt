package app.swapartists.ui.artists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import app.swapartists.databinding.FragmentArtistsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArtistsFragment : Fragment() {

    private var optionalBinding: FragmentArtistsBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = optionalBinding!!

    private val viewModel: ArtistsViewModel by viewModels()

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
        optionalBinding = FragmentArtistsBinding.inflate(inflater, container, false)
    }
}