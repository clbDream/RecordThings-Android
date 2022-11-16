package com.recordThings.mobile.db.entities

import androidx.room.*
import java.io.Serializable


/**
 * 记账类型
 *
 * @author bakumon https://bakumon.me
 */
@Entity(indices = [Index(value = ["type", "ranking", "state"])])
class RecordType : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    var name: String

    /**
     * 图片 name（本地mipmap）
     */
    @ColumnInfo(name = "img_name")
    var imgName: String

    /**
     * 类型
     * 0：支出
     * 1：收入
     *
     * @see RecordType.TYPE_OUTLAY
     *
     * @see RecordType.TYPE_INCOME
     */
    var type: Int

    /**
     * 排序
     */
    var ranking: Long = 0

    /**
     * 状态
     * 0：正常
     * 1：已删除
     *
     * @see RecordType.STATE_NORMAL
     *
     * @see RecordType.STATE_DELETED
     */
    var state = 0

    /**
     * 是否选中，用于 UI
     */
    @Ignore
    var isChecked = false

    @Ignore
    constructor(name: String, imgName: String, type: Int) {
        this.name = name
        this.imgName = imgName
        this.type = type
    }

    @Ignore
    constructor(name: String, imgName: String, type: Int, ranking: Long) {
        this.name = name
        this.imgName = imgName
        this.type = type
        this.ranking = ranking
    }

    constructor(id: Int, name: String, imgName: String, type: Int, ranking: Long) {
        this.id = id
        this.name = name
        this.imgName = imgName
        this.type = type
        this.ranking = ranking
    }

    companion object {
        @Ignore
        var TYPE_OUTLAY = 0

        @Ignore
        var TYPE_INCOME = 1

        @Ignore
        var STATE_NORMAL = 0

        @Ignore
        var STATE_DELETED = 1
    }
}
