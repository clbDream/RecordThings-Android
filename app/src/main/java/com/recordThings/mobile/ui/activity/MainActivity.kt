package com.recordThings.mobile.ui.activity

import android.view.View
import android.widget.TextView
import com.hjq.shape.view.ShapeButton
import com.recordThings.mobile.R
import com.recordThings.mobile.aop.SingleClick
import com.recordThings.mobile.app.AppActivity
import com.recordThings.mobile.app.AppApplication
import kotlin.concurrent.thread

class MainActivity : AppActivity() {

    private val addInspiration: ShapeButton? by lazy { findViewById(R.id.add_inspiration) }
    private val linggan_num: TextView? by lazy { findViewById(R.id.linggan_num) }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        setOnClickListener(addInspiration)
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
            else -> {}
        }
    }

    override fun onResume() {
        super.onResume()
        thread {
            val allInspirationNum = AppApplication.db.inspirationDao().getAllInspirationNum()
            runOnUiThread {
                linggan_num?.text = allInspirationNum.toString()
            }
        }
    }
}