package com.recordThings.mobile.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.recordThings.mobile.db.entities.SportClass
import com.recordThings.mobile.db.entities.SportLog

@Dao
interface SportDao {

    /**
     * 查询分类数量
     */
    @Query("SELECT count(className) FROM t_sport_class")
    fun getSportClassCount(): Long

    /**
     * 查询总运动次数
     */
    @Query("SELECT count(*) FROM t_sport_log")
    fun getSportCount(): Long

    /**
     * 添加运动分类
     */
    @Insert
    fun insertSportClass(recordTypes: Array<SportClass>)

    /**
     * 添加运动分类
     */
    @Insert
    fun insertSportClass(recordType: SportClass)

    /**
     * 查询所有运动分类
     */
    @Query("select * from t_sport_class")
    fun getAllSportClass(): List<SportClass>

    /**
     * 添加运动记录
     */
    @Insert
    fun addSportLog(sportLog: SportLog): Long
}