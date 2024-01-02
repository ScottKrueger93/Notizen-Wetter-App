package com.example.abschlussprojektscott.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.load
import com.example.abschlussprojektscott.data.MainViewModel
import com.example.abschlussprojektscott.data.remote.BASE_URL
import com.example.abschlussprojektscott.data.remote.IMG_URL
import com.example.abschlussprojektscott.data.remote.IMG_URL_LAST
import com.example.abschlussprojektscott.databinding.HomeFragmentBinding
import java.util.Calendar
import java.util.Date

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentTime: Date = Calendar.getInstance().time

        viewModel.notes.observe(viewLifecycleOwner) {

            if (it.isNotEmpty()) {
                var lastTask = it.filter { it.noteDate < currentTime.toString() }
                lastTask = lastTask.filter { it.noteTime < currentTime.toString() }

                if (lastTask.isNotEmpty()) {
                    binding.includeLastTask.tvTitlePlaceHolder.text = lastTask[0].noteName
                    binding.includeLastTask.tvDatePlaceholder.text = lastTask[0].noteDate
                    binding.includeLastTask.tvTimePlaceholder.text = lastTask[0].noteTime
                    binding.includeLastTask.tvDescriptionPlaceholder.text =
                        lastTask[0].noteDescription
                    binding.includeLastTask.tvWeather.text = lastTask[0].weatherName
                    binding.includeLastTask.tvWeatherDescription.text =
                        lastTask[0].weatherDescription
                    //binding.includeLastTask.ivWeatherIcon.setImageIcon(lastTask[0].weatherIcon)
                    binding.includeNewestTask.ivWeatherIcon.load(BASE_URL + IMG_URL + lastTask[0].weatherIcon + IMG_URL_LAST)
                } else {
                    binding.includeLastTask.cvItem.visibility = View.GONE
                }

                var nextTask = it.filter { it.noteDate > currentTime.toString() }
                nextTask = nextTask.filter { it.noteTime > currentTime.toString() }

                if (nextTask.isNotEmpty()) {
                    binding.includeNewestTask.tvTitlePlaceHolder.text = nextTask[0].noteName
                    binding.includeNewestTask.tvDescriptionPlaceholder.text =
                        nextTask[0].noteDescription
                    binding.includeNewestTask.tvDatePlaceholder.text = nextTask[0].noteDate
                    binding.includeNewestTask.tvTimePlaceholder.text = nextTask[0].noteTime
                    binding.includeNewestTask.tvWeather.text = nextTask[0].weatherName
                    binding.includeNewestTask.tvWeatherDescription.text =
                        nextTask[0].weatherDescription
                    //binding.includeLastTask.ivWeatherIcon.setImageIcon(lastTask[0].weatherIcon)
                    binding.includeNewestTask.ivWeatherIcon.load(BASE_URL + IMG_URL + nextTask[0].weatherIcon + IMG_URL_LAST)
                } else {
                    binding.includeNewestTask.cvItem.visibility = View.GONE
                }
            } else {
                binding.includeLastTask.cvItem.visibility = View.GONE
                binding.includeNewestTask.cvItem.visibility = View.GONE
            }
        }
    }
}