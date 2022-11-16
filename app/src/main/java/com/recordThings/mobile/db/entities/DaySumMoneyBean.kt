package com.recordThings.mobile.db.entities

import java.math.BigDecimal
import java.util.*


/**
 * 某天的支出或收入总和
 *
 * @author Bakumon https://bakumon.me
 */
class DaySumMoneyBean {
    /**
     * 类型
     * 0：支出
     * 1：收入
     *
     * @see RecordType.TYPE_OUTLAY
     *
     * @see RecordType.TYPE_INCOME
     */
    var type = 0
    var time: Date? = null

    /**
     * 支出或收入的总和
     */
    var daySumMoney: BigDecimal? = null
}
