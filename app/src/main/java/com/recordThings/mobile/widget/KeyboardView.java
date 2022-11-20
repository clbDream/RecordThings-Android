/*
 * Copyright 2018 Bakumon. https://github.com/Bakumon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.recordThings.mobile.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.recordThings.mobile.R;
import com.recordThings.mobile.app.AppApplication;
import com.recordThings.mobile.utils.SoftInputUtils;

/**
 * 自定义键盘
 *
 * @author Bakumon https://bakumon.me
 */
public class KeyboardView extends LinearLayout {
    private static final int MAX_INTEGER_NUMBER = 6;

    private OnAffirmClickListener mOnAffirmClickListener;
    private EditText editInput;
    private TextView keyboardAffirm;

    public KeyboardView(Context context) {
        this(context, null);
    }

    public KeyboardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public interface OnAffirmClickListener {
        /**
         * 确定按钮点击事件
         *
         * @param text 输入框的文字
         */
        void onAffirmClick(String text);
    }

    public void setAffirmEnable(boolean enable) {
            keyboardAffirm.setEnabled(enable);
    }

    public void setAffirmClickListener(OnAffirmClickListener listener) {
        mOnAffirmClickListener = listener;
    }

    public void setText(String text) {
        editInput.setText(text);
        editInput.setSelection(editInput.getText().length());
        SoftInputUtils.hideSoftInput(editInput);
        if (!editInput.isFocused()) {
            editInput.requestFocus();
        }
    }

    public void setEditTextFocus() {
        editInput.requestFocus();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(Context context) {
        try {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_keyboard, this, true);
            // 当前 activity 打开时不弹出软键盘
            Activity activity = (Activity) context;
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            setOrientation(VERTICAL);
            editInput = view.findViewById(R.id.edit_input);
            TextView keyboardNum0 = view.findViewById(R.id.keyboard_num_0);
            TextView keyboardNum1 = view.findViewById(R.id.keyboard_num_1);
            TextView keyboardNum2 = view.findViewById(R.id.keyboard_num_2);
            TextView keyboardNum3 = view.findViewById(R.id.keyboard_num_3);
            TextView keyboardNum4 = view.findViewById(R.id.keyboard_num_4);
            TextView keyboardNum5 = view.findViewById(R.id.keyboard_num_5);
            TextView keyboardNum6 = view.findViewById(R.id.keyboard_num_6);
            TextView keyboardNum7 = view.findViewById(R.id.keyboard_num_7);
            TextView keyboardNum8 = view.findViewById(R.id.keyboard_num_8);
            TextView keyboardNum9 = view.findViewById(R.id.keyboard_num_9);
            keyboardAffirm = view.findViewById(R.id.keyboard_affirm);
            TextView keyboardNumPoint = view.findViewById(R.id.keyboard_num_point);
            LinearLayout keyboardDelete = view.findViewById(R.id.keyboard_delete);

            editInput.requestFocus();
            editInput.setOnTouchListener((v, event) -> {
                SoftInputUtils.hideSoftInput(editInput);
                editInput.requestFocus();
                // 返回 true，拦截了默认的点击和长按操作，这是一个妥协的做法
                // 不再考虑多选粘贴的情况
                return true;
            });

            setInputTextViews(keyboardNum0, keyboardNum1,
                    keyboardNum2, keyboardNum3,
                    keyboardNum4, keyboardNum5,
                    keyboardNum6, keyboardNum7,
                    keyboardNum8, keyboardNum9,
                    keyboardNumPoint);
            setDeleteView(keyboardDelete);

            keyboardAffirm.setOnClickListener(v -> {
                if (mOnAffirmClickListener != null) {
                    String text = editInput.getText().toString();
                    boolean isDigital = !TextUtils.isEmpty(text)
                            && !TextUtils.equals("0", text)
                            && !TextUtils.equals("0.", text);
                    if (!isDigital) {
                        Animation animation = AnimationUtils.loadAnimation(AppApplication.Companion.getApp(), R.anim.shake);
                        editInput.startAnimation(animation);
                    } else {
                        mOnAffirmClickListener.onAffirmClick(text);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置键盘输入字符的textView，注意，textView点击后text将会输入到editText上
     */
    private void setInputTextViews(final TextView... textViews) {
        EditText target = editInput;
        if (target == null || textViews == null || textViews.length < 1) {
            return;
        }
        for (int i = 0; i < textViews.length; i++) {
            final int finalI = i;
            textViews[i].setOnClickListener(view -> {
                StringBuilder sb = new StringBuilder(target.getText().toString().trim());
                String result = inputFilter(sb, textViews[finalI].getText().toString());
                setText(result);
            });
        }
    }

    /**
     * 整数9位，小数2位
     */
    private String inputFilter(StringBuilder sb, String text) {
        if (sb.length() < 1) {
            // 输入第一个字符
            if (TextUtils.equals(text, ".")) {
                sb.insert(0, "0.");
            } else {
                sb.insert(0, text);
            }
        } else if (sb.length() == 1) {
            // 输入第二个字符
            if (sb.toString().startsWith("0")) {
                if (TextUtils.equals(".", text)) {
                    sb.insert(sb.length(), ".");
                } else {
                    sb.replace(0, 1, text);
                }
            } else {
                sb.insert(sb.length(), text);
            }
        } else if (sb.toString().contains(".")) {
            // 已经输入了小数点
            int length = sb.length();
            int index = sb.indexOf(".");
            if (!TextUtils.equals(".", text)) {
                if (length - index < 3) {
                    sb.insert(sb.length(), text);
                }
            }
        } else {
            // 整数
            if (TextUtils.equals(".", text)) {
                sb.insert(sb.length(), text);
            } else {
                if (sb.length() < MAX_INTEGER_NUMBER) {
                    sb.insert(sb.length(), text);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 设置删除键
     */
    public void setDeleteView(final View deleteView) {
        final EditText target = editInput;
        if (target == null) {
            return;
        }
        deleteView.setOnClickListener(view -> {
            StringBuilder sb = new StringBuilder(target.getText().toString().trim());
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
                setText(sb.toString());
            }
        });
        deleteView.setOnLongClickListener(v -> {
            StringBuilder sb = new StringBuilder(target.getText().toString().trim());
            if (sb.length() > 0) {
                setText("");
            }
            return false;
        });
    }
}
