package com.recordThings.mobile.db.entities

import java.math.BigDecimal


/**
 * 支出或收入的总和
 *
 * @author Bakumon https://bakumon.me
 */
class SumMoneyBean {
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

    /**
     * 支出或收入的总和
     */
    var sumMoney: BigDecimal? = null
}
