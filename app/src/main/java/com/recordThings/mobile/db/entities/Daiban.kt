package com.recordThings.mobile.db.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "t_daiban")
class Daiban(
    //主键
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    //待办内容
    @ColumnInfo(name = "content") var content: String? = "",
    //是否完成
    @ColumnInfo(name = "is_finish") var is_finish: Boolean? = false,
    //提醒时间
    @ColumnInfo(name = "reminder_time") var reminder_time: Long = 0,
    //创建时间
    @ColumnInfo(name = "create_time") var create_time: Long = System.currentTimeMillis(),
    //更新时间
    @ColumnInfo(name = "update_time") var update_time: Long = System.currentTimeMillis(),
) : Parcelable