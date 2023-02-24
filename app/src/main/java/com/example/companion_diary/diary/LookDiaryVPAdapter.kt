package com.example.companion_diary.diary

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.method.MovementMethod
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.companion_diary.MainActivity
import com.example.companion_diary.R
import com.example.companion_diary.databinding.ItemDiaryBinding
import com.example.companion_diary.diary.entities.Diary
import com.example.companion_diary.diary.entities.DiaryPreview
import com.example.companion_diary.diary.entities.DiaryResponse
import com.example.companion_diary.diary.network.DiaryClient
import com.gun0912.tedpermission.provider.TedPermissionProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "LookDiaryVPAdpater"

class LookDiaryVPAdapter(
    val diaryList: ArrayList<DiaryPreview>,
    val mContext: Context,
    val selectDate: String
) : RecyclerView.Adapter<LookDiaryVPAdapter.ViewHolder>() {

    interface MyItemClickListener{
        fun moreBtnClick(view:ImageView)
    }
    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }
    fun modifyItem(position: Int){
        val targetDiaryId = diaryList[position].diaryId
        /**
         * Diary 객체 intent로 전달 (WriteDiaryActivity로 전달)
         */
    }
    fun removeItem(position: Int){
        /**
         * 서버로 전달 필요
         */
        val targetDiaryId = diaryList[position].diaryId
        diaryList.removeAt(position)
        Log.d(TAG, "$targetDiaryId")
        val call: Call<DiaryResponse> = DiaryClient.diaryService.deleteDiary(targetDiaryId)
        call.enqueue(object : Callback<DiaryResponse> {
            override fun onResponse(call: Call<DiaryResponse>, response: Response<DiaryResponse>) {
                if (response.isSuccessful) {
                    val diaryResponse: DiaryResponse = response.body()!!
                    if (diaryResponse.isSuccess) {
                        Log.d(TAG,"삭제 성공")
                        val intent = Intent(mContext, MainActivity::class.java)
                        mContext.startActivity(intent)
                    } else {
                        showAlertDialog(diaryResponse.message)
                    }
                } else {
                    showAlertDialog(response.message())
                }
            }

            override fun onFailure(call: Call<DiaryResponse>, t: Throwable) {
                showAlertDialog(t.message.toString())
            }
        })
        notifyDataSetChanged()
    }

    private fun showAlertDialog(message: String) {
        AlertDialog.Builder(mContext)
            .setTitle("오류 발생")
            .setMessage(message)
            .setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    inner class ViewHolder(val binding: ItemDiaryBinding): RecyclerView.ViewHolder(binding.root){
        fun initView(position: Int){
            when(diaryList[position].petTag){
                "ANIMAL" -> {
                    val animalColor = mContext.getColor(R.color.main_color_orange)
                    binding.lookDiaryIconIv.setImageResource(R.drawable.ic_animal)
                    binding.yearMonthDateTv.setTextColor(animalColor)
                    binding.lookDiaryNameTagTv.setTextColor(animalColor)
                }
                else -> {
                    val plantColor = mContext.getColor(R.color.main_color_green)
                    binding.lookDiaryIconIv.setImageResource(R.drawable.ic_plant)
                    binding.yearMonthDateTv.setTextColor(plantColor)
                    binding.lookDiaryNameTagTv.setTextColor(plantColor)
                }
            }
            binding.itemDiaryLayout.setBackgroundResource(R.drawable.border_diary_item)
            binding.lookDiaryNameTagTv.text = diaryList[position].petName
            binding.titleTv.text = diaryList[position].diaryTitle
            binding.contentsTv.text = diaryList[position].diaryContent
            binding.contentsTv.movementMethod = ScrollingMovementMethod()
            binding.yearMonthDateTv.text = selectDate

            binding.lookDiaryMoreIv.setOnClickListener {
                mItemClickListener.moreBtnClick(binding.lookDiaryMoreIv)
            }

//            initImgVP(position)
        }
//        private fun initImgVP(position: Int){
//            val imgVPAdapter = LookDiaryImgVPAdapter(diaryList[position].imgList,mContext)
//            binding.imgVp.apply {
//                adapter = imgVPAdapter
//                binding.vpDotIndicator.attachTo(binding.imgVp)
//                (getChildAt(0) as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER
//            }
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDiaryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.initView(position)
    }

    override fun getItemCount(): Int = diaryList.size


}