package com.recordThings.mobile.db.entities

import androidx.room.*
import java.io.Serializable
import java.math.BigDecimal
import java.util.*

/**
 * 记账记录
 *
 */
@Entity(foreignKeys = [ForeignKey(entity = RecordType::class, parentColumns = ["id"], childColumns = ["record_type_id"])], indices = [Index(value = ["record_type_id", "time", "money", "create_time"])])
open class Record : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    var money: BigDecimal? = null
    var remark: String? = null
    var time: Date? = null

    @ColumnInfo(name = "create_time")
    var createTime: Date? = null

    @ColumnInfo(name = "record_type_id")
    var recordTypeId = 0
}
