package com.example.abschlussprojektscott.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.abschlussprojektscott.data.MainViewModel
import com.example.abschlussprojektscott.data.model.Note
import com.example.abschlussprojektscott.databinding.TaskAddFragmentBinding

class TaskAddFragment : Fragment() {

    private lateinit var binding: TaskAddFragmentBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TaskAddFragmentBinding.inflate(layoutInflater)
        viewModel.getWeatherData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btAddTask.setOnClickListener {

            var name = binding.etTaskTitle.text.toString()
            var date = binding.etTaskDate.text.toString()
            var time = binding.etTaskTime.text.toString()
            var description = binding.etTaskDescription.text.toString()
            var note = Note(name, date, time, description)

            viewModel.insertNote(note)

            binding.etTaskTitle.text.clear()
            binding.etTaskDate.text.clear()
            binding.etTaskDescription.text.clear()
            binding.etTaskTime.text.clear()
        }
    }

}