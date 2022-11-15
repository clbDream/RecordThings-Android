package com.recordThings.mobile.ui.activity

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hjq.base.BaseAdapter
import com.hjq.shape.view.ShapeEditText
import com.recordThings.mobile.R
import com.recordThings.mobile.app.AppActivity
import com.recordThings.mobile.db.DbHelper
import com.recordThings.mobile.db.entities.SportClass
import com.recordThings.mobile.db.entities.SportLog
import com.recordThings.mobile.ui.adapter.SportClassAdapter
import kotlin.concurrent.thread

/**
 * 添加运动
 */
class AddSportActivity : AppActivity() {

    private lateinit var sportClassAdapter: SportClassAdapter
    private val sport_class: RecyclerView? by lazy { findViewById(R.id.sport_class) }
    private val sport_time: ShapeEditText? by lazy { findViewById(R.id.sport_time) }

    override fun getLayoutId(): Int {
        return R.layout.activity_add_sport
    }

    override fun initView() {
        setOnClickListener(R.id.add_sport_log)

        sport_class?.also {
            it.layoutManager = GridLayoutManager(this, 4)
            sportClassAdapter = SportClassAdapter(this)
            sportClassAdapter.setOnItemClickListener(object : BaseAdapter.OnItemClickListener {
                override fun onItemClick(recyclerView: RecyclerView?, itemView: View?, position: Int) {
                    sportClassAdapter.checkItem(position)
                }
            })
            it.adapter = sportClassAdapter
        }
    }

    override fun initData() {
        thread {
            val allSportClass = DbHelper.db.sportDao().getAllSportClass() as ArrayList<SportClass>
            sportClassAdapter.addData(allSportClass)
        }
    }

    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.add_sport_log -> {
                val sportTime = sport_time?.text.toString()
                if (sportTime.isBlank() || sportTime.startsWith("0")) {
                    toast("请输入运动时长")
                    return
                }
                val currentItem = sportClassAdapter.getCurrentItem()
                if (currentItem == null) {
                    toast("请选择运动类型")
                    return
                }
                val sportLog = SportLog(class_name = currentItem.className, class_id = currentItem.id, class_icon = currentItem.classIcon, sport_time = sportTime.toLong(), create_time = System.currentTimeMillis())
                thread {
                    val addSportLog = DbHelper.db.sportDao().addSportLog(sportLog)
                    if (addSportLog > 0) {
                        runOnUiThread {
                            toast("添加成功")
                        }
                        finish()
                    } else {
                        runOnUiThread { toast("添加失败") }
                    }


                }
            }
            else -> {}
        }
    }
}