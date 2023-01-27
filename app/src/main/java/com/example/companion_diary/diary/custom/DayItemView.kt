package com.example.companion_diary.diary.custom


import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import com.example.companion_diary.R
import com.example.companion_diary.databinding.DialogDiaryDateSelectionDetailsBinding
import com.example.companion_diary.diary.entities.Date
import com.example.companion_diary.diary.entities.DiaryPreview
import com.example.companion_diary.diary.network.DiaryClient
import com.example.companion_diary.diary.utils.CalendarUtils.Companion.checkToday
import com.example.companion_diary.diary.utils.CalendarUtils.Companion.getDateColor
import com.example.companion_diary.diary.utils.CalendarUtils.Companion.isDiaryExist
import com.example.companion_diary.diary.utils.CalendarUtils.Companion.isSameMonth
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gun0912.tedpermission.provider.TedPermissionProvider
import org.joda.time.DateTime
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DayItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes private val defStyleAttr: Int = R.attr.itemViewStyle,
    @StyleRes private val defStyleRes: Int = R.style.Calendar_ItemViewStyle,
    private val date: DateTime = DateTime(),
    private val firstDayOfMonth: DateTime = DateTime(),
    private val dateList: ArrayList<String> = ArrayList<String>()
) : View(ContextThemeWrapper(context,defStyleRes),attrs,defStyleAttr) {

    private val bounds = Rect()
    private var textPaint: Paint = Paint()
    private var dotPaint: Paint = Paint()
    private var nameTagText: String = ""
    private var today : DateTime = DateTime()
    private val TAG = "DayItemView"

    init {
        context.withStyledAttributes(attrs, R.styleable.CalendarView, defStyleAttr, defStyleRes) {
            val dayTextSize =
                getDimensionPixelSize(R.styleable.CalendarView_dayTextSize, 22).toFloat()
            val todayTextSize =
                getDimensionPixelSize(R.styleable.CalendarView_todayTextSize,24).toFloat()
            val todayFont = ResourcesCompat.getFont(context,R.font.roboto_bold)
            val defaultFont = ResourcesCompat.getFont(context,R.font.roboto_medium)
            val isToday = checkToday(today,date)

            textPaint = TextPaint().apply {
                isAntiAlias = true
                typeface = defaultFont
                textSize = dayTextSize
                color = getDateColor(date.dayOfWeek)
                if (!isSameMonth(date, firstDayOfMonth)) {
                    visibility = GONE
                }
                if(isToday){
                    textSize = todayTextSize
                    typeface = todayFont
                }
            }
            dotPaint = Paint().apply {
                isAntiAlias = true
                color = when(date.monthOfYear%2){
                    0 -> context.getColor(R.color.main_color_orange)
                    else -> context.getColor(R.color.main_color_green)
                }
                strokeWidth = 20f
                strokeCap = Paint.Cap.ROUND
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return
        val dateStr = date.dayOfMonth.toString()
        textPaint.getTextBounds(dateStr, 0, dateStr.length, bounds)
        canvas.drawText(
            dateStr,
            (width / 2 - bounds.width() / 2).toFloat() - 2,
            (height / 2 + bounds.height() / 2).toFloat(),
            textPaint
        )

        /**
         * 일기가 존재하면 . 표시 item in array
         *
         */
        if(isDiaryExist(date, dateList)){
            canvas.drawPoint(
                (width / 2).toFloat(),
                ((height / 2) / 3).toFloat(),
                dotPaint
            )
        }

        /**
         * day item 클릭 시 bottomSheetDialog 띄우기
         */
        setOnClickListener {
            initBottomSheetDialog()
        }
    }

    private fun initBottomSheetDialog() {
        val dialog = BottomSheetDialog(context)
        val dialogBinding = DialogDiaryDateSelectionDetailsBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(dialogBinding.root)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.behavior.skipCollapsed = true
        dialogBinding.yearMonthDateTv.text = "${date.year}년 ${date.monthOfYear}월 ${date.dayOfMonth}일"
        Log.d(TAG,"${date.year}-${date.monthOfYear}-${date.dayOfMonth}")
        getDiaryList("2022-11-23") // TEST

        dialogBinding.prevIv.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun getDiaryList(date: String) {
        val call: Call<DiaryPreview> = DiaryClient.diaryService.getDiaryList(date)
        call.enqueue(object: Callback<DiaryPreview> {
            override fun onResponse(call: Call<DiaryPreview>, response: Response<DiaryPreview>) {
                if(response.isSuccessful){
                    val diaryResult: DiaryPreview = response.body()!!
                    if(diaryResult.isSuccess){
                        Log.d(TAG,"onResponse success")
                        if (diaryResult != null) {
                            Log.d(TAG,"${diaryResult}")
                        }
                    } else{
                        Log.d(TAG,"${diaryResult.message}")
                    }
                } else {
                    Log.d(TAG,"onResponse 실패")
                }
            }
            override fun onFailure(call: Call<DiaryPreview>, t: Throwable) {
                Log.d(TAG,"onFailure 예외: " + t.message)
            }
        })
    }


//    private fun initBottomSheetDialog(){
//
//        /**
//         * bottomSheetDialog
//         */
//        val dialog = BottomSheetDialog(context)
//        dialog.setContentView(R.layout.dialog_write_diary)
//        val dateTv = dialog.findViewById<TextView>(R.id.year_month_date_tv)
//        val prevBtn = dialog.findViewById<ImageView>(R.id.prev_iv)
//        val writeDiaryBtn = dialog.findViewById<Button>(R.id.write_diary_btn)
//        val companionRv = dialog.findViewById<RecyclerView>(R.id.companion_name_tag_rv)
//        val diaryRv = dialog.findViewById<RecyclerView>(R.id.diary_rv)
//        var nameTagList = ArrayList<String>()
//        var diaryList = ArrayList<String>()
//
//        /**
//         * 날짜
//         */
//        val year = date.year.toString()
//        val monthOfYear = date.monthOfYear.toString()
//        val dayOfMonth = date.dayOfMonth.toString()
//        var dateStr = year + "년 " + monthOfYear + "월 " + dayOfMonth + "일"
//        dateTv?.text = dateStr
//
//        /**
//         * Dummy data
//         */
//        nameTagList.apply{
//            add("otherAnimal")
//            add("otherPlant")
//            add("호두")
//            add("제임스")
//            add("연두")
//            add("밀키")
//        }
//
//        /**
//         * 이름 태그 recyclerView 설정
//         */
//        var nameTagListAdapter = NameTagRVAdapter(nameTagList)
//        var nameTagListManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
//        companionRv?.apply{
//            adapter = nameTagListAdapter
//            layoutManager = nameTagListManager
//            addItemDecoration(DiaryItemDecoration(context, 23f))
//        }
//        nameTagListAdapter.setMyItemSelectedListener(object: NameTagRVAdapter.MyItemSelectedListener{
//            override fun onItemSelected(nameTag: String) {
//                nameTagText = nameTag
//            }
//        })
//
//        /**
//         * 날짜에 맞는 일기 리스트 전부 받아오기
//         * 현재는 더미데이터
//         */
//
//        diaryList.apply{
//            add("호두")
//            add("제임스")
//            add("연두")
//            add("밀키")
//        }
//        var diaryListAdapter = DiaryRVAdater(diaryList,context)
//        var diaryListManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
//        diaryRv?.apply{
//            adapter = diaryListAdapter
//            layoutManager = diaryListManager
//            addItemDecoration(DiaryItemDecoration(context,10f))
//        }
//
//
//
//        /**
//         * 일기 쓰기 버튼 클릭 시 일기 쓰기 화면으로 이동
//         */
//        prevBtn?.setOnClickListener {
//            nameTagText = ""
//            dialog.dismiss()
//        }
//        dialog.setOnDismissListener {
//            nameTagText = ""
//        }
//        writeDiaryBtn?.setOnClickListener {
//            var intent = Intent(context, WriteDiaryActivity::class.java)
//            if(nameTagText == ""){
////                Toast.makeText(context, "어떤 친구의 하루를 작성할지 선택해주세요",Toast.LENGTH_SHORT).show()
//                nameTagText = "Other"
//            }
//            if(nameTagText =="otherAnimal"|| nameTagText =="otherPlant"){ // 삭제
//                nameTagText = "Other"
//            }
//            intent.putExtra("dayOfMonth", dayOfMonth)
//            intent.putExtra("monthOfYear", monthOfYear)
//            intent.putExtra("year", year)
//            intent.putExtra("nameTag",nameTagText)
//            context.startActivity(intent)
//            dialog.dismiss()
//            /**
//             * 동식물 구분해서 일기 작성 페이지에서 색상 다르게 나오도록
//             * other 태그 아니면 사진 뜨게 설정
//             */
//        }
//        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
//        dialog.behavior.skipCollapsed = true
//        dialog.show()
//    }
}