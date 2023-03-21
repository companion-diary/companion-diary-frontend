package com.example.companion_diary.diary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.companion_diary.MainActivity
import com.example.companion_diary.diary.utils.CalendarUtils.Companion.getMonthList
import com.example.companion_diary.R
import com.example.companion_diary.databinding.FragmentCalendarBinding
import com.example.companion_diary.diary.entities.Date
import com.example.companion_diary.diary.network.DiaryApi
import com.example.companion_diary.diary.network.DiaryClient
import kotlinx.coroutines.delay
import org.joda.time.DateTime
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class CalendarFragment: Fragment() {

    private var millis: Long = 0L
    lateinit var binding : FragmentCalendarBinding
    val TAG = "CalendarFragment"

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
        binding.itemMonthLayout.setBackgroundResource(R.drawable.border_diary_layout_unselected)
        when(month.toInt()%2){
            0 -> {
                context?.getColor(R.color.main_color_orange)
                    ?.let { binding.monthTv.setTextColor(it) }
            }
            else -> {
                context?.getColor(R.color.main_color_green)
                    ?.let { binding.monthTv.setTextColor(it) }
            }
        }

        /**
         * 여기다가 매개변수로 서버에서 받은 날짜 리스트 넘겨주기
         * 그리고 calendarView에서 DayItemView로 매개변수 넘겨줘서 CalendarUtils에서 확인후 뷰그리기
         * 앞 뒤로 두개씩 총 5개 미리 렌더링
         */

        loadDateList(DateTime(millis))
        return binding.root
    }

    private fun loadDateList(firstDayOfMonth: DateTime) {
        val startDate = firstDayOfMonth.toString().substring(0,8)+"01"
        val endDate = getEndDate(startDate)
        val call: Call<Date> = DiaryClient.diaryService.getDateList(startDate, endDate)
        call.enqueue(object: Callback<Date> {
            override fun onResponse(call: Call<Date>, response: Response<Date>) {
                if(response.isSuccessful){
                    val dateResult: Date = response.body()!!
                    if(dateResult.isSuccess){
                        Log.d(TAG,"onResponse success")
                        if (dateResult != null) {
                            initCal(dateResult.result)
                        }
                    } else{
                        showAlertDialog(dateResult.message)
                    }
                } else {
                    showAlertDialog(response.message())
                }
            }

            override fun onFailure(call: Call<Date>, t: Throwable) {
                showAlertDialog(t.message.toString())
            }
        })

    }

    private fun showAlertDialog(message: String) {
        context?.let {
            AlertDialog.Builder(it)
                .setTitle("오류 발생")
                .setMessage(message)
                .setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }

    /**
     * 각 월의 endDate return
     */
    private fun getEndDate(date: String): String{
        val year = date.substring(0,4).toInt()
        val month = date.substring(5,7)

        var day = when(month.toInt()){
            2 -> "28"
            1,3,5,7,8,10,12 -> "31"
            else -> "30"
        }
        if(year%4 == 0 && month == "02") day ="29"
        return "$year-$month-$day"
    }

    private fun initCal(dateList:ArrayList<String>){
        binding.calendarView.initCalendar(DateTime(millis),getMonthList(DateTime(millis)),dateList)
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