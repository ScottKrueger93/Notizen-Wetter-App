package com.example.abschlussprojektscott.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.abschlussprojektscott.R
import com.example.abschlussprojektscott.data.MainViewModel
import com.example.abschlussprojektscott.data.model.Note
import com.example.abschlussprojektscott.databinding.TaskAddFragmentBinding
import java.util.Calendar

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var userTimePicker: String = ""
        var userDatePicker: String = ""

        var isTimeFormatAccepted = false
        var isDateFormatAccepted = false

        fun updateButtonState() {
            if (!isTimeFormatAccepted || !isDateFormatAccepted) {
                binding.btAddTask.isEnabled = false
                binding.btAddTask.alpha = 0.5f
            } else {
                binding.btAddTask.isEnabled = true
                binding.btAddTask.alpha = 1.0f
            }
        }

        updateButtonState()

        class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

            override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
                val c = Calendar.getInstance()
                val hour = c.get(Calendar.HOUR_OF_DAY)
                val minute = c.get(Calendar.MINUTE)

                return TimePickerDialog(
                    activity, this, hour, minute,
                    DateFormat.is24HourFormat(activity)
                )
            }

            override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
                userTimePicker = "$hourOfDay:$minute"
                binding.etTaskTime.setText(userTimePicker)
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
                binding.etTaskDate.setText(userDatePicker)
            }

        }

        binding.ibAddTime.setOnClickListener {
            binding.etTaskTime.text?.clear()
            TimePickerFragment().show(childFragmentManager, "timePicker")
        }

        binding.ibAddCalender.setOnClickListener {
            binding.etTaskDate.text?.clear()
            val newFragment = DatePickerFragment()
            newFragment.show(childFragmentManager, "datePicker")
        }

        binding.etTaskTime.addTextChangedListener {
            var checkTime = viewModel.validateTimeFormat(binding.etTaskTime.text.toString())
            if (checkTime == "Wrong Input") {
                binding.etTaskTime.error = checkTime
                isTimeFormatAccepted = false
            } else {
                isTimeFormatAccepted = true
            }
            updateButtonState()
        }

        binding.etTaskDate.addTextChangedListener {
            var checkDate = viewModel.validateDateFormat(binding.etTaskDate.text.toString())
            if (checkDate == "Wrong Input") {
                binding.etTaskDate.error = checkDate
                isDateFormatAccepted = false
            } else {
                isDateFormatAccepted = true
            }
            updateButtonState()
        }


        viewModel.notes.observe(viewLifecycleOwner) {
            binding.btAddTask.setOnClickListener {

                var date: String
                var time: String
                var description = binding.etTaskDescription.text.toString()
                var name = binding.etTaskTitle.text.toString()

                if (binding.etTaskDate.text.isEmpty()) {
                    date = userDatePicker
                } else {
                    date = binding.etTaskDate.text.toString()
                }

                if (binding.etTaskTime.text.isEmpty()) {
                    time = userTimePicker
                } else {
                    time = binding.etTaskTime.text.toString()
                }
                
                var note = Note(
                    name = name,
                    date = date,
                    time = time,
                    description = description
                )

                viewModel.insertNote(note)

                binding.etTaskTitle.text.clear()
                binding.etTaskDate.text?.clear()
                binding.etTaskDescription.text.clear()
                binding.etTaskTime.text?.clear()
            }
        }
    }

}


