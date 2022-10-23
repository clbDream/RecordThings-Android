package com.recordThings.mobile.ui.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.ActivityUtils
import com.lxj.xpopup.XPopup
import com.recordThings.mobile.R
import com.recordThings.mobile.app.AppAdapter
import com.recordThings.mobile.db.DbHelper
import com.recordThings.mobile.db.entities.Daiban
import com.recordThings.mobile.ui.activity.EditDaibanActivity
import com.recordThings.mobile.utils.DateUtils
import java.util.*
import kotlin.concurrent.thread


class DaibanListAdapter(val mContext: Context) : AppAdapter<Daiban>(mContext) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.item_daiban) {

        private val tvTime: TextView? by lazy { findViewById(R.id.tv_time) }
        private val daiban_content: TextView? by lazy { findViewById(R.id.daiban_content) }
        private val show_tixing: ImageView? by lazy { findViewById(R.id.show_tixing) }

        override fun onBindView(position: Int) {
            val daiban = getItem(position)
            tvTime?.text = DateUtils.dateTurnBefore(Date(daiban.create_time), true)
            daiban_content?.text = daiban.content
            if (daiban.reminder_time > 0) {
                show_tixing?.visibility = View.VISIBLE
            } else {
                show_tixing?.visibility = View.INVISIBLE
            }
//            if (daiban.is_finish == true) {
//                daiban_content?.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
//            } else {
//                daiban_content?.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG.inv()
//            }

            getItemView().setOnLongClickListener {
                showMenu(it, position)
                true
            }
        }
    }

    private fun showMenu(view: View, position: Int) {
        val daiban = getItem(position)
        val menuList = if (daiban.is_finish == true) {
            arrayOf("未完成", "删除")
        } else {
            arrayOf("已完成", "删除", "修改")
        }
        XPopup.Builder(mContext)
            .isDestroyOnDismiss(true)
            .hasShadowBg(false)
            .atView(view)
            .asAttachList(
                menuList, null
            ) { _, text ->
                when (text) {
                    "已完成" -> {
                        thread {
                            daiban.is_finish = true
                            DbHelper.db.daibanDao().upDateDaiban(daiban)
                        }
                        removeItem(daiban)
                    }
                    "未完成" -> {
                        thread {
                            daiban.is_finish = false
                            DbHelper.db.daibanDao().delDaiban(daiban)
                        }
                        removeItem(daiban)
                    }
                    "删除" -> {
                        thread {
                            DbHelper.db.daibanDao().delDaiban(daiban)
                        }
                        removeItem(daiban)
                    }
                    "修改" -> {
                        val bundle = Bundle()
                        bundle.putParcelable("data", getItem(position))
                        val intent = Intent(mContext, EditDaibanActivity::class.java)
                        intent.putExtras(bundle)
                        ActivityUtils.startActivity(intent)
                    }
                    else -> {}
                }
            }.show()
    }
}