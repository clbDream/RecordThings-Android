package com.recordThings.mobile.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.recordThings.mobile.db.daos.*
import com.recordThings.mobile.db.entities.*
import com.recordThings.mobile.other.Converters

@Database(entities = [Inspiration::class, Daiban::class, SportClass::class, SportLog::class, Record::class, RecordType::class], version = 4, autoMigrations = [
    AutoMigration(from = 1, to = 2),
    AutoMigration(from = 2, to = 3),
    AutoMigration(from = 3, to = 4),
])
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun inspirationDao(): InspirationDao

    abstract fun daibanDao(): DaibanDao

    abstract fun sportDao(): SportDao

    /**
     * 获取记账类型操作类
     *
     * @return RecordTypeDao 记账类型操作类
     */
    abstract fun recordTypeDao(): RecordTypeDao

    /**
     * 获取记账操作类
     *
     * @return RecordDao 记账操作类
     */
    abstract fun recordDao(): RecordDao
}