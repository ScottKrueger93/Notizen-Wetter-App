package com.example.abschlussprojektscott.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.abschlussprojektscott.data.MainViewModel
import com.example.abschlussprojektscott.data.model.Note
import com.example.abschlussprojektscott.databinding.TaskEditFragmentBinding
import java.util.Calendar

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
        viewModel.getNotes()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var userTimePicker = ""
        var userDatePicker = ""

        var isTimeFormatAccepted = true
        var isDateFormatAccepted = true

        fun updateButtonState() {
            if (!isTimeFormatAccepted || !isDateFormatAccepted) {
                binding.btApply.isEnabled = false
                binding.btApply.alpha = 0.5f
            } else {
                binding.btApply.isEnabled = true
                binding.btApply.alpha = 1.0f
            }
        }

        updateButtonState()

        class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

            override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
                val c = Calendar.getInstance()
                val hour = c.get(Calendar.HOUR_OF_DAY)
                val minute = c.get(Calendar.MINUTE)

                return TimePickerDialog(activity, this, hour, minute,
                    DateFormat.is24HourFormat(activity)
                )
            }

            override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
                userTimePicker = "$hourOfDay:$minute"
                binding.etTaskTimeEdit.setText(userTimePicker)
            }
        }

        class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

            override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                return DatePickerDialog(requireContext(), this, year, month, day)
            }

            override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
                userDatePicker = "$day.${month+1}.$year"
                binding.etTaskDateEdit.setText(userDatePicker)
            }

        }

        binding.ibEditTime.setOnClickListener {
            binding.etTaskTimeEdit.text?.clear()
            TimePickerFragment().show(childFragmentManager, "timePicker")
        }

        binding.ibEditCalender.setOnClickListener {
            binding.etTaskDateEdit.text?.clear()
            val newFragment = DatePickerFragment()
            newFragment.show(childFragmentManager, "datePicker")
        }

        binding.etTaskTimeEdit.addTextChangedListener {
            val checkTime = viewModel.validateTimeFormat(binding.etTaskTimeEdit.text.toString())
            if (checkTime == "Wrong Input") {
                binding.etTaskTimeEdit.error = checkTime
                isTimeFormatAccepted = false
            } else {
                isTimeFormatAccepted = true
            }
            updateButtonState()
        }

        binding.etTaskDateEdit.addTextChangedListener {
            val checkDate = viewModel.validateDateFormat(binding.etTaskDateEdit.text.toString())
            if (checkDate == "Wrong Input") {
                binding.etTaskDateEdit.error = checkDate
                isDateFormatAccepted = false
            } else {
                isDateFormatAccepted = true
            }
            updateButtonState()
        }

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

                val description = binding.etTaskDescriptionEdit.text.toString()
                val name = binding.etTaskTitleEdit.text.toString()

                val date: String = if (binding.etTaskDateEdit.text.isEmpty()){
                    userDatePicker
                } else {
                    binding.etTaskDateEdit.text.toString()
                }

                val time: String = if (binding.etTaskTimeEdit.text.isEmpty()){
                    userTimePicker
                } else {
                    binding.etTaskTimeEdit.text.toString()
                }

                val note = Note(
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