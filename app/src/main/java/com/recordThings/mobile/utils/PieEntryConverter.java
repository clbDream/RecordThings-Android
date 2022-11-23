

package com.recordThings.mobile.utils;

import com.github.mikephil.charting.data.PieEntry;
import com.recordThings.mobile.db.entities.TypeSumMoneyBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 饼状图数据转换器
 *
 * @author Bakumon https://bakumon.me
 */
public class PieEntryConverter {
    /**
     * 获取饼状图所需数据格式 PieEntry
     *
     * @param typeSumMoneyBeans 类型汇总数据
     * @return List<PieEntry>
     */
    public static List<PieEntry> getBarEntryList(List<TypeSumMoneyBean> typeSumMoneyBeans) {
        List<PieEntry> entryList = new ArrayList<>();
        for (int i = 0; i < typeSumMoneyBeans.size(); i++) {
            BigDecimal typeMoney = typeSumMoneyBeans.get(i).getTypeSumMoney();
            entryList.add(new PieEntry(typeMoney.intValue(), typeSumMoneyBeans.get(i).getTypeName(), typeSumMoneyBeans.get(i)));
        }
        return entryList;
    }
}
