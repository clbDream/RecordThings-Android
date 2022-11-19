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

import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.hjq.base.BaseAdapter;
import com.recordThings.mobile.R;
import com.recordThings.mobile.db.entities.RecordType;
import com.recordThings.mobile.db.entities.RecordWithType;
import com.recordThings.mobile.ui.adapter.TypeAdapter;
import com.recordThings.mobile.widget.pagerlayoutmanager.PagerGridLayoutManager;
import com.recordThings.mobile.widget.pagerlayoutmanager.PagerGridSnapHelper;

import java.util.List;
import java.util.Objects;

import me.relex.circleindicator.CircleIndicator;
import me.relex.circleindicator.CircleIndicator2;

/**
 * 翻页的 recyclerView + 指示器
 *
 * @author Bakumon https://bakumon.me
 */
public class TypePageView extends LinearLayout {

    private static final int ROW = 2;
    private static final int COLUMN = 4;

    private TypeAdapter mAdapter;
    private GridLayoutManager mLayoutManager;
    private int mCurrentTypeIndex = -1;

    public TypePageView(Context context) {
        this(context, null);
    }

    public TypePageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TypePageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        try {
            init(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_type_page, this, true);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
//        CircleIndicator2 indicator = view.findViewById(R.id.indicator);

        // 1.水平分页布局管理器
        mLayoutManager = new GridLayoutManager(getContext(),4);
        recyclerView.setLayoutManager(mLayoutManager);

        // 2.设置滚动辅助工具
//        PagerSnapHelper pageSnapHelper = null;
//        try {
//            pageSnapHelper = new PagerSnapHelper();
//            pageSnapHelper.attachToRecyclerView(recyclerView);
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        }

        mAdapter = new TypeAdapter(context);
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(@Nullable RecyclerView recyclerView, @Nullable View itemView, int position) {
                mAdapter.clickItem(position);
                mCurrentTypeIndex = position;
            }
        });
        recyclerView.setAdapter(mAdapter);

        //指示器
//        indicator.attachToRecyclerView(recyclerView,pageSnapHelper);
//        mAdapter.registerAdapterDataObserver(indicator.getAdapterDataObserver());

//        mLayoutManager.setPageListener(new PagerGridLayoutManager.PageListener() {
//            int currentPageIndex;
//            int pageSize;
//
//            @Override
//            public void onPageSizeChanged(int pageSize) {
//                this.pageSize = pageSize;
//                setIndicator();
//            }
//
//            @Override
//            public void onPageSelect(int pageIndex) {
//                currentPageIndex = pageIndex;
//                setIndicator();
//            }
//
//            private void setIndicator() {
//                if (pageSize > 1) {
//                    indicator.setVisibility(View.VISIBLE);
//                } else {
//                    indicator.setVisibility(View.INVISIBLE);
//                }
//            }
//        });
    }

    public void setNewData(@Nullable List<RecordType> data) {
        mAdapter.setData(data);
    }

    /**
     * 该方法只改变一次
     */
    public void initCheckItem(RecordWithType record) {
        if (mCurrentTypeIndex == -1) {
            mCurrentTypeIndex = 0;
            int isTypeExist = 0;
            int size = mAdapter.getData().size();
            if (record != null && size > 0) {
                for (int i = 0; i < size; i++) {
                    if (Objects.requireNonNull(record.getMRecordTypes()).get(0).getId() == mAdapter.getData().get(i).getId()) {
                        mCurrentTypeIndex = i;
                        isTypeExist++;
                        break;
                    }
                }
                if (isTypeExist != 0) {
                    // 选中对应的页
                    int pageIndex = mCurrentTypeIndex / (ROW * COLUMN);
//                    post(() -> mLayoutManager.smoothScrollToPage(pageIndex));
                } else {
                    showTypeNotExistTip();
                }
            }
            mAdapter.clickItem(mCurrentTypeIndex);
        }
    }

    /**
     * 提示用户该记录的类型已经被删除
     */
    private void showTypeNotExistTip() {
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.text_tip_type_delete)
                .setPositiveButton(R.string.text_button_know, null)
                .create()
                .show();
    }

    public RecordType getCurrentItem() {
        return mAdapter.getCurrentItem();
    }

}
