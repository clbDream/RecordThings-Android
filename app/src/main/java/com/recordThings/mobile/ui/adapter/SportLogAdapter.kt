package com.recordThings.mobile.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.recordThings.mobile.R
import com.recordThings.mobile.app.AppAdapter
import com.recordThings.mobile.db.entities.SportLog
import com.recordThings.mobile.utils.DateUtils
import java.util.*

class SportLogAdapter(val mContext: Context):AppAdapter<SportLog>(mContext) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder:AppViewHolder(R.layout.item_sport_log){

        private val sport_name:TextView? by lazy { findViewById(R.id.sport_name) }
        private val sport_icon:ImageView? by lazy { findViewById(R.id.sport_icon) }
        private val sport_time:TextView? by lazy { findViewById(R.id.sport_time) }
        private val create_time:TextView? by lazy { findViewById(R.id.create_time) }

        override fun onBindView(position: Int) {
            val sportLog = getItem(position)

            sport_name?.text = sportLog.class_name
            sport_time?.text = "${sportLog.sport_time}分钟"
            sport_icon?.setImageResource(mContext.resources.getIdentifier(sportLog.class_icon, "drawable", mContext.packageName))
            create_time?.text = DateUtils.dateTurnBefore(Date(sportLog.create_time),true)
        }
    }
}