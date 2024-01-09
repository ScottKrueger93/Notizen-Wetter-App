package com.example.abschlussprojektscott.ui

import android.os.Build
import android.os.Bundle
import android.text.method.TextKeyListener.clear
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
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
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
        viewModel.getWeatherData()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.notes.observe(viewLifecycleOwner) { notes ->
            notes?.let {
                val currentLocalDateTime = LocalDateTime.now()

                var lastTasks =
                    it.filter { parseDateTime(it.noteDate + " " + it.noteTime) <= currentLocalDateTime }
                        .sortedBy { parseDateTime(it.noteDate + " " + it.noteTime) }


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

                var nextTask =
                    it.filter { parseDateTime(it.noteDate + " " + it.noteTime) > currentLocalDateTime }
                        .sortedBy { parseDateTime(it.noteDate + " " + it.noteTime) }

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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseDateTime(dateTimeString: String): LocalDateTime {
        val patterns = arrayOf(
            "dd.MM.yyyy HH:mm",
            "dd.MM.yyyy H:mm",
            "dd.MM.yyyy HH:mm:ss",
            "dd.MM.yyyy H:mm:ss",
            "yyyy-MM-dd HH:mm",
            "yyyy-MM-dd H:mm",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd H:mm:ss",
            "MM/dd/yyyy HH:mm",
            "MM/dd/yyyy H:mm",
            "MM/dd/yyyy HH:mm:ss",
            "MM/dd/yyyy H:mm:ss",
        )

        for (pattern in patterns) {
            try {
                return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern(pattern))
            } catch (e: DateTimeParseException) {
                // Ignorieren und mit dem nächsten Muster versuchen
            }
        }

        // Standardwert, wenn keine der Formate passt (könnte eine Ausnahmebehandlung oder ein anderer Standardwert sein)
        return LocalDateTime.now()
    }
}