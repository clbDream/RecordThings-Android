package com.recordThings.mobile.ui.activity

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hjq.shape.layout.ShapeRadioGroup
import com.hjq.widget.view.FloatActionButton
import com.recordThings.mobile.R
import com.recordThings.mobile.app.AppActivity
import com.recordThings.mobile.db.DbHelper
import com.recordThings.mobile.db.entities.Daiban
import com.recordThings.mobile.ui.adapter.DaibanListAdapter
import kotlin.concurrent.thread

class DaibanListActivity : AppActivity() {

    private lateinit var daibanListAdapter: DaibanListAdapter
    private val btn_send: FloatActionButton? by lazy { findViewById(R.id.btn_send) }
    private val daiban_list: RecyclerView? by lazy { findViewById(R.id.daiban_list) }
    private val shapeRadioGroup: ShapeRadioGroup? by lazy { findViewById(R.id.shapeRadioGroup) }

    override fun getLayoutId(): Int {
        return R.layout.activity_daiban_list
    }

    override fun initView() {
        setOnClickListener(btn_send)
        daiban_list?.let {
            it.layoutManager = LinearLayoutManager(this)
            daibanListAdapter = DaibanListAdapter(this)
            it.adapter = daibanListAdapter
        }
        shapeRadioGroup?.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.rb_1 -> {
                    getDaibanList(false)
                }
                R.id.rb_2 -> {
                    getDaibanList(true)
                }
                else -> {}
            }
        }
    }

    override fun initData() {

    }

    override fun onClick(view: View) {
        super.onClick(view)
        when (view) {
            btn_send -> {
                startActivity(EditDaibanActivity::class.java)
            }
            else -> {}
        }
    }

    override fun onResume() {
        super.onResume()
        getDaibanList(false)
    }

    /**
     * 获取待办列表
     * @param status true:已完成  false:待完成
     */
    private fun getDaibanList(status: Boolean) {
        thread {
            val daiBanListData = DbHelper.db.daibanDao().getDaibanList(status) as ArrayList<Daiban>
            runOnUiThread {
                daibanListAdapter.setData(daiBanListData)
            }
        }
    }

    override fun isStatusBarEnabled(): Boolean {
        return false
    }
}