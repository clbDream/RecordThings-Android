package com.recordThings.mobile.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.recordThings.mobile.db.daos.DaibanDao
import com.recordThings.mobile.db.daos.InspirationDao
import com.recordThings.mobile.db.daos.SportDao
import com.recordThings.mobile.db.entities.Daiban
import com.recordThings.mobile.db.entities.Inspiration
import com.recordThings.mobile.db.entities.SportClass
import com.recordThings.mobile.db.entities.SportLog

@Database(entities = [Inspiration::class, Daiban::class, SportClass::class, SportLog::class], version = 3, autoMigrations = [
    AutoMigration(from = 1, to = 2),
    AutoMigration(from = 2, to = 3),
])
abstract class AppDataBase : RoomDatabase() {

    abstract fun inspirationDao(): InspirationDao

    abstract fun daibanDao(): DaibanDao

    abstract fun sportDao(): SportDao

}