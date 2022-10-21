package com.recordThings.mobile.db.daos

import androidx.room.*
import com.recordThings.mobile.db.entities.Inspiration

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
}