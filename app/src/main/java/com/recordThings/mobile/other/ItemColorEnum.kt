package com.recordThings.mobile.other

import java.util.*

/**
 * 条目颜色
 */
object ItemColors {
    enum class ItemColorEnum(val itemBgColor: String, val textColor: String) {

        Color1("#03A9F4", "#FFFFFF"),
        Color2("#414459", "#73798c"),
        Color3("#4CAF50", "#FFFFFF"),
        Color4("#FF9800", "#FFFFFF")
    }

    private val items = ItemColorEnum.values()
    private val rand = Random()
    fun genItemColor(): ItemColorEnum {
        val n = rand.nextInt(3) + 1
        return items[n]
    }
}