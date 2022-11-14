package com.recordThings.mobile.db.datasource

import android.content.res.Resources
import com.recordThings.mobile.app.AppApplication
import com.recordThings.mobile.db.entities.SportClass

object SportClassInitCreator {

    fun createSportClassData(): Array<SportClass> {
        val list: ArrayList<SportClass> = ArrayList()

        val res: Resources = AppApplication.getApp().resources

        // 跑步
        var type = SportClass(className = "跑步", classIcon = "sport_class_paobu")
        list.add(type)

        // 跳绳
        type = SportClass(className = "跳绳", classIcon = "sport_class_跳绳")
        list.add(type)

        return list.toArray(arrayOfNulls<SportClass>(list.size))
    }
}