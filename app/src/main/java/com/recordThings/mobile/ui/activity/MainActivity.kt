package com.recordThings.mobile.ui.activity

import android.view.View
import android.widget.TextView
import com.hjq.shape.view.ShapeButton
import com.recordThings.mobile.R
import com.recordThings.mobile.aop.SingleClick
import com.recordThings.mobile.app.AppActivity
import com.recordThings.mobile.app.AppApplication
import com.recordThings.mobile.db.DbHelper
import kotlin.concurrent.thread

class MainActivity : AppActivity() {

    private val addInspiration: ShapeButton? by lazy { findViewById(R.id.add_inspiration) }
    private val lingganNum: TextView? by lazy { findViewById(R.id.linggan_num) }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        setOnClickListener(addInspiration,lingganNum)
    }

    override fun initData() {

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
            lingganNum->{
                startActivity(InspirationListActivity::class.java)
            }
            else -> {}
        }
    }

    override fun onResume() {
        super.onResume()
        thread {
            val allInspirationNum = DbHelper.db.inspirationDao().getAllInspirationNum()
            runOnUiThread {
                lingganNum?.text = allInspirationNum.toString()
            }
        }
    }
}