package com.example.abschlussprojektscott.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.abschlussprojektscott.data.MainViewModel
import com.example.abschlussprojektscott.databinding.TaskAddFragmentBinding
import com.example.abschlussprojektscott.databinding.TaskEditFragmentBinding

class TaskEditFragment: Fragment() {

    private lateinit var binding : TaskEditFragmentBinding
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
        }
        binding = TaskEditFragmentBinding.inflate(layoutInflater)
        viewModel.getWeatherData()
        viewModel.getNotes()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectedNote.observe(viewLifecycleOwner){
            binding.etTaskTitleEdit.setText(it.noteName)
            binding.etTaskDescriptionEdit.setText(it.noteDescription)
            binding.etTaskDateEdit.setText(it.noteDate)
            binding.etTaskTimeEdit.setText(it.noteTime)
        }

        viewModel.notes.observe(viewLifecycleOwner){
            binding.btDeleteTask.setOnClickListener {
                //TODO: delete funktioniert nicht
                viewModel.deleteNote(noteId)
                findNavController().navigate(TaskEditFragmentDirections.actionTaskEditFragmentToToDoFragment())
            }
        }
    }

}