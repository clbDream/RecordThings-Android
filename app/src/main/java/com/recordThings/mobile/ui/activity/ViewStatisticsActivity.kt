package com.recordThings.mobile.ui.activity

import android.view.View
import android.widget.*
import com.hjq.base.BaseDialog
import com.recordThings.mobile.R
import com.recordThings.mobile.app.AppActivity
import com.recordThings.mobile.ui.adapter.ViewPagerAdapter
import com.recordThings.mobile.ui.dialog.DateDialog
import com.recordThings.mobile.ui.fragment.BillFragment
import com.recordThings.mobile.ui.fragment.ReportsFragment
import com.recordThings.mobile.utils.DateUtils2
import com.recordThings.mobile.widget.NotPagingViewPager

/**
 * 查看统计信息
 */
class ViewStatisticsActivity : AppActivity() {

    private val ll_title: LinearLayout? by lazy { findViewById(R.id.ll_title) }
    private val iv_title: ImageView? by lazy { findViewById(R.id.iv_title) }
    private val tv_title: TextView? by lazy { findViewById(R.id.tv_title) }
    private val rg_type: RadioGroup? by lazy { findViewById(R.id.rg_type) }
    private val rb_outlay: RadioButton? by lazy { findViewById(R.id.rb_outlay) }
    private val rb_income: RadioButton? by lazy { findViewById(R.id.rb_income) }
    private val view_pager: NotPagingViewPager? by lazy { findViewById(R.id.view_pager) }
    private var mCurrentYear: Int = DateUtils2.getCurrentYear()
    private var mCurrentMonth: Int = DateUtils2.getCurrentMonth()
    private var mBillFragment: BillFragment? = null
    private var mReportsFragment: ReportsFragment? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_view_statistics
    }

    override fun initView() {
        setOnClickListener(
            R.id.ibt_close,
            R.id.iv_title,
        )
        val title: String = DateUtils2.getCurrentYearMonth()
        tv_title?.setText(title)
        tv_title?.setText(title)
        iv_title?.setVisibility(View.VISIBLE)
        rb_outlay?.setText(R.string.text_order)
        rb_income?.setText(R.string.text_reports)

        setUpFragment()
    }

    private fun setUpFragment() {
        val infoPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        mBillFragment = BillFragment()
        mReportsFragment = ReportsFragment()
        infoPagerAdapter.addFragment(mBillFragment)
        infoPagerAdapter.addFragment(mReportsFragment)
        view_pager?.setAdapter(infoPagerAdapter)
        view_pager?.setOffscreenPageLimit(2)
        rg_type?.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rb_outlay) {
                view_pager?.setCurrentItem(0, false)
            } else {
                view_pager?.setCurrentItem(1, false)
            }
        }
        rg_type?.check(R.id.rb_outlay)
    }

    override fun initData() {

    }

    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.ibt_close -> {
                finish()
            }
            R.id.iv_title -> {
                chooseMonth()
            }
            else -> {}
        }
    }

    private fun chooseMonth() {
        ll_title?.setEnabled(false)
        DateDialog.Builder(this, mCurrentYear).setYear(mCurrentYear).setMonth(mCurrentMonth).setIgnoreDay()
            .setListener(object : DateDialog.OnListener {
                override fun onSelected(dialog: BaseDialog?, year: Int, month: Int, day: Int) {
                    mCurrentYear = year
                    mCurrentMonth = month
                    val title: String = DateUtils2.getYearMonthFormatString(year, month)
                    tv_title?.setText(title)
                    mBillFragment?.setYearMonth(year, month)
                    mReportsFragment?.setYearMonth(year, month)
                }
            })
            .show()
    }
}