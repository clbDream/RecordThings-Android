package com.recordThings.mobile.db.datasource

import android.content.res.Resources
import com.recordThings.mobile.app.AppApplication
import com.recordThings.mobile.db.entities.SportClass

object SportClassInitCreator {

    fun createSportClassData(): Array<SportClass> {
        val list: ArrayList<SportClass> = ArrayList()

        val res: Resources = AppApplication.getApp().resources

        var type = SportClass(className = "健走", classIcon = "sport_class_jianzou")
        list.add(type)

        type = SportClass(className = "跑步", classIcon = "sport_class_paobu")
        list.add(type)

        type = SportClass(className = "跳绳", classIcon = "sport_class_tiaosheng")
        list.add(type)

        type = SportClass(className = "骑行", classIcon = "sport_class_qixing")
        list.add(type)

        type = SportClass(className = "健身", classIcon = "sport_class_jianshen")
        list.add(type)

        type = SportClass(className = "俯卧撑", classIcon = "sport_class_fuwocheng")
        list.add(type)

        type = SportClass(className = "仰卧起坐", classIcon = "sport_class_yangwoqizuo")
        list.add(type)

        type = SportClass(className = "游泳", classIcon = "sport_class_youyong")
        list.add(type)

        type = SportClass(className = "举重", classIcon = "sport_class_juzhong")
        list.add(type)

        type = SportClass(className = "踢毽子", classIcon = "sport_class_tijianzi")
        list.add(type)

        return list.toArray(arrayOfNulls<SportClass>(list.size))
    }
}