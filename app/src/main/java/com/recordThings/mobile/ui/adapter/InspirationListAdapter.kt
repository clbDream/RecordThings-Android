package com.recordThings.mobile.ui.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.blankj.utilcode.util.ActivityUtils.startActivity
import com.hjq.shape.layout.ShapeConstraintLayout
import com.lxj.xpopup.XPopup
import com.recordThings.mobile.R
import com.recordThings.mobile.app.AppAdapter
import com.recordThings.mobile.db.DbHelper
import com.recordThings.mobile.db.entities.Inspiration
import com.recordThings.mobile.ui.activity.EditInspirationActivity
import com.recordThings.mobile.utils.DateUtils
import java.util.*
import kotlin.concurrent.thread

class InspirationListAdapter(val mContext: Context) : AppAdapter<Inspiration>(mContext) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.item_linggan) {

        private val linggan_content: TextView? by lazy { findViewById(R.id.linggan_content) }
        private val tv_time: TextView? by lazy { findViewById(R.id.tv_time) }
        private val layout_view: ShapeConstraintLayout? by lazy { findViewById(R.id.layout_view) }

        override fun onBindView(position: Int) {
            val inspiration = getItem(position)
            linggan_content?.text = inspiration.content
            tv_time?.text = DateUtils.dateTurnBefore(Date(inspiration.update_time), true)

            layout_view?.shapeDrawableBuilder?.setSolidColor(Color.parseColor(inspiration.item_bg_color))
                ?.intoBackground()
            linggan_content?.setTextColor(Color.parseColor(inspiration.text_color))
            tv_time?.setTextColor(Color.parseColor(inspiration.text_color))

            getItemView().setOnLongClickListener {
                showMenu(it,position)
                true
            }
        }
    }

    private fun showMenu(view: View, position: Int) {
        XPopup.Builder(mContext)
            .isDestroyOnDismiss(true)
            .hasShadowBg(false)
            .atView(view)
            .asAttachList(
                arrayOf("修改", "删除"), null
            ) { _, text ->
                when (text) {
                    "修改" -> {
                        val bundle = Bundle()
                        bundle.putParcelable("data",getItem(position))
                        val intent = Intent(mContext,EditInspirationActivity::class.java)
                        intent.putExtras(bundle)
                        startActivity( intent)
                    }
                    "删除" -> {
                        thread {
                            DbHelper.db.inspirationDao().delInspiration(getItem(position))
                        }
                        removeItem(getItem(position))
                    }
                    else -> {}
                }
            }.show()
    }
}