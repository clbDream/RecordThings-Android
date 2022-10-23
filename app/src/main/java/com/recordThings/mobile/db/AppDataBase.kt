package com.recordThings.mobile.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.recordThings.mobile.db.daos.DaibanDao
import com.recordThings.mobile.db.daos.InspirationDao
import com.recordThings.mobile.db.entities.Daiban
import com.recordThings.mobile.db.entities.Inspiration

@Database(entities = [Inspiration::class, Daiban::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun inspirationDao(): InspirationDao

    abstract fun daibanDao(): DaibanDao

}