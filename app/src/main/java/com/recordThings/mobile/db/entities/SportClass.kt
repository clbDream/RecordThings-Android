package com.recordThings.mobile.db.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "t_sport_class")
class SportClass(
        //主键
        @PrimaryKey(autoGenerate = true) val id: Int? = null,
        //分类名
        @ColumnInfo(name = "className") var className: String? = "",
        //分类图标
        @ColumnInfo(name = "classIcon") var classIcon: String? = "",
        //是否选中
        var isChecked: Boolean = false
) : Parcelable