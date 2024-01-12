package com.example.abschlussprojektscott.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.abschlussprojektscott.R
import com.example.abschlussprojektscott.data.MainViewModel
import com.example.abschlussprojektscott.data.model.Note
import com.example.abschlussprojektscott.databinding.TaskAddFragmentBinding
import java.text.DateFormat
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
//
//            override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
//                // Use the current time as the default values for the picker.
//                val c = Calendar.getInstance()
//                val hour = c.get(Calendar.HOUR_OF_DAY)
//                val minute = c.get(Calendar.MINUTE)
//
//                // Create a new instance of TimePickerDialog and return it.
//                return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
//            }
//
//            override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
//                // Do something with the time the user picks.
//            }
//        }


//        class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
//
//            override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
//                // Use the current date as the default date in the picker.
//                val c = Calendar.getInstance()
//                val year = c.get(Calendar.YEAR)
//                val month = c.get(Calendar.MONTH)
//                val day = c.get(Calendar.DAY_OF_MONTH)
//
//                // Create a new instance of DatePickerDialog and return it.
//                return DatePickerDialog(requireContext(), this, year, month, day)
//
//            }
//
//            override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
//                // Do something with the date the user picks.
//            }
//
//        }




        viewModel.notes.observe(viewLifecycleOwner) {
            binding.btAddTask.setOnClickListener {

                var name = binding.etTaskTitle.text.toString()
                var date = binding.etTaskDate.text.toString()
                var time = binding.etTaskTime.text.toString()
                var description = binding.etTaskDescription.text.toString()
                var note = Note(
                    name = name,
                    date = date,
                    time = time,
                    description = description
                )

                viewModel.insertNote(note)

                binding.etTaskTitle.text.clear()
                binding.etTaskDate.text.clear()
                binding.etTaskDescription.text.clear()
                binding.etTaskTime.text.clear()
            }
        }
    }

}

