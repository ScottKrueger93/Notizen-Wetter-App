package com.example.abschlussprojektscott.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
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
import com.example.abschlussprojektscott.data.MainActivity
import com.example.abschlussprojektscott.data.MainViewModel
import com.example.abschlussprojektscott.data.model.Note
import com.example.abschlussprojektscott.databinding.TaskAddFragmentBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Calendar

class TaskAddFragment : Fragment() {

    private lateinit var binding: TaskAddFragmentBinding
    private lateinit var activity: MainActivity
    private lateinit var locationService: FusedLocationProviderClient
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TaskAddFragmentBinding.inflate(layoutInflater)
        activity = requireActivity() as MainActivity
        locationService = LocationServices.getFusedLocationProviderClient(requireActivity())
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var userTimePicker = ""
        var userDatePicker = ""

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
                userDatePicker = "$day.${month + 1}.$year"
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
            val checkTime = viewModel.validateTimeFormat(binding.etTaskTime.text.toString())
            if (checkTime == "Wrong Input") {
                binding.etTaskTime.error = checkTime
                isTimeFormatAccepted = false
            } else {
                isTimeFormatAccepted = true
            }
            updateButtonState()
        }

        binding.etTaskDate.addTextChangedListener {
            val checkDate = viewModel.validateDateFormat(binding.etTaskDate.text.toString())
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

                if (activity.checkLocationPermission()) {
                    locationService.lastLocation.addOnSuccessListener { location: Location ->
                        viewModel.getWeatherData(location.latitude, location.longitude)
                        Log.e("LOCATION", "${location.latitude} ${location.longitude}")

                    }
                } else {
                    Log.e("PERMISSION", "DENIED")
                }

                val description = binding.etTaskDescription.text.toString()
                val name = binding.etTaskTitle.text.toString()

                val date: String = if (binding.etTaskDate.text.isEmpty()) {
                    userDatePicker
                } else {
                    binding.etTaskDate.text.toString()
                }

                val time: String = if (binding.etTaskTime.text.isEmpty()) {
                    userTimePicker
                } else {
                    binding.etTaskTime.text.toString()
                }

                val note = Note(
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


