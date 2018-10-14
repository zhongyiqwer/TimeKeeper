package com.example.timekeeper.widget;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

import com.example.timekeeper.R;
import com.example.timekeeper.util.DateTimeHelper;
/**
 * Created by ZY on 2018/10/14.
 */

/**
 * 日期时间选择控件 使用方法： private EditText inputDate;//需要设置的日期时间文本编辑框 private String
 * initDateTime="2012年9月3日 14:44",//初始日期时间值 在点击事件中使用：
 * inputDate.setOnClickListener(new OnClickListener() {
 *
 * @author
 * @Override public void onClick(View v) { DateTimePickDialogUtil
 * dateTimePicKDialog=new
 * DateTimePickDialogUtil(SinvestigateActivity.this,initDateTime);
 * dateTimePicKDialog.dateTimePicKDialog(inputDate);
 * <p/>
 * } });
 */
public class TimePickDialogUtil implements OnDateChangedListener{
    private DatePicker datePicker;
    private DatePicker datePickerEnd;
    private AlertDialog ad;
    private String dateTime;
    private String initDateTime;
    private Activity activity;
    private DatePickList datePickList;

    private String startDate = DateTimeHelper.getCurrentDateTime2();
    private String endDate = DateTimeHelper.getCurrentDateTime2();

    /**
     * 日期时间弹出选择框构造函数
     *
     * @param activity     ：调用的父activity
     * @param initDateTime 初始日期时间值，作为弹出窗口的标题和日期时间初始值
     */
    public TimePickDialogUtil(Activity activity, String initDateTime) {
        this.activity = activity;
        this.initDateTime = initDateTime;

    }

    public void init(DatePicker datePicker) {
        Calendar calendar = Calendar.getInstance();
        if (!(null == initDateTime || "".equals(initDateTime))) {
            calendar = this.getCalendarByInintData(initDateTime);
        } else {
            initDateTime = calendar.get(Calendar.YEAR) + "年"
                    + calendar.get(Calendar.MONTH) + "月"
                    + calendar.get(Calendar.DAY_OF_MONTH) + "日 ";
        }


        datePicker.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), this);

        datePickerEnd.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), this);
    }

    /**
     * 弹出日期时间选择框方法
     *
     * @return
     */
    public AlertDialog dateTimePicKDialog() {
        LinearLayout dateTimeLayout = (LinearLayout) activity
                .getLayoutInflater().inflate(R.layout.dialog_date_pick, null);
        datePicker = dateTimeLayout.findViewById(R.id.datepickerSta);
        datePickerEnd = dateTimeLayout.findViewById(R.id.datepickerEnd);
        init(datePicker);

        ad = new AlertDialog.Builder(activity)
                .setView(dateTimeLayout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.e("onclick",""+DateTimeHelper.daysBetween(startDate,endDate));
                        Log.e("onclick",""+DateTimeHelper.daysBetween(DateTimeHelper.getCurrentDateTime2(),startDate));
                        if (DateTimeHelper.daysBetween(startDate,endDate)>=1 &&
                                DateTimeHelper.daysBetween(DateTimeHelper.getCurrentDateTime2(),startDate)>=1) {
                            datePickList.onPost(true,startDate,endDate);
                        }else {
                            Toast.makeText(activity, "时间选择不合适", Toast.LENGTH_SHORT).show();
                            datePickList.onPost(false,startDate,endDate);
                        }
                    }
                })
                .setNegativeButton(
                        "取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                datePickList.onPost(false,startDate,endDate);
                            }
                        }

                )
                .show();

        //onDateChanged(null, 0, 0, 0);
        return ad;
    }

    public void setDatePickList(DatePickList datePickList){
        this.datePickList = datePickList;
    }

    public void onDateChanged(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
        // 获得日历实例
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dateTime = sdf.format(calendar.getTime());
        if (view == datePicker){
            Log.e("DateChangedSta",dateTime);
            startDate = dateTime;
        }else if (view == datePickerEnd){
            Log.e("DateChangedEnd",dateTime);
            endDate = dateTime;
        }
    }

    /**
     * 实现将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒,并赋值给calendar
     *
     * @param initDateTime 初始日期时间值 字符串型
     * @return Calendar
     */
    private Calendar getCalendarByInintData(String initDateTime) {
        Calendar calendar = Calendar.getInstance();

        // 将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒
        String date = spliteString(initDateTime, "日", "index", "front"); // 日期

        String yearStr = spliteString(date, "年", "index", "front"); // 年份
        String monthAndDay = spliteString(date, "年", "index", "back"); // 月日

        String monthStr = spliteString(monthAndDay, "月", "index", "front"); // 月
        String dayStr = spliteString(monthAndDay, "月", "index", "back"); // 日

        int currentYear = Integer.valueOf(yearStr.trim()).intValue();
        int currentMonth = Integer.valueOf(monthStr.trim()).intValue() - 1;
        int currentDay = Integer.valueOf(dayStr.trim()).intValue();

        calendar.set(currentYear, currentMonth, currentDay);
        return calendar;
    }

    /**
     * 截取子串
     *
     * @param srcStr      源串
     * @param pattern     匹配模式
     * @param indexOrLast
     * @param frontOrBack
     * @return
     */
    public static String spliteString(String srcStr, String pattern,
                                      String indexOrLast, String frontOrBack) {
        String result = "";
        int loc = -1;
        if (indexOrLast.equalsIgnoreCase("index")) {
            loc = srcStr.indexOf(pattern); // 取得字符串第一次出现的位置
        } else {
            loc = srcStr.lastIndexOf(pattern); // 最后一个匹配串的位置
        }
        if (frontOrBack.equalsIgnoreCase("front")) {
            if (loc != -1)
                result = srcStr.substring(0, loc); // 截取子串
        } else {
            if (loc != -1)
                result = srcStr.substring(loc + 1, srcStr.length()); // 截取子串
        }
        return result;
    }

   public interface DatePickList{

        void onPost(boolean isPost,String start,String end);
    }

}