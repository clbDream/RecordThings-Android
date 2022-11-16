package com.recordThings.mobile.db.daos


import androidx.room.*
import com.recordThings.mobile.db.entities.*
import kotlinx.coroutines.flow.Flow
import java.util.*


/**
 * 记账记录表操作类
 *
 * @author Bakumon https://bakumon.me
 */
@Dao
interface RecordDao {
    @Transaction
    @Query("SELECT * from record WHERE time BETWEEN :from AND :to ORDER BY time DESC, create_time DESC")
    fun getRangeRecordWithTypes(from: Date?, to: Date?): Flow<List<RecordWithType?>?>?

    @Transaction
    @Query("SELECT record.* from record LEFT JOIN RecordType ON record.record_type_id=RecordType.id WHERE (RecordType.type=:type AND time BETWEEN :from AND :to) ORDER BY time DESC, create_time DESC")
    fun getRangeRecordWithTypes(from: Date?, to: Date?, type: Int): Flow<List<RecordWithType?>?>?

    @Transaction
    @Query("SELECT record.* from record LEFT JOIN RecordType ON record.record_type_id=RecordType.id WHERE (RecordType.type=:type AND record.record_type_id=:typeId AND time BETWEEN :from AND :to) ORDER BY time DESC, create_time DESC")
    fun getRangeRecordWithTypes(from: Date?, to: Date?, type: Int, typeId: Int): Flow<List<RecordWithType?>?>?

    @Transaction
    @Query("SELECT record.* from record LEFT JOIN RecordType ON record.record_type_id=RecordType.id WHERE (RecordType.type=:type AND record.record_type_id=:typeId AND time BETWEEN :from AND :to) ORDER BY money DESC, create_time DESC")
    fun getRecordWithTypesSortMoney(from: Date?, to: Date?, type: Int, typeId: Int): Flow<List<RecordWithType?>?>?

    @Insert
    fun insertRecord(record: Record?)

    @Update
    fun updateRecords(vararg records: Record?)

    @Delete
    fun deleteRecord(record: Record?)

    @Query("SELECT recordType.type AS type, sum(record.money) AS sumMoney FROM record LEFT JOIN RecordType ON record.record_type_id=RecordType.id WHERE time BETWEEN :from AND :to GROUP BY RecordType.type")
    fun getSumMoney(from: Date?, to: Date?): Flow<List<SumMoneyBean?>?>?

    @Query("SELECT count(id) FROM record WHERE record_type_id = :typeId")
    fun getRecordCountWithTypeId(typeId: Int): Long

    @Query("SELECT * FROM record WHERE record_type_id = :typeId")
    fun getRecordsWithTypeId(typeId: Int): List<Record?>?

    /**
     * 尽量使用 Flow 返回，因为当数据库数据改变时，会自动回调
     * 而直接用 List ，在调用的地方自己写 Flowable 不会自动回调
     */
    @Query("SELECT recordType.type AS type, record.time AS time, sum(record.money) AS daySumMoney FROM record LEFT JOIN RecordType ON record.record_type_id=RecordType.id where (RecordType.type=:type and record.time BETWEEN :from AND :to) GROUP BY record.time")
    fun getDaySumMoney(from: Date?, to: Date?, type: Int): Flow<List<DaySumMoneyBean?>?>?

    @Query("SELECT t_type.img_name AS imgName,t_type.name AS typeName, record.record_type_id AS typeId,sum(record.money) AS typeSumMoney, count(record.record_type_id) AS count FROM record LEFT JOIN RecordType AS t_type ON record.record_type_id=t_type.id where (t_type.type=:type and record.time BETWEEN :from AND :to) GROUP by record.record_type_id Order by sum(record.money) DESC")
    fun getTypeSumMoney(from: Date?, to: Date?, type: Int): Flow<List<TypeSumMoneyBean?>?>?
}
