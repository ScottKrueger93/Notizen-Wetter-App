package com.example.abschlussprojektscott.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.load
import com.example.abschlussprojektscott.data.MainViewModel
import com.example.abschlussprojektscott.data.remote.IMAGE_BASE_URL
import com.example.abschlussprojektscott.data.remote.IMG_URL_LAST
import com.example.abschlussprojektscott.databinding.HomeFragmentBinding
import java.time.LocalDateTime

class HomeFragment : Fragment() {

    private lateinit var binding: HomeFragmentBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(layoutInflater)
        viewModel.getNotes()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.notes.observe(viewLifecycleOwner) { notes ->
            notes?.let {
                val currentLocalDateTime = LocalDateTime.now()

                val lastTasks =
                    it.filter { viewModel.parseDateTime(it.noteDate + " " + it.noteTime) <= currentLocalDateTime }
                        .sortedBy { viewModel.parseDateTime(it.noteDate + " " + it.noteTime) }


                if (lastTasks.isNotEmpty()) {
                    binding.includeLastTask.tvTitlePlaceHolder.text = lastTasks.last().noteName
                    binding.includeLastTask.tvDatePlaceholder.text = lastTasks.last().noteDate
                    binding.includeLastTask.tvTimePlaceholder.text = lastTasks.last().noteTime
                    binding.includeLastTask.tvDescriptionPlaceholder.text =
                        lastTasks.last().noteDescription
                    binding.includeLastTask.tvWeather.text = lastTasks.last().weatherName
                    binding.includeLastTask.tvWeatherDescription.text =
                        lastTasks.last().weatherDescription
                    binding.includeLastTask.ivWeatherIcon.load(IMAGE_BASE_URL + lastTasks.last().weatherIcon + IMG_URL_LAST)
                } else {
                    binding.includeLastTask.cvItem.visibility = View.GONE
                }

                val nextTask =
                    it.filter { viewModel.parseDateTime(it.noteDate + " " + it.noteTime) > currentLocalDateTime }
                        .sortedBy { viewModel.parseDateTime(it.noteDate + " " + it.noteTime) }

                if (nextTask.isNotEmpty()) {
                    binding.includeNewestTask.tvTitlePlaceHolder.text = nextTask[0].noteName
                    binding.includeNewestTask.tvDatePlaceholder.text = nextTask[0].noteDate
                    binding.includeNewestTask.tvTimePlaceholder.text = nextTask[0].noteTime
                    binding.includeNewestTask.tvDescriptionPlaceholder.text =
                        nextTask[0].noteDescription
                    binding.includeNewestTask.tvWeather.text = nextTask[0].weatherName
                    binding.includeNewestTask.tvWeatherDescription.text =
                        nextTask[0].weatherDescription
                    binding.includeNewestTask.ivWeatherIcon.load(IMAGE_BASE_URL + nextTask[0].weatherIcon + IMG_URL_LAST)
                } else {
                    binding.includeNewestTask.cvItem.visibility = View.GONE
                }

            }
        }
    }

}