package com.recordThings.mobile.db.daos

import androidx.room.*
import com.recordThings.mobile.db.entities.Inspiration
import kotlinx.coroutines.selects.select

@Dao
interface InspirationDao {

    /**
     * 添加灵感
     */
    @Insert
    abstract fun addInspiration(inspiration: Inspiration): Long

    /**
     * 编辑灵感
     */
    @Update
    abstract fun upDateInspiration(inspiration: Inspiration): Int

    /**
     * 删除灵感
     */
    @Delete
    abstract fun delInspiration(inspiration: Inspiration): Int

    /**
     * 查询灵感
     */
    @Query("select * from t_inspiration")
    fun getInspirationList(): List<Inspiration>

    /**
     * 查询灵感数量
     */
    @Query("select count(*) from t_inspiration")
    fun getAllInspirationNum(): Long
}