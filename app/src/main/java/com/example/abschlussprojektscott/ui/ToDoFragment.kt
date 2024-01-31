package com.example.abschlussprojektscott.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.abschlussprojektscott.data.MainViewModel
import com.example.abschlussprojektscott.data.adapter.ToDoAdapter
import com.example.abschlussprojektscott.data.model.Notes
import com.example.abschlussprojektscott.databinding.TodoFragmentBinding

class ToDoFragment : Fragment() {

    private lateinit var binding: TodoFragmentBinding
    private val viewModel: MainViewModel by activityViewModels()
    private var isDescendingOrder = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TodoFragmentBinding.inflate(layoutInflater)
        viewModel.getNotes()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.notes.observe(viewLifecycleOwner) { notes ->
            binding.rvTodo.adapter = ToDoAdapter(notes, viewModel)

            binding.btSortButton.setOnClickListener {
                viewModel.notes.value?.let { currentNotes ->
                    val sortedNotes = if (isDescendingOrder) {
                        currentNotes.sortedWith(
                            compareByDescending<Notes> {
                                it.noteDate
                            }.thenByDescending {
                                it.noteTime
                            }
                        )
                    } else {
                        currentNotes.sortedWith(
                            compareBy<Notes> {
                                it.noteDate
                            }.thenBy {
                                it.noteTime
                            }
                        )
                    }
                    isDescendingOrder = !isDescendingOrder
                    binding.rvTodo.adapter = ToDoAdapter(sortedNotes, viewModel)
                }
            }
        }
    }
}
