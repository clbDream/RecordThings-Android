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

@Database(entities = [Inspiration::class, Daiban::class, SportClass::class], version = 2, autoMigrations = [
    AutoMigration(from = 1, to = 2)
])
abstract class AppDataBase : RoomDatabase() {

    abstract fun inspirationDao(): InspirationDao

    abstract fun daibanDao(): DaibanDao

    abstract fun sportDao(): SportDao

}