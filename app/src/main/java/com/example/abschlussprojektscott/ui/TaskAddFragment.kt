package com.example.abschlussprojektscott.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.abschlussprojektscott.data.MainViewModel
import com.example.abschlussprojektscott.data.model.Notes
import com.example.abschlussprojektscott.databinding.TaskAddFragmentBinding

class TaskAddFragment: Fragment() {

    private lateinit var binding : TaskAddFragmentBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TaskAddFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var name = binding.etTaskTitle.text
        var date = binding.etTaskDate.text
        var description = binding.etTaskDescription.text



        var note = Notes(id = 0, name,date,description, )

        binding.btAddTask.setOnClickListener {


            //viewModel.insertNote()
        }
    }

}