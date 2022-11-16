package com.recordThings.mobile.other

import androidx.room.TypeConverter
import java.math.BigDecimal
import java.util.*

object Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun stringToBig(intDecimal: Int): BigDecimal {
        return BigDecimal(intDecimal)
    }

    @TypeConverter
    fun bigToString(bigDecimal: BigDecimal): Int {
        return bigDecimal.toInt()
    }
}