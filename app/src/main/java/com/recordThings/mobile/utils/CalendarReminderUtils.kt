package com.recordThings.mobile.utils

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.provider.CalendarContract
import android.text.TextUtils
import timber.log.Timber
import java.util.*


/**
 * 日历工具类
 */
object CalendarReminderUtils {
    private const val CALENDER_URL = "content://com.android.calendar/calendars"
    private const val CALENDER_EVENT_URL = "content://com.android.calendar/events"
    private const val CALENDER_REMINDER_URL = "content://com.android.calendar/reminders"
    private const val CALENDARS_NAME = "boohee"
    private const val CALENDARS_ACCOUNT_NAME = "BOOHEE@boohee.com"
    private const val CALENDARS_ACCOUNT_TYPE = "com.android.boohee"
    private const val CALENDARS_DISPLAY_NAME = "BOOHEE账户"

    /**
     * 检查是否已经添加了日历账户，如果没有添加先添加一个日历账户再查询
     * 获取账户成功返回账户id，否则返回-1
     */
    private fun checkAndAddCalendarAccount(context: Context): Int {
        val oldId = checkCalendarAccount(context)
        return if (oldId >= 0) {
            oldId
        } else {
            val addId = addCalendarAccount(context)
            if (addId >= 0) {
                checkCalendarAccount(context)
            } else {
                -1
            }
        }
    }

    /**
     * 检查是否存在现有账户，存在则返回账户id，否则返回-1
     */
    @SuppressLint("Range")
    private fun checkCalendarAccount(context: Context): Int {
        val userCursor =
            context.contentResolver.query(Uri.parse(CALENDER_URL), null, null, null, null)
        return try {
            if (userCursor == null) { //查询返回空值
                return -1
            }
            val count = userCursor.count
            if (count > 0) { //存在现有账户，取第一个账户的id返回
                userCursor.moveToFirst()
                userCursor.getInt(userCursor.getColumnIndex(CalendarContract.Calendars._ID))
            } else {
                -1
            }
        } finally {
            userCursor?.close()
        }
    }

    /**
     * 添加日历账户，账户创建成功则返回账户id，否则返回-1
     */
    private fun addCalendarAccount(context: Context): Long {
        val timeZone: TimeZone = TimeZone.getDefault()
        val value = ContentValues()
        value.put(CalendarContract.Calendars.NAME, CALENDARS_NAME)
        value.put(
            CalendarContract.Calendars.ACCOUNT_NAME,
            CALENDARS_ACCOUNT_NAME
        )
        value.put(
            CalendarContract.Calendars.ACCOUNT_TYPE,
            CALENDARS_ACCOUNT_TYPE
        )
        value.put(
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
            CALENDARS_DISPLAY_NAME
        )
        value.put(CalendarContract.Calendars.VISIBLE, 1)
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE)
        value.put(
            CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
            CalendarContract.Calendars.CAL_ACCESS_OWNER
        )
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1)
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID())
        value.put(
            CalendarContract.Calendars.OWNER_ACCOUNT,
            CALENDARS_ACCOUNT_NAME
        )
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0)
        var calendarUri = Uri.parse(CALENDER_URL)
        calendarUri = calendarUri.buildUpon()
            .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
            .appendQueryParameter(
                CalendarContract.Calendars.ACCOUNT_NAME,
                CALENDARS_ACCOUNT_NAME
            )
            .appendQueryParameter(
                CalendarContract.Calendars.ACCOUNT_TYPE,
                CALENDARS_ACCOUNT_TYPE
            )
            .build()
        val result = context.contentResolver.insert(calendarUri, value)
        return if (result == null) -1 else ContentUris.parseId(result)
    }

    /**
     * 添加日历事件
     */
    fun addCalendarEvent(
        context: Context?,
        title: String?,
        description: String?,
        reminderTime: Long,
        previousDate: Int
    ) {
        if (context == null) {
            return
        }
        val calId = checkAndAddCalendarAccount(context) //获取日历账户的id
        if (calId < 0) { //获取账户id失败直接返回，添加日历事件失败
            return
        }

        //添加日历事件
        val mCalendar: Calendar = Calendar.getInstance()
        mCalendar.setTimeInMillis(reminderTime) //设置开始时间
        val start: Long = mCalendar.getTime().getTime()
        mCalendar.setTimeInMillis(start + 10 * 60 * 1000) //设置终止时间，开始时间加10分钟
        val end: Long = mCalendar.getTime().getTime()
        val event = ContentValues()
        event.put("title", title)
        event.put("description", description)
        event.put("calendar_id", calId) //插入账户的id
        event.put(CalendarContract.Events.DTSTART, start)
        event.put(CalendarContract.Events.DTEND, end)
        event.put(CalendarContract.Events.HAS_ALARM, 1) //设置有闹钟提醒
        event.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai") //这个是时区，必须有
        //event.put("rrule", "FREQ=WEEKLY;COUNT=10;WKST=SU;BYDAY=FR");这个表示每周的星形五重复，从第一次重复开始重复10次。里面的参数自己可以修改
        event.put(CalendarContract.Events.RRULE, "FREQ=DAILY")
        val newEvent = context.contentResolver.insert(Uri.parse(CALENDER_EVENT_URL), event)
            ?: //添加日历事件失败直接返回
            return //添加事件

        //事件提醒的设定
        val values = ContentValues()
        values.put(CalendarContract.Reminders.EVENT_ID, ContentUris.parseId(newEvent))
        values.put(CalendarContract.Reminders.MINUTES, previousDate * 24 * 60) // 提前previousDate天有提醒
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
        val uri = context.contentResolver.insert(Uri.parse(CALENDER_REMINDER_URL), values)
        if (uri != null) {
            Timber.d("日程添加成功")
        } else {
            Timber.d("日程添加失败")
        }
    }

    /**
     * 删除日历事件
     */
    @SuppressLint("Range")
    fun deleteCalendarEvent(context: Context?, title: String) {
        if (context == null) {
            return
        }
        val eventCursor =
            context.contentResolver.query(Uri.parse(CALENDER_EVENT_URL), null, null, null, null)
        try {
            if (eventCursor == null) { //查询返回空值
                return
            }
            if (eventCursor.count > 0) {
                //遍历所有事件，找到title跟需要查询的title一样的项
                eventCursor.moveToFirst()
                while (!eventCursor.isAfterLast) {
                    val eventTitle = eventCursor.getString(eventCursor.getColumnIndex("title"))
                    if (!TextUtils.isEmpty(title) && title == eventTitle) {
                        val id =
                            eventCursor.getInt(eventCursor.getColumnIndex(CalendarContract.Calendars._ID)) //取得id
                        val deleteUri =
                            ContentUris.withAppendedId(Uri.parse(CALENDER_EVENT_URL), id.toLong())
                        val rows = context.contentResolver.delete(deleteUri, null, null)
                        if (rows == -1) { //事件删除失败
                            return
                        }
                    }
                    eventCursor.moveToNext()
                }
            }
        } finally {
            eventCursor?.close()
        }
    }
}