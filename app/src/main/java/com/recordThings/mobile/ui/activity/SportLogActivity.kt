package com.recordThings.mobile.ui.activity

import android.view.View
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recordThings.mobile.R
import com.recordThings.mobile.app.AppActivity
import com.recordThings.mobile.db.DbHelper
import com.recordThings.mobile.ui.adapter.SportLogAdapter
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

/**
 * 运动记录
 */
class SportLogActivity : AppActivity() {

    private lateinit var sportLogAdapter: SportLogAdapter
    private val sport_num: TextView? by lazy { findViewById(R.id.sport_count) }
    private val sport_time: TextView? by lazy { findViewById(R.id.sport_time) }
    private val sport_log_list: RecyclerView? by lazy { findViewById(R.id.sport_log_list) }

    override fun getLayoutId(): Int {
        return R.layout.activity_sport_log
    }

    override fun initView() {
        setOnClickListener(R.id.btn_send)

        sport_log_list?.also {
            sportLogAdapter = SportLogAdapter(this)
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = sportLogAdapter
        }
    }

    override fun initData() {
        lifecycleScope.launch {
            val allSportTime = DbHelper.db.sportDao().getAllSportTime()
            allSportTime.collect { value ->
                runOnUiThread {
                    sport_time?.text = "${value}分钟"
                }
            }
        }
        lifecycleScope.launch {
            val allSportLog = DbHelper.db.sportDao().getAllSportLog()
            allSportLog.collect { values ->
                runOnUiThread {
                    sportLogAdapter.setData(values.toMutableList())
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        thread {
            val sportCountToday = DbHelper.db.sportDao().getSportCount()
            runOnUiThread {
                sport_num?.text = sportCountToday.toString()
            }
        }
    }

    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.btn_send -> {
                startActivity(AddSportActivity::class.java)
            }
            else -> {}
        }
    }
}