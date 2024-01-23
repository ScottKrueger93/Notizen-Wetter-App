package com.example.abschlussprojektscott.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.abschlussprojektscott.data.MainViewModel
import com.example.abschlussprojektscott.data.remote.IMAGE_BASE_URL
import com.example.abschlussprojektscott.data.remote.IMG_URL_LAST
import com.example.abschlussprojektscott.databinding.TaskDetailFragmentBinding

class TaskDetailFragment : Fragment() {

    private lateinit var binding: TaskDetailFragmentBinding
    private val viewModel: MainViewModel by activityViewModels()

    private var notesId: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        arguments?.let { bundle ->
            val args = TaskDetailFragmentArgs.fromBundle(bundle)
            notesId = args.notesId
        }
        binding = TaskDetailFragmentBinding.inflate(layoutInflater)
        viewModel.getNotes()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSelectedNoteById(notesId)

        viewModel.selectedNote.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.tvTaskTitle.text = it.noteName
                binding.tvTaskDescription.text = it.noteDescription
                binding.tvTaskDueDate.text = it.noteDate
                binding.tvTaskTime.text = it.noteTime
                binding.tvWeather.text = it.weatherName
                binding.tvWeatherDescription.text = it.weatherDescription
                binding.ivWeatherIcon.load(IMAGE_BASE_URL + it.weatherIcon + IMG_URL_LAST)
            }
        }

        binding.btEdit.setOnClickListener {
            findNavController().navigate(
                TaskDetailFragmentDirections.actionTaskDetailFragmentToTaskEditFragment(
                    notesId
                )
            )
        }

    }
}