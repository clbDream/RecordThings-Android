package com.recordThings.mobile.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.recordThings.mobile.R
import com.recordThings.mobile.app.AppAdapter
import com.recordThings.mobile.db.entities.RecordWithType
import com.recordThings.mobile.utils.DateUtils2

class HomeAdapter(mContext: Context) : AppAdapter<RecordWithType>(mContext) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.item_home) {

        private val tv_date: TextView? by lazy { findViewById(R.id.tv_date) }
        private val name_type: TextView? by lazy { findViewById(R.id.name_type) }
        private val desc_beizhu: TextView? by lazy { findViewById(R.id.desc_beizhu) }
        private val jine: TextView? by lazy { findViewById(R.id.jine) }
        private val icon_res: ImageView? by lazy { findViewById(R.id.icon_res) }

        override fun onBindView(position: Int) {
            val recordWithType = getItem(position)
            val isDataShow = position == 0 ||
                    !DateUtils2.isSameDay(recordWithType.time, getData()[position - 1].time)

            if (isDataShow) {
                tv_date?.visibility = View.VISIBLE
            } else {
                tv_date?.visibility = View.INVISIBLE
            }
//            name_type?.text = recordWithType.mRecordTypes
        }
    }
}