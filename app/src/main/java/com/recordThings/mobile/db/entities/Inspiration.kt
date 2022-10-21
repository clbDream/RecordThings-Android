package com.recordThings.mobile.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "t_inspiration")
data class Inspiration(
    //主键
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    //灵感内容
    @ColumnInfo(name = "content") var content: String? = "",
)
