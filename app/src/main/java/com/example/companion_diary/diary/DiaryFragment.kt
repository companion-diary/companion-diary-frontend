package com.example.companion_diary.diary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.companion_diary.databinding.FragmentDiaryBinding

class DiaryFragment : Fragment() {

    lateinit var binding: FragmentDiaryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiaryBinding.inflate(inflater, container, false)
        initRv()
        return binding.root
    }

    /**
     * recyclerView 설정
     **/
    fun initRv() {
        val monthListManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        val monthListAdapter = DiaryMonthRVAdapter()
        binding.diaryMonthRv.apply{
            layoutManager = monthListManager
            adapter = monthListAdapter
            scrollToPosition(Int.MAX_VALUE/2)
        }
        val snap = PagerSnapHelper()
        snap.attachToRecyclerView(binding.diaryMonthRv)
    }
}