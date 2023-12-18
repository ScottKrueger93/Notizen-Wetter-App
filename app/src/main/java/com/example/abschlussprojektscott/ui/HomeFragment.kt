package com.example.abschlussprojektscott.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.abschlussprojektscott.data.MainViewModel
import com.example.abschlussprojektscott.databinding.HomeFragmentBinding
import com.example.abschlussprojektscott.databinding.ItemTaskrvBinding

class HomeFragment: Fragment() {

    private lateinit var binding : HomeFragmentBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(layoutInflater)
        //viewModel.getWeatherResult()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}