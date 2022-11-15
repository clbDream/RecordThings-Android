package com.recordThings.mobile.ui.activity

import android.view.View
import android.widget.TextView
import com.hjq.shape.layout.ShapeLinearLayout
import com.hjq.shape.view.ShapeButton
import com.recordThings.mobile.R
import com.recordThings.mobile.aop.SingleClick
import com.recordThings.mobile.app.AppActivity
import com.recordThings.mobile.db.DbHelper
import kotlin.concurrent.thread

class MainActivity : AppActivity() {

    private val addInspiration: ShapeButton? by lazy { findViewById(R.id.add_inspiration) }
    private val add_daiban: ShapeButton? by lazy { findViewById(R.id.add_daiban) }
    private val add_sport: ShapeButton? by lazy { findViewById(R.id.add_sport) }
    private val lingganNum: TextView? by lazy { findViewById(R.id.linggan_num) }
    private val daiban_num: TextView? by lazy { findViewById(R.id.daiban_num) }
    private val sport_num: TextView? by lazy { findViewById(R.id.sport_num) }
    private val layout_linggan: ShapeLinearLayout? by lazy { findViewById(R.id.layout_linggan) }
    private val layout_daiban: ShapeLinearLayout? by lazy { findViewById(R.id.layout_daiban) }
    private val layout_sport: ShapeLinearLayout? by lazy { findViewById(R.id.layout_sport) }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        setOnClickListener(addInspiration, layout_linggan, layout_daiban, add_daiban,add_sport,layout_sport)
    }

    override fun initData() {
        DbHelper.initSportClass()
    }

    override fun isStatusBarEnabled(): Boolean {
        return false
    }

    @SingleClick
    override fun onClick(view: View) {
        when (view) {
            addInspiration -> {
                startActivity(EditInspirationActivity::class.java)
            }
            layout_linggan -> {
                startActivity(InspirationListActivity::class.java)
            }
            layout_daiban -> {
                startActivity(DaibanListActivity::class.java)
            }
            add_daiban -> {
                startActivity(EditDaibanActivity::class.java)
            }
            add_sport->{
                startActivity(AddSportActivity::class.java)
            }
            layout_sport->{
                startActivity(SportLogActivity::class.java)
            }
            else -> {}
        }
    }

    override fun onResume() {
        super.onResume()
        thread {
            val allInspirationNum = DbHelper.db.inspirationDao().getAllInspirationNum()
            val allDaibanNum = DbHelper.db.daibanDao().getAllDaibanNum(false)
            val sportCountToday = DbHelper.db.sportDao().getSportCount()
            runOnUiThread {
                lingganNum?.text = allInspirationNum.toString()
                daiban_num?.text = allDaibanNum.toString()
                sport_num?.text = sportCountToday.toString()
            }
        }
    }
}