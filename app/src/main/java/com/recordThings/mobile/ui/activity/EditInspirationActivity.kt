package com.recordThings.mobile.ui.activity

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.hjq.shape.view.ShapeEditText
import com.recordThings.mobile.R
import com.recordThings.mobile.app.AppActivity
import com.recordThings.mobile.app.AppApplication
import com.recordThings.mobile.db.entities.Inspiration
import kotlin.concurrent.thread

class EditInspirationActivity : AppActivity() {

    private val inputContent: ShapeEditText? by lazy { findViewById(R.id.input_content) }

    private var inspiration: Inspiration = Inspiration()

    override fun getLayoutId(): Int {
        return R.layout.activity_edit_inspiration
    }

    override fun initView() {
        inputContent?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                inspiration.content = p0?.toString()
            }

        })
    }

    override fun initData() {

    }

    override fun onRightClick(view: View) {
        if (inspiration.content?.isBlank() == true) {
            toast("您还没有输入内容哦!")
            return
        }
        thread {
            val l = AppApplication.db.inspirationDao().addInspiration(inspiration)
            if (l > 0) {
                toast("保存成功")
                finish()
            } else {
                toast("保存失败,请稍后重试")
            }
        }
    }
}