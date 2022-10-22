package com.recordThings.mobile.db.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "t_inspiration")
data class Inspiration(
    //主键
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    //灵感内容
    @ColumnInfo(name = "content") var content: String? = "",
    //创建时间
    @ColumnInfo(name = "create_time") var create_time: Long = System.currentTimeMillis(),
    //更新时间
    @ColumnInfo(name = "update_time") var update_time: Long = System.currentTimeMillis(),
    //背景颜色
    @ColumnInfo(name = "item_bg_color") var item_bg_color: String = "#03A9F4",
    //灵感文本颜色
    @ColumnInfo(name = "text_color") var text_color: String = "#ffffff",
):Parcelable
