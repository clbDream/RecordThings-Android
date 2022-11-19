package com.recordThings.mobile.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.recordThings.mobile.R
import com.recordThings.mobile.app.AppAdapter
import com.recordThings.mobile.db.entities.RecordType
import com.recordThings.mobile.utils.DateUtils
import java.util.*

class TypeAdapter(val mContext: Context) : AppAdapter<RecordType>(mContext) {

    private var mCurrentCheckPosition = 0
    private var mCurrentCheckId = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.item_type) {

        private val iv_type: ImageView? by lazy { findViewById(R.id.iv_type) }
        private val iv_check: ImageView? by lazy { findViewById(R.id.iv_check) }
        private val tv_type_name: TextView? by lazy { findViewById(R.id.tv_type_name) }

        override fun onBindView(position: Int) {
            val item = getItem(position)
            tv_type_name?.text = item.name
            iv_type?.setImageResource(mContext.resources.getIdentifier(item.imgName, "drawable", mContext.packageName))
            if (item.isChecked){
                iv_check?.visibility = View.VISIBLE
            }else{
                iv_check?.visibility = View.INVISIBLE
            }
        }

    }

    /**
     * 选中某一个 item，或点击设置 item
     *
     * @param position 选中 item 的索引
     */
    fun clickItem(position: Int) {
        // 点击设置 item
        val item = getItem(position)
        if (item.type == -1) {
//            Floo.navigation(mContext, Router.Url.URL_TYPE_MANAGE)
//                .putExtra(Router.ExtraKey.KEY_TYPE, mType)
//                .start()
            return
        }
        // 选中某一个 item
        var temp: RecordType
        for (i in 0 until getData().size) {
            temp = getData()[i]
            if (temp.type != -1) {
                temp.isChecked = i == position
            }
        }
        mCurrentCheckPosition = position
        mCurrentCheckId = getCurrentItem().id
        notifyDataSetChanged()
    }

    /**
     * 获取当前选中的 item
     */
    fun getCurrentItem(): RecordType {
        return getItem(mCurrentCheckPosition)
    }
}