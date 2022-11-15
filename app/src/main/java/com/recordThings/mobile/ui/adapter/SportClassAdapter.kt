package com.recordThings.mobile.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.recordThings.mobile.R
import com.recordThings.mobile.app.AppAdapter
import com.recordThings.mobile.db.entities.SportClass

class SportClassAdapter(val mContext: Context) : AppAdapter<SportClass>(mContext) {
    private var mCurrentCheckPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.item_sport_class) {
        private val sportIcon: ImageView? by lazy { findViewById(R.id.sport_icon) }
        private val sportName: TextView? by lazy { findViewById(R.id.sport_name) }
        private val check_item: ImageView? by lazy { findViewById(R.id.check_item) }

        override fun onBindView(position: Int) {
            val item = getItem(position)

            val iconId = mContext.getResources().getIdentifier(item.classIcon, "drawable", mContext.getPackageName());
            sportIcon?.setImageResource(iconId)

            sportName?.text = item.className

            check_item?.visibility = if (item.isChecked) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    fun checkItem(position: Int) {
        getData().forEachIndexed { index, sportClass ->
            sportClass.isChecked = index == position
        }
        mCurrentCheckPosition = position
        notifyDataSetChanged()
    }

    /**
     * 获取当前选中的 item
     */
    fun getCurrentItem(): SportClass? {
        if (mCurrentCheckPosition == -1) return null
        return getItem(mCurrentCheckPosition)
    }
}