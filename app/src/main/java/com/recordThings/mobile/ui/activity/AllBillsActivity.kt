package com.recordThings.mobile.ui.activity

import android.view.View
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recordThings.mobile.R
import com.recordThings.mobile.app.AppActivity
import com.recordThings.mobile.db.DbHelper
import com.recordThings.mobile.db.entities.RecordType
import com.recordThings.mobile.db.entities.RecordWithType
import com.recordThings.mobile.ui.adapter.HomeAdapter
import com.recordThings.mobile.utils.BigDecimalUtil
import com.recordThings.mobile.utils.DateUtils2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.util.*

/**
 * 全部账单
 */
class AllBillsActivity: AppActivity() {

    private val tv_month_outlay:TextView?by lazy { findViewById(R.id.tv_month_outlay) }
    private val tv_month_income:TextView?by lazy { findViewById(R.id.tv_month_income) }
    private val tv_month_balance:TextView?by lazy { findViewById(R.id.tv_month_balance) }
    private val rv_home:RecyclerView?by lazy { findViewById(R.id.rv_home) }
    private var mAdapter: HomeAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_all_bills
    }

    override fun initView() {
        setOnClickListener(R.id.addRecord,R.id.statistics_view)

        rv_home?.also {
            it.layoutManager = LinearLayoutManager(this)
            mAdapter = HomeAdapter(this)
            it.adapter = mAdapter
        }

    }

    override fun initData() {
        getRecordList()
        lifecycleScope.launch(Dispatchers.IO){
            val dateFrom: Date = DateUtils2.getCurrentMonthStart()
            val dateTo: Date = DateUtils2.getCurrentMonthEnd()
            val sumMoney = DbHelper.db.recordDao().getSumMoney(dateFrom, dateTo)
            withContext(Dispatchers.Main){
                sumMoney?.collect {sumMoneyBean->
                    //支出


                    //支出
                    var outlay = "0"
                    //收入
                    //收入
                    var inCome = "0"
                    //结余
                    //结余
                    var balance = "0"
                    if (sumMoneyBean != null && sumMoneyBean.isNotEmpty()) {

                        //原始支出
                        var originalOutlay = BigDecimal("0")
                        //原始收入
                        var originalInCome = BigDecimal("0")
                        for (bean in sumMoneyBean) {
                            if (bean?.type == RecordType.TYPE_OUTLAY) {
                                originalOutlay = bean.sumMoney!!
                                outlay = BigDecimalUtil.fen2Yuan(bean.sumMoney)
                            } else if (bean?.type == RecordType.TYPE_INCOME) {
                                originalInCome = bean.sumMoney!!
                                inCome = BigDecimalUtil.fen2Yuan(bean.sumMoney)
                            }
                        }

                        //本月结余
                        balance = BigDecimalUtil.fen2Yuan(originalInCome.subtract(originalOutlay))
                    }
                    tv_month_outlay?.text = outlay
                    tv_month_income?.text = inCome
                    tv_month_balance?.text = balance
                }
            }
        }
    }

    private fun getRecordList() {
        lifecycleScope.launch(Dispatchers.IO){
            val dateFrom: Date = DateUtils2.getCurrentMonthStart()
            val dateTo: Date = DateUtils2.getCurrentMonthEnd()
            val rangeRecordWithTypes =
                DbHelper.db.recordDao().getRangeRecordWithTypes(dateFrom, dateTo)
            withContext(Dispatchers.Main){
                rangeRecordWithTypes?.collect{recordWithTypes->
                    mAdapter?.setData(recordWithTypes as MutableList<RecordWithType>?)
                }
            }
        }
    }

    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.addRecord -> {
                startActivity(AddRecordActivity::class.java)
            }
            R.id.statistics_view->{
                startActivity(ViewStatisticsActivity::class.java)
            }
            else -> {}
        }
    }
}