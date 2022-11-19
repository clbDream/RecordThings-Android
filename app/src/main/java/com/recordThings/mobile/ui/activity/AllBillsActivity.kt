package com.recordThings.mobile.ui.activity

import android.view.View
import com.recordThings.mobile.R
import com.recordThings.mobile.app.AppActivity

/**
 * 全部账单
 */
class AllBillsActivity: AppActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_all_bills
    }

    override fun initView() {
        setOnClickListener(R.id.addRecord)
    }

    override fun initData() {
        
    }

    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.addRecord -> {
                startActivity(AddRecordActivity::class.java)
            }
            else -> {}
        }
    }
}