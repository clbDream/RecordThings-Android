package com.recordThings.mobile.ui.activity

import android.view.View
import android.widget.EditText
import android.widget.RadioGroup
import androidx.lifecycle.lifecycleScope
import com.hjq.shape.view.ShapeTextView
import com.recordThings.mobile.R
import com.recordThings.mobile.app.AppActivity
import com.recordThings.mobile.db.DbHelper
import com.recordThings.mobile.db.entities.Record
import com.recordThings.mobile.db.entities.RecordType
import com.recordThings.mobile.utils.BigDecimalUtil
import com.recordThings.mobile.utils.DateUtils2
import com.recordThings.mobile.widget.KeyboardView
import com.recordThings.mobile.widget.TypePageView
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
 * 添加账单
 */
class AddRecordActivity : AppActivity() {

    private val TAG_PICKER_DIALOG = "Datepickerdialog"
    private val type_page_outlay: TypePageView? by lazy { findViewById(R.id.type_page_outlay) }
    private val type_page_income: TypePageView? by lazy { findViewById(R.id.type_page_income) }
    private val rg_type: RadioGroup? by lazy { findViewById(R.id.rg_type) }
    private val keyboard: KeyboardView? by lazy { findViewById(R.id.keyboard) }
    private val edt_remark: EditText? by lazy { findViewById(R.id.edt_remark) }
    private val qm_tv_date: ShapeTextView? by lazy { findViewById(R.id.qm_tv_date) }
    private var mCurrentChooseDate: Date = DateUtils2.getTodayDate()
    private val mCurrentChooseCalendar = Calendar.getInstance()
    private var mCurrentType = 0

    override fun getLayoutId(): Int {
        return R.layout.activity_add_recorad
    }

    override fun initView() {
        rg_type?.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.rb_outlay -> {
                    mCurrentType = RecordType.TYPE_OUTLAY
                    type_page_outlay?.visibility = View.VISIBLE
                    type_page_income?.visibility = View.GONE
                }
                R.id.rb_income -> {
                    mCurrentType = RecordType.TYPE_INCOME
                    type_page_outlay?.visibility = View.GONE
                    type_page_income?.visibility = View.VISIBLE
                }
                else -> {}
            }
        }
        keyboard?.setEditTextFocus()
        keyboard?.setAffirmClickListener { text ->
            insertRecord(text)
        }

        mCurrentType = RecordType.TYPE_OUTLAY

        qm_tv_date?.setOnClickListener { v ->
            val dpd: DatePickerDialog = DatePickerDialog.newInstance(
                { view, year, monthOfYear, dayOfMonth ->
                    mCurrentChooseDate = DateUtils2.getDate(year, monthOfYear + 1, dayOfMonth)
                    mCurrentChooseCalendar.setTime(mCurrentChooseDate)
                    qm_tv_date?.setText(DateUtils2.getWordTime(mCurrentChooseDate))
                }, mCurrentChooseCalendar
            )
            dpd.setMaxDate(Calendar.getInstance())
            dpd.show(fragmentManager, TAG_PICKER_DIALOG)
        }
    }

    private fun insertRecord(text: String?) {
        //防重复提交
        keyboard?.setAffirmEnable(false)
        val record = Record()
        record.money = BigDecimalUtil.yuan2FenBD(text)
        record.remark = edt_remark?.text.toString().trim()
        record.time = mCurrentChooseDate
        record.createTime = Date()
        record.recordTypeId =
            if (mCurrentType == RecordType.TYPE_OUTLAY) type_page_outlay?.currentItem?.id!! else type_page_income?.currentItem?.id!!
        lifecycleScope.launch(Dispatchers.IO) {
            val insertRecord = DbHelper.db.recordDao().insertRecord(record)
            withContext(Dispatchers.Main) {
                if (insertRecord > 0) {
                    toast("保存成功")
                    finish()
                } else {
                    toast("保存失败")
                }
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
                    } else {
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