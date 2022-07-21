package com.example.companion_diary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.companion_diary.databinding.FragmentFiveBinding

class ProfileFragment : Fragment() {

    lateinit var binding: FragmentFiveBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFiveBinding.inflate(inflater, container, false)

        return binding.root
    }
}