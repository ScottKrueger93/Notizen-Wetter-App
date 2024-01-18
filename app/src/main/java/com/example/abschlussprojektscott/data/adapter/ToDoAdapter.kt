package com.example.abschlussprojektscott.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.abschlussprojektscott.data.MainViewModel
import com.example.abschlussprojektscott.data.model.Notes
import com.example.abschlussprojektscott.data.remote.IMAGE_BASE_URL
import com.example.abschlussprojektscott.data.remote.IMG_URL_LAST
import com.example.abschlussprojektscott.databinding.ItemTasktodoBinding
import com.example.abschlussprojektscott.ui.ToDoFragmentDirections

class ToDoAdapter(
    private val dataset: List<Notes>,
    private val viewModel: MainViewModel
) : RecyclerView.Adapter<ToDoAdapter.NoteViewHolder>() {
    inner class NoteViewHolder(val binding: ItemTasktodoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemTasktodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val item = dataset[position]

        holder.binding.tvTitlePlaceHolderToDo.text = item.noteName
        holder.binding.tvDatePlaceholderToDo.text = item.noteDate
        holder.binding.ivWeatherIconToDo.load(IMAGE_BASE_URL + item.weatherIcon + IMG_URL_LAST)
        holder.binding.tvTimePlaceholderToDo.text = item.noteTime

        holder.itemView.setOnClickListener {
            it.findNavController().navigate(ToDoFragmentDirections.actionToDoFragmentToTaskDetailFragment(item.id))
        }
    }
}