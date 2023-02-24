package com.example.companion_diary.diary.custom


import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.companion_diary.MainActivity
import com.example.companion_diary.R
import com.example.companion_diary.databinding.DialogDiaryDateSelectionDetailsBinding
import com.example.companion_diary.diary.*
import com.example.companion_diary.diary.entities.*
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
    private var selectedPet: Pet? = null
    private var today : DateTime = DateTime()
    private var selectDateStr: String = ""
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
        val dialog = BottomSheetDialog(context, R.style.TransparentBottomSheetDialogFragment)
        val dialogBinding = DialogDiaryDateSelectionDetailsBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(dialogBinding.root)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.behavior.skipCollapsed = true
        selectDateStr = "${date.year}년 ${date.monthOfYear}월 ${date.dayOfMonth}일"
        dialogBinding.yearMonthDateTv.text = selectDateStr

        dialogBinding.prevIv.setOnClickListener {
            selectedPet = null
            dialog.dismiss()
        }
        dialog.setOnDismissListener {
            selectedPet = null
        }

        getDiaryList("${date.year}-${date.monthOfYear}-${date.dayOfMonth}", dialogBinding, selectDateStr)
        getPetList(dialogBinding)

        dialogBinding.writeDiaryBtn.setOnClickListener {
            var intent = Intent(context, WriteDiaryActivity::class.java)
            if(selectedPet == null){
                Toast.makeText(context, "어떤 친구의 하루를 작성할지 선택해주세요",Toast.LENGTH_SHORT).show()
            } else {
                intent.putExtra("date",date)
                intent.putExtra("pet",selectedPet)
                selectedPet = null
                context.startActivity(intent)
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun getPetList(dialogBinding: DialogDiaryDateSelectionDetailsBinding) {
        val call: Call<PetList> = DiaryClient.diaryService.getPetList()
        call.enqueue(object: Callback<PetList>{
            override fun onResponse(call: Call<PetList>, response: Response<PetList>) {
                if(response.isSuccessful){
                    val petResult: PetList = response.body()!!
                    if(petResult.isSuccess){
                        if (petResult.result != null) {
                            var nameTagListAdapter = NameTagRVAdapter(petResult.result)
                            var nameTagListManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
                            dialogBinding.companionNameTagRv.apply{
                                adapter = nameTagListAdapter
                                layoutManager = nameTagListManager
                                addItemDecoration(DiaryItemDecoration(context, 23f))
                            }
                            nameTagListAdapter.setMyItemSelectedListener(object: NameTagRVAdapter.MyItemSelectedListener{
                                override fun onItemSelected(pet: Pet) {
                                    selectedPet = pet
                                }
                            })
                        }
                    } else{
                        showAlertDialog(petResult.message)
                    }
                } else {
                    showAlertDialog(response.message())
                }
            }

            override fun onFailure(call: Call<PetList>, t: Throwable) {
                showAlertDialog(t.message.toString())
            }
        })
    }

    private fun showAlertDialog(message: String){
        AlertDialog.Builder(context)
            .setTitle("오류 발생")
            .setMessage(message)
            .setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun getDiaryList(date: String, dialogBinding: DialogDiaryDateSelectionDetailsBinding, selectDateStr: String) {
        val call: Call<DiaryPreviewList> = DiaryClient.diaryService.getDiaryList(date)
        call.enqueue(object: Callback<DiaryPreviewList> {
            override fun onResponse(call: Call<DiaryPreviewList>, response: Response<DiaryPreviewList>) {
                if(response.isSuccessful){
                    val diaryResult: DiaryPreviewList = response.body()!!
                    if(diaryResult.isSuccess){
                        if (diaryResult.result != null) {
                            var diaryListAdapter = DiaryRVAdapter(diaryResult.result, context, selectDateStr)
                            var diaryListManager =
                                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                            dialogBinding.diaryRv.apply {
                                adapter = diaryListAdapter
                                layoutManager = diaryListManager
                                addItemDecoration(DiaryItemDecoration(context, 10f))
                            }
                        }
                    } else{
                        showAlertDialog(diaryResult.message)
                    }
                } else {
                    showAlertDialog(response.message())
                }
            }

            override fun onFailure(call: Call<DiaryPreviewList>, t: Throwable) {
                showAlertDialog(t.message.toString())
            }
        })
    }
}