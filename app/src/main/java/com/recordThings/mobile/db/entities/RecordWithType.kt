package com.recordThings.mobile.db.entities

import androidx.room.Relation


/**
 * 包含 RecordType 的 Record
 *
 * @author Bakumon https://bakumon.me
 */
class RecordWithType : Record() {
    @Relation(parentColumn = "record_type_id", entityColumn = "id", entity = RecordType::class)
    var mRecordTypes: List<RecordType>? = null
}
