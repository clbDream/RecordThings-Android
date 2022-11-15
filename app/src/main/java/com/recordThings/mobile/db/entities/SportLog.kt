package com.recordThings.mobile.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "t_sport_log")
class SportLog(
        //主键
        @PrimaryKey(autoGenerate = true)
        val id: Int? = null,

        //运动分类名
        @ColumnInfo(name = "class_name")
        val class_name: String? = "",

        //运动分类ID
        @ColumnInfo(name = "class_id")
        val class_id: Int? = 0,

        //运动分类图标
        @ColumnInfo(name = "class_icon")
        val class_icon: String? = "",

        //运动时长
        @ColumnInfo(name = "sport_time")
        val sport_time: Long = 0,

        //运动创建时间
        @ColumnInfo(name = "create_time")
        val create_time: Long = 0,
)