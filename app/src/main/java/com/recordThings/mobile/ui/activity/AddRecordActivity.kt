package com.recordThings.mobile.ui.activity

import android.view.View
import android.widget.RadioGroup
import androidx.lifecycle.lifecycleScope
import com.recordThings.mobile.R
import com.recordThings.mobile.app.AppActivity
import com.recordThings.mobile.db.DbHelper
import com.recordThings.mobile.db.entities.RecordType
import com.recordThings.mobile.widget.TypePageView
import kotlinx.coroutines.launch

/**
 * 添加账单
 */
class AddRecordActivity: AppActivity() {

    private val type_page_outlay:TypePageView?by lazy { findViewById(R.id.type_page_outlay) }
    private val type_page_income:TypePageView?by lazy { findViewById(R.id.type_page_income) }
    private val rg_type: RadioGroup?by lazy { findViewById(R.id.rg_type) }

    override fun getLayoutId(): Int {
        return R.layout.activity_add_recorad
    }

    override fun initView() {
        rg_type?.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.rb_outlay -> {
                    type_page_outlay?.visibility = View.VISIBLE
                    type_page_income?.visibility = View.GONE
                }
                R.id.rb_income -> {
                    type_page_outlay?.visibility = View.GONE
                    type_page_income?.visibility = View.VISIBLE
                }
                else -> {}
            }
        }
    }

    override fun initData() {
        getTypes()
    }

    private fun getTypes() {

        lifecycleScope.launch {
            val allRecordTypes = DbHelper.db.recordTypeDao().allRecordTypes
            allRecordTypes?.collect { value ->
                val resultzhichu: ArrayList<RecordType> = ArrayList()
                val resultshouru: ArrayList<RecordType> = ArrayList()
                value?.forEachIndexed { index, recordType ->
                    //支出
                    if (recordType?.type == RecordType.TYPE_OUTLAY) {
                        resultzhichu.add(recordType)
                    }else{
                        if (recordType != null) {
                            resultshouru.add(recordType)
                        }
                    }
                }

                type_page_outlay?.setNewData(resultzhichu)
                type_page_income?.setNewData(resultshouru)
            }
        }
    }
}