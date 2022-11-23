package com.recordThings.mobile.ui.fragment

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.recordThings.mobile.R
import com.recordThings.mobile.app.AppFragment
import com.recordThings.mobile.db.DbHelper
import com.recordThings.mobile.db.entities.DaySumMoneyBean
import com.recordThings.mobile.db.entities.RecordType
import com.recordThings.mobile.ui.activity.ViewStatisticsActivity
import com.recordThings.mobile.utils.BarEntryConverter
import com.recordThings.mobile.utils.BigDecimalUtil
import com.recordThings.mobile.utils.DateUtils2
import com.recordThings.mobile.widget.BarChartMarkerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class BillFragment : AppFragment<ViewStatisticsActivity>() {

    var mYear = 0
    var mMonth = 0
    var mType = 0

    private val rg_type: RadioGroup? by lazy { findViewById(R.id.rg_type) }
    private val rb_outlay: RadioButton? by lazy { findViewById(R.id.rb_outlay) }
    private val rb_income: RadioButton? by lazy { findViewById(R.id.rb_income) }
    private val bar_chart: BarChart? by lazy { findViewById(R.id.bar_chart) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_bill
    }

    override fun initView() {
        mYear = DateUtils2.getCurrentYear();
        mMonth = DateUtils2.getCurrentMonth();
        mType = RecordType.TYPE_OUTLAY;

        initBarChart()

        rg_type?.setOnCheckedChangeListener { group, checkedId ->
            mType = if (checkedId == R.id.rb_outlay) {
                RecordType.TYPE_OUTLAY
            } else {
                RecordType.TYPE_INCOME
            }
//            getOrderData()
            getDaySumData()
            getMonthSumMoney()
        }
        rg_type?.check(R.id.rb_outlay)
    }

    private fun getMonthSumMoney() {
        lifecycleScope.launch(Dispatchers.IO) {
            val dateFrom: Date = DateUtils2.getMonthStart(mYear, mMonth)
            val dateTo: Date = DateUtils2.getMonthEnd(mYear, mMonth)
            val sumMoney = DbHelper.db.recordDao().getSumMoney(dateFrom, dateTo)
            sumMoney?.collect{sumMoneyBean->
                withContext(Dispatchers.Main){
                    var outlay = getString(R.string.text_month_outlay_symbol) + "0"
                    var income = getString(R.string.text_month_income_symbol) + "0"
                    if (sumMoneyBean != null && sumMoneyBean.size > 0) {
                        for (bean in sumMoneyBean) {
                            if (bean?.type == RecordType.TYPE_OUTLAY) {
                                outlay =
                                    getString(R.string.text_month_outlay_symbol) + BigDecimalUtil.fen2Yuan(
                                        bean.sumMoney
                                    )
                            } else if (bean?.type == RecordType.TYPE_INCOME) {
                                income =
                                    getString(R.string.text_month_income_symbol) + BigDecimalUtil.fen2Yuan(
                                        bean.sumMoney
                                    )
                            }
                        }
                    }
                    rb_outlay?.setText(outlay)
                    rb_income?.setText(income)
                }
            }
        }
    }

    private fun getDaySumData() {
        lifecycleScope.launch(Dispatchers.IO) {

            val dateFrom: Date = DateUtils2.getMonthStart(mYear, mMonth)
            val dateTo: Date = DateUtils2.getMonthEnd(mYear, mMonth)
            val daySumMoney = DbHelper.db.recordDao().getDaySumMoney(dateFrom, dateTo, mType)
            daySumMoney?.collect {
                withContext(Dispatchers.Main){
                    setChartData(it)
                }
            }
        }
    }

    private fun initBarChart() {
        bar_chart?.setNoDataText("")
        bar_chart?.setScaleEnabled(false)
        bar_chart?.getDescription()?.setEnabled(false)
        bar_chart?.getLegend()?.setEnabled(false)
        bar_chart?.getAxisLeft()?.setAxisMinimum(0F)
        bar_chart?.getAxisLeft()?.setEnabled(false)
        bar_chart?.getAxisRight()?.setEnabled(false)
        val xAxis: XAxis? = bar_chart?.getXAxis()
        xAxis?.position = XAxis.XAxisPosition.BOTTOM
        xAxis?.setDrawGridLines(false)
        xAxis?.textColor = resources.getColor(R.color.colorTextGray)
        xAxis?.labelCount = 5
        val mv = BarChartMarkerView(context)
        mv.setChartView(bar_chart)
        bar_chart?.setMarker(mv)
    }

    override fun initData() {

    }

    /**
     * 设置月份
     */
    fun setYearMonth(year: Int, month: Int) {
        if (year == mYear && month == mMonth) {
            return
        }
        mYear = year
        mMonth = month
        // 更新数据
//        getOrderData()
        getDaySumData()
        getMonthSumMoney()
    }

    private fun setChartData(daySumMoneyBeans: List<DaySumMoneyBean?>?) {
        if (daySumMoneyBeans == null || daySumMoneyBeans.size < 1) {
            bar_chart?.setVisibility(View.INVISIBLE)
            return
        } else {
            bar_chart?.setVisibility(View.VISIBLE)
        }
        val count: Int = DateUtils2.getDayCount(mYear, mMonth)
        val barEntries: List<BarEntry> = BarEntryConverter.getBarEntryList(count, daySumMoneyBeans)
        val set1: BarDataSet
        if (bar_chart?.getData() != null && bar_chart?.getData()!!
                .getDataSetCount() > 0
        ) {
            set1 = bar_chart?.getData()?.getDataSetByIndex(0) as BarDataSet
            set1.values = barEntries
            bar_chart?.getData()?.notifyDataChanged()
            bar_chart?.notifyDataSetChanged()
        } else {
            set1 = BarDataSet(barEntries, "")
            set1.setDrawIcons(false)
            set1.setDrawValues(false)
            set1.color = resources.getColor(R.color.colorAccent)
            set1.valueTextColor = resources.getColor(R.color.colorTextWhite)
            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(set1)
            val data = BarData(dataSets)
            data.barWidth = 0.5f
            data.isHighlightEnabled = true
            bar_chart?.setData(data)
        }
        bar_chart?.invalidate()
        bar_chart?.animateY(1000)
    }
}