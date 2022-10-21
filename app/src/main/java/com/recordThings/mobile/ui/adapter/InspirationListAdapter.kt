package com.recordThings.mobile.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.widget.TextView
import com.hjq.shape.layout.ShapeConstraintLayout
import com.recordThings.mobile.R
import com.recordThings.mobile.app.AppAdapter
import com.recordThings.mobile.db.entities.Inspiration
import com.recordThings.mobile.utils.DateUtils
import java.util.*

class InspirationListAdapter(mContext: Context) : AppAdapter<Inspiration>(mContext) {
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
        }
    }
}