package com.recordThings.mobile.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * @project : Buoluo
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/1/26
 */
object DateUtils {

    fun format(timeMillis:Long,pattern:String): String {
        return SimpleDateFormat(pattern,Locale.getDefault()).format(timeMillis)
    }


    private const val minutesLong = 60 * 1000L

    /**
     * 格式化日期:与当前日期作比较,判断应该显示的时间的格式
     * @param date 要格式化的日期
     * @param flag 用于判断是否要显示具体的日期还是模糊时间
     */
    fun dateTurnBefore(date: Date, flag: Boolean): String {
        val shortFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()).format(date)
        val dayFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(date)
        val start = Date()
        if (start.time < date.time) {
            return shortFormat.format(date)
        }
        val result: String
        val minutes: Long = timeIntervalMinutes(start, date)
        //分钟
        result = if (minutes < 60) {
            if (0L == minutes) "刚刚" else minutes.toString() + "分钟前"
        } else if (minutes / 60 < 24) {
            val hourTime = minutes / 60
            //小时
            if (0L == hourTime) "1小时前" else hourTime.toString() + "小时前"
        } else if (minutes / 60 / 24 < 365) {
            //天
            val dayLong = minutes / 60 / 24
            if (flag) {
                if (0L == dayLong) "1天前" else dayLong.toString() + "天前"
            } else {
                when {
                    0L == dayLong -> "1天前"
                    7 < dayLong -> dayLong.toString() + "天前"
                    else -> dayFormat.format(
                        date
                    )
                }
            }
        } else {
            if (flag) {
                val year = minutes / 60 / 24 / 365
                if (0L == year) "1年前" else year.toString() + "年前"
            } else {
                shortFormat.format(date)
            }
        }
        return result
    }

    /**
     *
     */
    private fun timeIntervalMinutes(end: Date, start: Date): Long {
        return (end.time - start.time) / minutesLong
    }
}