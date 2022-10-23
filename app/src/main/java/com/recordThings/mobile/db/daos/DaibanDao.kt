package com.recordThings.mobile.db.daos

import androidx.room.*
import com.recordThings.mobile.db.entities.Daiban

@Dao
interface DaibanDao {

    /**
     * 添加待办
     */
    @Insert
    abstract fun addDaiban(daiban: Daiban): Long

    /**
     * 编辑待办
     */
    @Update
    abstract fun upDateDaiban(daiban: Daiban): Int

    /**
     * 删除待办
     */
    @Delete
    abstract fun delDaiban(daiban: Daiban): Int

    /**
     * 查询待办(待完成)
     * @param status true:已完成  false:待完成
     */
    @Query("select * from t_daiban where is_finish=:status order by create_time ")
    fun getDaibanList(status: Boolean): List<Daiban>

    /**
     * 查询待办数量
     * @param status true:已完成  false:待完成
     */
    @Query("select count(*) from t_daiban where is_finish=:status")
    fun getAllDaibanNum(status: Boolean): Long
}