package com.recordThings.mobile.db

import android.content.Context
import androidx.room.Room

/**
 * 数据库工具类
 */
object DbHelper {

    lateinit var db: AppDataBase

    fun init(context: Context) {
        db = Room.databaseBuilder(context, AppDataBase::class.java, "Record_Things_db")
            .build()
    }

}