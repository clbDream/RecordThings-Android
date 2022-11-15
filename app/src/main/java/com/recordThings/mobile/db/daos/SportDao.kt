package com.recordThings.mobile.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.recordThings.mobile.db.entities.SportClass
import com.recordThings.mobile.db.entities.SportLog
import kotlinx.coroutines.flow.Flow

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

    /**
     * 查询所有运动记录
     */
    @Query("select * from t_sport_log order by create_time desc")
    fun getAllSportLog(): Flow<List<SportLog>>

    /**
     * 查询所有运动时长
     */
    @Query("select sum(sport_time) from t_sport_log")
    fun getAllSportTime(): Flow<Long>
}