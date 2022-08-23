package com.example.companion_diary.diary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.companion_diary.diary.utils.CalendarUtils.Companion.getMonthList
import com.example.companion_diary.R
import com.example.companion_diary.databinding.FragmentCalendarBinding
import org.joda.time.DateTime

class CalendarFragment: Fragment() {

    private var millis: Long = 0L
    lateinit var binding : FragmentCalendarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let{
            millis = it.getLong(MILLIS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCalendarBinding.inflate(inflater, container, false)

        val year = DateTime(millis).toString("yyyy")
        val month = DateTime(millis).toString("M")

        binding.yearTv.text = year
        binding.monthTv.text = month + "월"
        when(month.toInt()%2){
            0 -> {
                binding.itemMonthLayout.setBackgroundResource(R.drawable.border_diary_layout_orange)
                context?.getColor(R.color.main_color_orange)
                    ?.let { binding.monthTv.setTextColor(it) }
            }
            else -> {
                binding.itemMonthLayout.setBackgroundResource(R.drawable.border_diary_layout_green)
                context?.getColor(R.color.main_color_green)
                    ?.let { binding.monthTv.setTextColor(it) }
            }
        }

        binding.calendarView.initCalendar(DateTime(millis),getMonthList(DateTime(millis)))
        return binding.root
    }

    companion object {
        private const val MILLIS = "MILLIS"
        fun newInstance(millis: Long) = CalendarFragment().apply {
            arguments = Bundle().apply {
                putLong(MILLIS, millis)
            }
        }
    }
}