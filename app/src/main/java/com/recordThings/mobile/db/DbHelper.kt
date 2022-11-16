package com.recordThings.mobile.db

import android.content.Context
import androidx.room.Room
import com.recordThings.mobile.db.datasource.RecordTypeInitCreator
import com.recordThings.mobile.db.datasource.SportClassInitCreator
import kotlin.concurrent.thread

/**
 * 数据库工具类
 */
object DbHelper {

    lateinit var db: AppDataBase

    fun init(context: Context) {
        db = Room.databaseBuilder(context, AppDataBase::class.java, "Record_Things_db")
                .fallbackToDestructiveMigration()
                .build()
    }

    /**
     * 初始化运动分类数据
     */
    fun initSportClass() {
        thread {
            if (db.sportDao().getSportClassCount() < 1) {
                db.sportDao().insertSportClass(SportClassInitCreator.createSportClassData())
            }
            if (db.recordTypeDao().recordTypeCount < 1) {
                db.recordTypeDao().insertRecordTypes(RecordTypeInitCreator.createRecordTypeData())
            }
        }
    }

}