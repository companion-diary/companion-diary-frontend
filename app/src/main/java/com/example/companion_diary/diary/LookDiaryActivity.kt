package com.example.companion_diary.diary

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.companion_diary.MainActivity
import com.example.companion_diary.diary.LookDiaryVPAdapter
import com.example.companion_diary.R
import com.example.companion_diary.databinding.ActivityLookDiaryBinding
import com.example.companion_diary.diary.entities.Diary
import com.example.companion_diary.diary.entities.DiaryPreview
import com.example.companion_diary.diary.entities.DiaryResponse
import com.example.companion_diary.diary.network.DiaryClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.abs

class LookDiaryActivity:AppCompatActivity() {
    private val binding: ActivityLookDiaryBinding by lazy {
        ActivityLookDiaryBinding.inflate(
            layoutInflater
        )
    }
    private val diaryList: ArrayList<DiaryPreview> by lazy { mIntent.getSerializableExtra("diaryList") as ArrayList<DiaryPreview> }
    private val selectDate by lazy { mIntent.getStringExtra("selectDate")!! }
    private val mIntent by lazy { intent }
    private val lookDiaryAdapter by lazy { LookDiaryVPAdapter(diaryList, this, selectDate) }
    private val TAG = "LookDiaryActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityLookDiaryBinding.inflate(layoutInflater)

//        getDiaryList()
        initVp()
        initView()
        setContentView(binding.root)
    }

    private fun getDiaryList() {
//        diaryList = mIntent.getSerializableExtra("diaryList") as ArrayList<DiaryPreview>
        /**
         * intent로 데이터 받아오기
         * 아래는 dummy data
         */
//        var tempImgList = ArrayList<Uri>()
//        var tempImg = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBxMQEhUQEBIVFRUVFxUVFRUVFRcVFRcWFRUXFxUVFRYYHSggGBolGxUVITEhJSkrLi4uFx8zODMsNygtLisBCgoKDg0OGhAQGCsdHR0tLSstKysrLS0tKy0tLSstLS0tLS0rLS0tLSstLS0tLS0tKzUtKy0tKzctLTcrKy0tK//AABEIALcBEwMBIgACEQEDEQH/xAAbAAACAwEBAQAAAAAAAAAAAAACAwABBAUGB//EADgQAAIBAgMECQQBAwMFAAAAAAABAgMRBCExBRJBUQYTYXGBkaGx8CLB0eHxFDJSFWKCByNCQ3L/xAAYAQADAQEAAAAAAAAAAAAAAAAAAQMCBP/EAB4RAQEAAwEBAQEBAQAAAAAAAAABAgMRIRIxQVEy/9oADAMBAAIRAxEAPwD10UMSAiMRpkSQSRSDQBaRaIi0AWQhYBC0UQAtlEIACPjoIsNQreNSI2UHYGTI3YpNYWDcjqAE7tUmsaI0BKdgt8X2fyrdIkTfKlLgOZFYlyOQtsByzNzYx8NFymIVYXKs3poUuySMTXbWm5DIq1tWXRxalLdQsdktPLVZGqwSRLFlUiMVoZIRNuJWRlWgAEkLkMkxcgBTRCyAGyLDUhCYaYA9MNMQmGmAOTDTEplpgZ1yXF7xdwBnaJlWsapL6Tm4hHLs22Xx0Ya5Y1Uqu8rjLnKwrabOjCVzU3Flq5TYE6wq4tks9nW8cDpVBMWRgykS6pIJR1GJ5C1LK5Fo2OAqrzF53GyXz2EV6yQji4VHdjY3fiYqE7u3LUuGOvK0edhyi4ugoZ9xU4maeKs7cte8nXXXzNs11j5BKyyWrE7QxG4kl8QEKmbk3lw/JydoYrfkYtVmK6uKbVlqbNgybm2+Ry1nkdjZVLdV35cSur/rqe2+cd65BUaqYSkdnXFxVfQxo3VdDHYZFyQuSHSYmUkALsWU5ogBoihiQSiEogApBpBKIaiAAkFYNRL3QMFiWGbpN0A00FvRsYcTTNVCTi7oDGyvmcm7H+ujVl/GCCNdGBVOBqULEYtSJFSQcmJqyM04qchFWoDOTBtfMUaa6bvFBNcBVGe6iutWprrPDajOdVp3ZqlV4mbESby0Cnj4TWVk0nZPjxZIuNGN46215FdTzfzsA6xN6NqPPT9jh1hdd731O3HP5mdGNf6bfO9mHFVFJ6acbfe/4HUa6StFNvm7L3AwYmpZZvzy9ORybZv3ZvxMpPW3g0/Uy7lvlxSDK8NoROrhZnJhL4zoYeqXwQyb+ssF1oiErlSkbYaliGJnVYG+XJmplYzcZS5NsXKLG7xL3KTLqdxsZ9xkNG6Q0w6aQSQKYaYASQSQKYSYASLSKRdwC7EsRF3A0sDOlcZFXDcCG78W1ApUmHNW1NFOAFZZHPzxbvrDVYibG1UImibZVXLIGGniHUWQtPdS+c0HGul1qjfjkDTTft4cSSd5di9wK9W3d7/oOH0VSp5cPAlKDvvSduSvd58X2iN7K+aXN2V+5Fwrp5LPwbXoMj6nP+TDUvz9DXdy14dlkYsRCXBK3Yr/AGGCKzvk/wAPwYmUJQz3m1ytmBVqPk/N+wyNVtaP0+4utcLliU1ldPu3TNvVHon53GV6Kk73Sfk/2FRhb/y8ggv4XTUk/qy70dSgjJLDzvdTbXJ6GqkmiuE9RzrXCRbYtF3LIjUiOQpyLjIDFKQMahczPJ2F3g51qVYsxdaQf0Xy9Eg0LQaLoDQSBRaADRaAuEAFctZgDKWojOpU2aIIKCyLic2y9q+E8VICaGVnkZHiUsiVbhdamZmuBrdVSM1dWMVuMWIf49HmZG3uJ8Vl88l5ja0rqXcDGWUXw/VwjXPC6cbPPRK/e87v5zM1dNu/vwXdz4mmpC9ny975fYk6dlduy1f2NBllTy0b7/wFRedreDV/YJVr56Lz8+RFXtwXhYXh+tfUcZfPAzV6ceErPufuMhUcrq/i1cU1bKUVfudn87wJyK8mnbLvtkKWIs/qS7GszTjKCvdWs+23o0ZJUlo36XMqQ2ruTzaXfl9huHow5+hnwtOzs9OfA6NOEdLfj9mozkrqf8X9w6bejHdQlo7eNvRFJeJXFGoU2XcFlUwtkTKkwUIGSmZqjGyZmqsVpxChfWEMG9ZFjExKYSZ2OQ5MJMUmGmBjCQCZdwAx+GhdmVM14SWZm/hxuashKqK5ploZ4YfefE5Mu9dOPOE4ybaysYFUVNOdSSSXNHoFgI20MlbZEH/de2eV2K67+nNmP48rT6TYWVTdhWSd7Wat75naxDvG/B5prR9x8p21j6scPLCxoQjPrXOVW8d9rebSS4JKy14Hr+g+2nUwipVFeVO6bunlr9zezVJj0YZ9ya61ZK+fj7CqWIWmtr3Xr9jJtRuG9JdtjlUazdv9134o5Ja7bhOPU4SSlC/b62zMONrtvdjd/fvK2bWy3ZZO/wBjmdJ9rKhB7r+p3u+XMthPpDKfKsRXcXZtLsvmZajqvOMl4nj9qbUxNColU3d5xU912k0pab1sk8tDpbL22+s6urDdfOLutL+RrLTlCw3Y3x6jBbVcfpqxs+aOk8Ymrp/nyOFOtCWkk/cfQg+DyI9q1xlb6lW/G5kqQjLs9mSpT46MRPfXJi6JF0JbrsdGnW+Z38jDGF82nfsH4epbiOUso6Majta5SnzLiotEtb+C0QqSkVcXOoSMysSorEcS0WaZ6VIzVmaqhzMXPtM04ROrmQy27SGeNveoJMNUGGqDOtxgTDTCVBhKiwMNy7hdSy1RYEE14SSM/VMfQjYzWo7EVkXh+4HDyujRf5/JHnvVO+cNQuoim+/0+5HPLh7euhq+xmPmfSronUnW3oRvF3s+S/xklbu7jdsfZEMHBxi4uUs5tcXwV1y5Hq8fTi9b9z+ZnDrrkuw59mV5x1a+VwNtyb0+dgjZuA+qF9Ip375XOs8DvP6jdToJNvuWWvac8xrquc5xx9s0VTpyqK91nkr5as+f7cxDqwvFX1fplkfU9oW3Wua0WunYj5xWwt5yiuD0V8uRfXeJZT6nHkqbp1Pq3EpZf23S7zp7JwvW1IqEcou8mvY61Po9CTvNduX2sehwGHhSju0oNdtvuVz3eeI4aeXtVRwMck1nxOlQwCtdL8DMHSOpCFl+USwx/wBb2Z3+ONidm3V1deH4OeqUovdlmuD/AJ0PWQguwz4vA7yyXrc1lhKzjss/XGlSaWn2FKF389zpLCTirSX4/Rj6vdeliVx4rMuijkE6gp1OwW6q7u/7FJ4nfV1qnxAUqoirK71KpJoPovl06cxqZipTNMJFMck8sQ1zg7Uq2R3sQ8sjx216jcs8rGqUaFVIc2NWxB8Pr7aoFqI1RCUCzmKUQlAcoF7oAncJuD90m4AJ3A4RtmM3SOIqcPpTsVWxNuDEMqV3qzntWmJNTGyv/b7t+xrwmKcspL0f3MzTLhWS1du/8sMbTykbJUc9MuH8GarhIxV5ak2ztZYag6+trRiucpPdivNnzXavSHGU53rrdTzvFX5duma8zd19Zxy/j2rjdtrS/wCReKi0rJ2+dh4+l08pxSindu2qslnm8zuUNpdfFTjLxXzUndfFZl614qgpQs3vW8PVHmcRh1GSvlbis/KR28TWdr78nlo2lfzZx6lW7y3nfxJZcWw66GCqU2sl89DVUSei9jmYfDyX7/bNbbX6NT1nLk/BpeAUanau53uc3E4uz1Bo15VHkr+Hba4+scdyM1zs/nINza+3FGGjQqvWL7zRCDWqeXM10uNU6t1+Tn1bPgiYrGxjf6rW1s0/RGOe1YLt/wCLQuw5jV1qaf8ABzMRTtoPqbS3n9GXsBKs3r6GMrG5LGeMBqDUS90ydDEfCRUY2LRqeMU7ePMdIKMm/pjlzPRRkSrBNZotKlfHhNxkOzXw8d56ev4Ia6T7GohqJEGiqCki90tBJAFKJN0MsYBYW3ca0DVVkSyvfG8SmmhTmHUk7WOZWqtNetmSs4rL1uqVMsjk42qk7Ni8Vj0vwcvF4tPPIWVbxx634mccXhauH1klvQSybcHdZ+a8T5fVnNXvUk7Jr6rrJ/5R4O1suZ7FY506m+m08uy/Y39jft3ZVPEQjWtGEpW32leL53S1duJXXn2M5aveR8gw9qjl1ct6y+pNNO3B9p73/p7Rl9W+3bJJfgRLoduy3k4tdmubWvE9X0fwMYZq8Xws/K/B5WzRu+xPnHRx1JKNrJpcHr5HBqYNXyjbuyfqeorqOt0nx/g8/tLadKk7OS3npxj5o58sFsMycRJUktbvhf8AZiqVJS/tTfddmKji+trJTlq7c7H0PY+yo01dcfLvHMejLLji7J6PuVpVG2nw1PSYfZ0IL6YryNqjHQVVqpO3z1Nc4n9WhTSEYmCfAGpUevxlScXm8sudhG8/tPBWd1pyOZWoJ52XK7zOxjq31Wya7jn1pdmXaSuK2OVc3qUm9Q4Uw61RXsxkKbnoZk61aWoCtGaZUnHXL1Ftdhr5Y+lLPsLt2oqwyOZqYs2qUSVFkGsgajVikx4na8/Xpy3nk/Mh0JJXIa4XX1VINIFBoqitIJIpBIYSxaRZaAB4gY3KDduY9xuuRlxlOo4OKaeTtwMWNSpCl/21fl+DzW1MSk76JHSxn9SqShTgpSSs7ySTyt7+55zH7JxtXNwinlf68r3u2ssuBjKdjeH76ybTxkYRSbzt7nBxO1Emdyv0OxVaW9OUVfk+Bpw3QWS/ummTuu2ujHZhI8g6s5yvFSZ7bo/jd6Cp1lutLXLzN+H6Kxjqzo4fYkI8DeGFiee2X8ecx+CqJ3ppS4bybaz/ANpyqOFxSf1Npc2+GbVuTTt5n0Crs2LVk7dxy8ZsGc9KjKcYuz6/XltoXteU3fNPPK/xXPJ7RxWfO3E93iuh85XvO9/sc+XQaXO5iy1qZYx57orNVMRBNtO/Ll4n26lTSj4HgNg9FJYesqjzST9j2+KxNqTa1yXn89ByMZ3tSN91y7zDjJWV9WMxOOhCKjfh5u1/nccXG7Ti8ln84hYUVVxbvbivbgDjcY2s8mjzsNvxjiHSlJbryvyf82E4zHS3nFO/JrNNEr3i2M7W6tiMxOIrZamSnGcuDH/6fUnwZOY2qWyMn9Rd6+BuwtTIOj0eqN3Orhuj0lqzeOus5Z48cyc08n6MT3HpYbAjxux8djwXAr8I/byii+8ZCnL/ABZ62Oz4rgH/AEkeRr4L7eWVGXIGpg5Pger/AKVAvCofyz9PH/6cyHrf6VEH8l120GhcQ0zTI0GhaYSAhospFgY0yMpFgFNCpRGlWEZW6Xuh2JYAXYiDaBABZVi2RDILiDuDWCxAEY5jatGMlZpPvBQ1MOBhrbLpSteCy7+JmjsLDr/1ROswJIOH1x49HcMndUKd/wD5Hx2VRWlKC8DfYpoXB1mjhILSK8g+qjyQ1oFgAbi5E3QmVcAqwDQbBYwWyhjQEkMAYLCYLAgkKsQA6EWGiEACQSIQCEgiEALRZZANCEIKhRCEA1MEhBhTKIQCQpkIIKQaZRAC7lMogBRRZAAWCQgGpoGxCCILI0QgwFgshBgDAkQgAu5CEAP/2Q=="
//        tempImgList.apply {
//            add(Uri.parse(tempImg))
//            add(Uri.parse(tempImg))
//            add(Uri.parse(tempImg))
//            add(Uri.parse(tempImg))
//        }
//        diaryList.apply{
//            add(Diary("2022.08.22","호두야 안녕","호두 귀여워","호두",tempImgList))
//            add(Diary("2022.08.22","호두야 안녕","호두 귀여워1","호두",tempImgList))
//            add(Diary("2022.08.22","호두야 안녕","호두 귀여워2","호두",tempImgList))
//            add(Diary("2022.08.22","호두야 안녕","호두 귀여워3","호두",tempImgList))
//        }
    }

    private fun initView() {
        binding.prevIv.bringToFront()
        binding.prevItemIv.bringToFront()
        binding.nextItemIv.bringToFront()
        /**
         * diary 내용 view에 보여주기
         */
        setClickListener()
    }

    private fun setClickListener() {
        binding.prevIv.setOnClickListener {
            finish()
        }
        binding.prevItemIv.setOnClickListener {
            binding.lookDiaryVp.currentItem -= 1
        }
        binding.nextItemIv.setOnClickListener {
            binding.lookDiaryVp.currentItem += 1
        }
    }

    private fun initVp() {
//        val selectDate = mIntent.getStringExtra("selectDate")!!
//        val lookDiaryAdapter = LookDiaryVPAdapter(diaryList, this, selectDate)
        val pos = mIntent.getIntExtra("position", 0)
        val itemMargin = resources.getDimension(R.dimen.look_diary_margin).toInt()
        val itemPadding = resources.getDimension(R.dimen.look_diary_padding).toInt()

        binding.lookDiaryVp.apply {
            adapter = lookDiaryAdapter
            (getChildAt(0) as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            setCurrentItem(pos, false)

            /**
             * pageTransformer 설정
             */

            var compositePageTransformer = CompositePageTransformer()
            compositePageTransformer.addTransformer(MarginPageTransformer(itemMargin))
            compositePageTransformer.addTransformer(ViewPager2.PageTransformer { page, position ->
                var r = 1 - abs(position)
                page.scaleX = 0.88f + r * 0.12f
            })
            compositePageTransformer.addTransformer(LookDiaryVPTransformer())
            setPageTransformer(compositePageTransformer)

            /**
             * prev, next item padding 설정
             */
            setPadding(0, itemPadding, 0, itemPadding)

            /**
             * 스크롤 막기
             */
            isUserInputEnabled = false
        }

        /**
         * clickListener 설정
         */
        lookDiaryAdapter.setMyItemClickListener(object : LookDiaryVPAdapter.MyItemClickListener {
            override fun moreBtnClick(moreIv: ImageView) {
                initPopup(moreIv)
            }
        })
    }

    fun initPopup(moreIv: ImageView) {
        val inflater: LayoutInflater = getSystemService(
            Context.LAYOUT_INFLATER_SERVICE
        ) as LayoutInflater

        val view = inflater.inflate(R.layout.dialog_look_diary_menu, null)
        val menuWidth = resources.getDimension(R.dimen.look_diary_menu_width).toInt()
        val menuStartMargin = resources.getDimension(R.dimen.look_diary_menu_start_margin).toInt()

        val popupWindow = PopupWindow(
            view,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val moreBtn = moreIv.findViewById<ImageView>(R.id.look_diary_more_iv)

        var location = IntArray(2)
        moreBtn.getLocationOnScreen(location)

        popupWindow.showAtLocation(
            binding.root,
            Gravity.NO_GRAVITY,
            moreBtn.left - menuStartMargin - menuWidth,
            location[1]
        )

        var modifyBtn = view.findViewById<TextView>(R.id.modify_diary_tv)
        var removeBtn = view.findViewById<TextView>(R.id.remove_diary_tv)
        modifyBtn.setOnClickListener {
            Log.d(TAG, "수정")
            //modifyDiary()
        }
        removeBtn.setOnClickListener {
            Log.d(TAG, "삭제")
            popupWindow.dismiss()
            lookDiaryAdapter.removeItem(binding.lookDiaryVp.currentItem)
        }
        if (popupWindow.isShowing) {
            binding.outsideIv.visibility = View.VISIBLE
            binding.outsideIv.bringToFront()
        }
        binding.outsideIv.setOnClickListener {
            if (popupWindow.isShowing) {
                binding.outsideIv.visibility = View.GONE
                popupWindow.dismiss()
            }
        }
    }

}
//    fun dpToPx(context: Context, dp: Float): Float {
//        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
//    }

