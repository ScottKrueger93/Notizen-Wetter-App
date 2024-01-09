package com.example.abschlussprojektscott.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.abschlussprojektscott.data.MainViewModel
import com.example.abschlussprojektscott.data.model.Note
import com.example.abschlussprojektscott.databinding.TaskAddFragmentBinding
import com.example.abschlussprojektscott.databinding.TaskEditFragmentBinding

class TaskEditFragment : Fragment() {

    private lateinit var binding: TaskEditFragmentBinding
    private val viewModel: MainViewModel by activityViewModels()

    private var noteId: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        arguments?.let { bundle ->
            val args = TaskEditFragmentArgs.fromBundle(bundle)
            noteId = args.detailNoteId
            Log.d("TaskEditFragment", "Received noteId: $noteId")
        }
        binding = TaskEditFragmentBinding.inflate(layoutInflater)
        viewModel.getWeatherData()
        viewModel.getNotes()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectedNote.observe(viewLifecycleOwner) {
            Log.d("TaskEditFragment", "Observed updated notes: $it")
            binding.etTaskTitleEdit.setText(it.noteName)
            binding.etTaskDescriptionEdit.setText(it.noteDescription)
            binding.etTaskDateEdit.setText(it.noteDate)
            binding.etTaskTimeEdit.setText(it.noteTime)

            binding.btDeleteTask.setOnClickListener {
                Log.d("TaskEditFragment", "Deleting note with ID: $noteId")
                viewModel.deleteNoteById(noteId)
                findNavController().navigate(TaskEditFragmentDirections.actionTaskEditFragmentToToDoFragment())
            }

            binding.btApply.setOnClickListener {
                var name = binding.etTaskTitleEdit.text.toString()
                var date = binding.etTaskDateEdit.text.toString()
                var time = binding.etTaskTimeEdit.text.toString()
                var description = binding.etTaskDescriptionEdit.text.toString()
                var note = Note(
                    id = noteId,
                    name = name,
                    date = date,
                    time = time,
                    description = description
                )

                viewModel.updateNote(note)
            }
        }
    }
}