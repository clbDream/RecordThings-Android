package com.recordThings.mobile.db.daos

import androidx.room.*
import com.recordThings.mobile.db.entities.RecordType
import kotlinx.coroutines.flow.Flow


/**
 * 记账类型表操作类
 *
 * @author Bakumon https://bakumon.me
 */
@Dao
interface RecordTypeDao {
    @get:Query("SELECT * FROM recordtype WHERE state = 0 ORDER BY ranking")
    val allRecordTypes: Flow<List<RecordType?>?>?

    @Query("SELECT * FROM recordtype WHERE state = 0 AND type = :type ORDER BY ranking")
    fun getRecordTypes(type: Int): Flow<List<RecordType?>?>?

    @get:Query("SELECT count(recordtype.id) FROM recordtype")
    val recordTypeCount: Long

    @Query("SELECT * FROM recordtype WHERE type = :type AND name = :name")
    fun getTypeByName(type: Int, name: String?): RecordType?

    @Insert
    fun insertRecordType(recordTypes: RecordType)

    @Insert
    fun insertRecordTypes(recordTypes: Array<RecordType>)

    @Update
    fun updateRecordTypes(vararg recordTypes: RecordType?)

    @Delete
    fun deleteRecordType(recordType: RecordType?)
}
