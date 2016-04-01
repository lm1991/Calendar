package com.mesor.application;

import android.app.Application;

import com.mesor.utils.Lunar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2016/3/29.
 */
public class MyApplication extends Application {

    private List<String> dateList;

    private static MyApplication myApplication;

    public static MyApplication getInstance() {
        if (myApplication == null) {
            myApplication = new MyApplication();
        }
        return myApplication;
    }

    /**
     * 通过日期参数获取月份日期信息
     *
     * @param calendar : 查询日期
     * @return
     */
    public List<String> getDateList(Calendar calendar) {
        if (dateList == null || dateList.size() == 0) {
            dateList = new ArrayList<>();
            Calendar current = Calendar.getInstance();
            current.setTimeInMillis(calendar.getTimeInMillis());
            current.set(Calendar.DAY_OF_MONTH, 1);
            int day = current.get(Calendar.DAY_OF_WEEK);
            current.add(Calendar.DAY_OF_WEEK, 1 - day);
            Lunar lunar = new Lunar(current.getTimeInMillis());
            while (true) {
                for (int i = 0; i < 7; i++) {
                    lunar.setTime(current.getTimeInMillis());
                    String date = String.valueOf(current.get(Calendar.DAY_OF_MONTH));
                    if (!"".equals(lunar.getLFestivalName()) && lunar.getLFestivalName().length() <= 3)
                        date += "\n" + lunar.getLFestivalName();
                    else if (!"".equals(lunar.getSFestivalName()) && lunar.getSFestivalName().length() <= 3)
                        date += "\n" + lunar.getSFestivalName();
                    else date += "\n" + lunar.getLunarDayString();
                    dateList.add(date);
                    current.add(Calendar.DAY_OF_MONTH, 1);
                }

                if (current.get(Calendar.MONTH) != calendar.get(Calendar.MONTH))
                    break;
            }
        }
        return dateList;
    }

}
