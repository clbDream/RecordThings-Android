

package com.recordThings.mobile.utils;

import com.github.mikephil.charting.data.BarEntry;
import com.recordThings.mobile.db.entities.DaySumMoneyBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 柱状图数据转换器
 *
 * @author Bakumon https://bakumon.me
 */
public class BarEntryConverter {
    /**
     * 获取柱状图所需数据格式 BarEntry
     *
     * @param count            生成的数据 list 大小
     * @param daySumMoneyBeans 包含日期和该日期汇总数据
     * @return List<BarEntry>
     */
    public static List<BarEntry> getBarEntryList(int count, List<DaySumMoneyBean> daySumMoneyBeans) {
        List<BarEntry> entryList = new ArrayList<>();
        if (daySumMoneyBeans != null && daySumMoneyBeans.size() > 0) {
            BarEntry barEntry;
            for (int i = 0; i < count; i++) {
                for (int j = 0; j < daySumMoneyBeans.size(); j++) {
                    if (i + 1 == daySumMoneyBeans.get(j).getTime().getDate()) {
                        BigDecimal money = BigDecimalUtil.fen2YuanBD(daySumMoneyBeans.get(j).getDaySumMoney());
                        // 这里的 y 由于是 float，所以数值很大的话，还是会出现科学计数法
                        barEntry = new BarEntry(i + 1, money.floatValue());
                        entryList.add(barEntry);
                    }
                }
                barEntry = new BarEntry(i + 1, 0);
                entryList.add(barEntry);
            }
        }
        return entryList;
    }
}
