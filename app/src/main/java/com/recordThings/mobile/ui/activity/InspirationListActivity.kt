package com.recordThings.mobile.ui.activity

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.recordThings.mobile.R
import com.recordThings.mobile.app.AppActivity
import com.recordThings.mobile.db.DbHelper
import com.recordThings.mobile.db.entities.Inspiration
import com.recordThings.mobile.ui.adapter.InspirationListAdapter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import kotlin.concurrent.thread

class InspirationListActivity : AppActivity() {

    private lateinit var listAdapter: InspirationListAdapter

    private val linggan_list: RecyclerView? by lazy { findViewById(R.id.linggan_list) }
    private val refresh: SmartRefreshLayout? by lazy { findViewById(R.id.refresh) }

    override fun getLayoutId(): Int {
        return R.layout.activity_inspiration_list
    }

    override fun initView() {
        linggan_list?.also {
            it.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            listAdapter = InspirationListAdapter(this)
            it.adapter = listAdapter
        }

        refresh?.setOnRefreshListener {

        }
        refresh?.setOnLoadMoreListener {

        }
    }

    override fun initData() {
        thread {
            val inspirationList =
                DbHelper.db.inspirationDao().getInspirationList() as ArrayList<Inspiration>
            listAdapter.addData(inspirationList)
        }
    }
}