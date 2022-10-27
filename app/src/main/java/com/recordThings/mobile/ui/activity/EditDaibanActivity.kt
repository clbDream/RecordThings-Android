package com.recordThings.mobile.ui.activity

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.hjq.base.BaseDialog
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.hjq.shape.view.ShapeEditText
import com.hjq.widget.layout.SettingBar
import com.recordThings.mobile.R
import com.recordThings.mobile.aop.Permissions
import com.recordThings.mobile.app.AppActivity
import com.recordThings.mobile.db.DbHelper
import com.recordThings.mobile.db.entities.Daiban
import com.recordThings.mobile.ui.dialog.TimeDialog
import com.recordThings.mobile.utils.CalendarReminderUtils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class EditDaibanActivity : AppActivity() {

    private val chooseTime: SettingBar? by lazy { findViewById(R.id.choose_time) }
    private val inputContent: ShapeEditText? by lazy { findViewById(R.id.input_content) }

    private var daiban: Daiban = Daiban()

    override fun getLayoutId(): Int {
        return R.layout.activity_edit_daiban
    }

    override fun initView() {
        requestPermission()
        setOnClickListener(chooseTime)
        val bundle = getBundle()
        daiban = bundle?.getParcelable("data") ?: Daiban()
        daiban.content.let {
            inputContent?.setText(it)
            it?.length?.let { it1 -> inputContent?.setSelection(it1) }
        }
        if (daiban.reminder_time > 0) {
            chooseTime?.setRightText("${SimpleDateFormat("kk时mm分ss秒").format(daiban.reminder_time)}")
        }
        inputContent?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                daiban.content = p0?.toString()
            }

        })
    }

    override fun initData() {

    }

    override fun onRightClick(view: View) {
        if (daiban.content?.isBlank() == true) {
            toast("您还没有输入内容哦!")
            return
        }
        if (daiban.reminder_time > 0) {
            //判断是否同意日历权限
            val granted =
                XXPermissions.isGranted(this, Permission.WRITE_CALENDAR, Permission.READ_CALENDAR)
            if (granted.not()) {
                requestPermission()
                return
            }
        }
        thread {
            val l = if (daiban.id != null) {
                //修改待办
                daiban.update_time = System.currentTimeMillis()
                DbHelper.db.daibanDao().upDateDaiban(daiban).toLong()
            } else {
                DbHelper.db.daibanDao().addDaiban(daiban)
            }
            if (daiban.reminder_time > 0) {
                CalendarReminderUtils.addCalendarEvent(
                    this,
                    daiban.content,
                    "待办提醒",
                    daiban.reminder_time,
                    0
                )
            }
            if (l > 0) {
                toast("保存成功")
                finish()
            } else {
                toast("保存失败,请稍后重试")
            }
        }
    }

    override fun onClick(view: View) {
        super.onClick(view)
        when (view) {
            chooseTime -> {
                chooseTimeDialog()
            }
            else -> {}
        }
    }

    private fun chooseTimeDialog() {
        // 时间选择对话框
        TimeDialog.Builder(this)
            .setTitle(getString(R.string.time_title))
            // 确定按钮文本
            .setConfirm(getString(R.string.common_confirm))
            // 设置 null 表示不显示取消按钮
            .setCancel(getString(R.string.common_cancel))
            // 设置时间
            //.setTime("23:59:59")
            //.setTime("235959")
            // 设置小时
            //.setHour(23)
            // 设置分钟
            //.setMinute(59)
            // 设置秒数
            //.setSecond(59)
            // 不选择秒数
            //.setIgnoreSecond()
            .setListener(object : TimeDialog.OnListener {

                override fun onSelected(dialog: BaseDialog?, hour: Int, minute: Int, second: Int) {
                    chooseTime?.setRightText(
                        hour.toString() + getString(R.string.common_hour) + minute + getString(
                            R.string.common_minute
                        ) + second + getString(R.string.common_second)
                    )

                    // 如果不指定年月日则默认为今天的日期
                    val calendar: Calendar = Calendar.getInstance()
                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    calendar.set(Calendar.MINUTE, minute)
                    calendar.set(Calendar.SECOND, second)
                    //更改提醒时间
                    daiban.reminder_time = calendar.timeInMillis
//                    toast("时间戳：" + calendar.timeInMillis)
                    //toast(new SimpleDateFormat("yyyy年MM月dd日 kk:mm:ss").format(calendar.getTime()));
                }

                override fun onCancel(dialog: BaseDialog?) {
                    toast("取消了")
                }
            })
            .show()
    }

    @Permissions(Permission.WRITE_CALENDAR, Permission.READ_CALENDAR)
    private fun requestPermission() {

    }
}