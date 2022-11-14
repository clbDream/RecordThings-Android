package com.recordThings.mobile.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.recordThings.mobile.db.entities.SportClass

@Dao
interface SportDao {

    /**
     * 查询分类数量
      */
    @Query("SELECT count(className) FROM t_sport_class")
    fun getSportClassCount(): Long

    @Insert
    fun insertSportClass(recordTypes: Array<SportClass>)

}