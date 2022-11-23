package com.recordThings.mobile.ui.fragment

import android.graphics.Color
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.recordThings.mobile.R
import com.recordThings.mobile.app.AppFragment
import com.recordThings.mobile.db.DbHelper
import com.recordThings.mobile.db.entities.RecordType
import com.recordThings.mobile.db.entities.TypeSumMoneyBean
import com.recordThings.mobile.ui.activity.ViewStatisticsActivity
import com.recordThings.mobile.utils.BigDecimalUtil
import com.recordThings.mobile.utils.DateUtils2
import com.recordThings.mobile.utils.PieEntryConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ReportsFragment: AppFragment<ViewStatisticsActivity>() {

    var mYear = 0
    var mMonth = 0
    var mType = 0
    private val rg_type: RadioGroup? by lazy { findViewById(R.id.rg_type) }
    private val rb_outlay: RadioButton? by lazy { findViewById(R.id.rb_outlay) }
    private val rb_income: RadioButton? by lazy { findViewById(R.id.rb_income) }
    private val pie_chart: PieChart? by lazy { findViewById(R.id.pie_chart) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_reports
    }

    override fun initView() {
        mYear = DateUtils2.getCurrentYear();
        mMonth = DateUtils2.getCurrentMonth();
        mType = RecordType.TYPE_OUTLAY;

        initPieChart()

        rg_type?.setOnCheckedChangeListener { group, checkedId ->
            mType = if (checkedId == R.id.rb_outlay) {
                RecordType.TYPE_OUTLAY
            } else {
                RecordType.TYPE_INCOME
            }
            getTypeSumMoney()
            getMonthSumMoney()
        }
        rg_type?.check(R.id.rb_outlay)
    }

    private fun initPieChart() {
        pie_chart?.getDescription()?.setEnabled(false)
        pie_chart?.setNoDataText("")
        pie_chart?.setUsePercentValues(true)
        pie_chart?.setDrawHoleEnabled(false)
        pie_chart?.setRotationEnabled(false)

        pie_chart?.getLegend()?.setEnabled(false)
        pie_chart?.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry, h: Highlight) {
                val typeName: String? = (e.data as TypeSumMoneyBean).typeName
                val typeId: Int = (e.data as TypeSumMoneyBean).typeId
//                navTypeRecords(typeName, typeId)
            }

            override fun onNothingSelected() {}
        })
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
        // 更新数据
        getTypeSumMoney()
        getMonthSumMoney()
    }

    private fun getTypeSumMoney() {
        lifecycleScope.launch(Dispatchers.IO) {
            val dateFrom: Date = DateUtils2.getMonthStart(mYear, mMonth)
            val dateTo: Date = DateUtils2.getMonthEnd(mYear, mMonth)
            val typeSumMoney = DbHelper.db.recordDao().getTypeSumMoney(dateFrom, dateTo, mType)
            typeSumMoney?.collect{typeSumMoneyBeans->
                withContext(Dispatchers.Main){
                    setChartData(typeSumMoneyBeans)
//                    mAdapter.setNewData(typeSumMoneyBeans)
//                    if (typeSumMoneyBeans == null || typeSumMoneyBeans.isEmpty()) {
//                        mAdapter.setEmptyView(inflate(R.layout.layout_statistics_empty))
//                    }
                }
            }

        }
    }

    private fun setChartData(typeSumMoneyBeans: List<TypeSumMoneyBean?>?) {
        if (typeSumMoneyBeans == null || typeSumMoneyBeans.size < 1) {
            pie_chart?.setVisibility(View.INVISIBLE)
            return
        } else {
            pie_chart?.setVisibility(View.VISIBLE)
        }
        val entries: List<PieEntry> = PieEntryConverter.getBarEntryList(typeSumMoneyBeans)
        val dataSet: PieDataSet
        if (pie_chart?.getData() != null && pie_chart?.getData()?.getDataSetCount()!! > 0
        ) {
            dataSet = pie_chart?.getData()?.getDataSetByIndex(0) as PieDataSet
            dataSet.values = entries
            pie_chart?.getData()?.notifyDataChanged()
            pie_chart?.notifyDataSetChanged()
        } else {
            dataSet = PieDataSet(entries, "")
            dataSet.sliceSpace = 0f
            dataSet.selectionShift = 1.2f
            dataSet.valueLinePart1Length = 0.3f
            dataSet.valueLinePart2Length = 1f
            dataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
            dataSet.yValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
            dataSet.valueTextSize = 10f
            dataSet.isValueLineVariableLength = true
            dataSet.valueLineColor = resources.getColor(R.color.colorTextWhite)
            val color: List<Int>
            color = if (entries.size % 7 == 0) {
                ColorTemplate.createColors(
                    resources, intArrayOf(
                        R.color.colorPieChart1, R.color.colorPieChart2,
                        R.color.colorPieChart3, R.color.colorPieChart4,
                        R.color.colorPieChart5, R.color.colorPieChart6,
                        R.color.colorPieChart7
                    )
                )
            } else {
                ColorTemplate.createColors(
                    resources, intArrayOf(
                        R.color.colorPieChart1, R.color.colorPieChart2,
                        R.color.colorPieChart3, R.color.colorPieChart4,
                        R.color.colorPieChart5, R.color.colorPieChart6
                    )
                )
            }
            dataSet.colors = color
            val data = PieData(dataSet)
            data.setValueFormatter(PercentFormatter())
            data.setValueTextSize(10f)
            data.setValueTextColor(Color.WHITE)
            pie_chart?.setData(data)
        }
        // undo all highlights
        pie_chart?.highlightValues(null)
        pie_chart?.invalidate()
        pie_chart?.animateY(1000)
    }

    private fun getMonthSumMoney() {
        lifecycleScope.launch(Dispatchers.IO) {
            val dateFrom: Date = DateUtils2.getMonthStart(mYear, mMonth)
            val dateTo: Date = DateUtils2.getMonthEnd(mYear, mMonth)
            val typeSumMoney = DbHelper.db.recordDao().getSumMoney(dateFrom, dateTo)
            typeSumMoney?.collect { sumMoneyBean ->
                withContext(Dispatchers.Main) {
                    var outlay = getString(R.string.text_month_outlay_symbol) + "0"
                    var income = getString(R.string.text_month_income_symbol) + "0"
                    if (sumMoneyBean != null && sumMoneyBean.size > 0) {
                        for (bean in sumMoneyBean) {
                            if (bean?.type == RecordType.TYPE_OUTLAY) {
                                outlay =
                                    getString(R.string.text_month_outlay_symbol) + BigDecimalUtil.fen2Yuan(
                                        bean?.sumMoney
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
}