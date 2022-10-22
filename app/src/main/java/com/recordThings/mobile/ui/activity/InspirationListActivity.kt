package com.recordThings.mobile.ui.activity

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.hjq.widget.view.FloatActionButton
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
    private val btn_send: FloatActionButton? by lazy { findViewById(R.id.btn_send) }

    override fun getLayoutId(): Int {
        return R.layout.activity_inspiration_list
    }

    override fun initView() {
        setOnClickListener(btn_send)
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

    }

    override fun onClick(view: View) {
        super.onClick(view)
        when (view) {
            btn_send -> {
                startActivity(EditInspirationActivity::class.java)
            }
            else -> {}
        }
    }

    override fun onResume() {
        super.onResume()
        thread {
            val inspirationList =
                DbHelper.db.inspirationDao().getInspirationList() as ArrayList<Inspiration>
            runOnUiThread {
                listAdapter.setData(inspirationList)
            }
        }
    }
}