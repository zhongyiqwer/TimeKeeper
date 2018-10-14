package com.example.timekeeper.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.timekeeper.R;
import com.example.timekeeper.util.Common;

import java.util.ArrayList;

/**
 * Created by ZY on 2018/5/25.
 */

public class GridViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> dataList;
    private ArrayList<String> timeData;

    public GridViewAdapter(Context context, ArrayList<String> dataList,ArrayList<String> timeData) {
        this.context = context;
        this.dataList = dataList;
        this.timeData = timeData;
        Log.e(Common.TAG,"dataList:"+dataList.toString());
    }

    private class ViewHolder{
        TextView textView;
    }

    public void setTimeData(ArrayList<String> timeData){
        this.timeData.clear();
        this.timeData = timeData;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        String data = dataList.get(position);
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.gridview_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = convertView.findViewById(R.id.grid_tv);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //Log.e("GridView:",""+position+" "+timeData.size());
        //if (timeData!=null && !timeData.isEmpty()){
            viewHolder.textView.setText(timeData.get(position));
            viewHolder.textView.setTextColor(context.getResources().getColor(R.color.lightGray));
        //}
        switch (data){
            case "0": viewHolder.textView.setTextColor(Color.WHITE);
                viewHolder.textView.setBackgroundResource(
                    R.color.lighterGray);
                break;
            case "1": viewHolder.textView.setBackgroundResource(
                    R.color.white);
                    break;
            case "5": viewHolder.textView.setBackgroundResource(
                    R.color.bar_deepgreen);
                break;
            case "4": viewHolder.textView.setBackgroundResource(
                    R.color.bar_lightgreen);
                break;
            case "3": viewHolder.textView.setBackgroundResource(
                    R.color.bar_orange);
                break;
            case "2": viewHolder.textView.setBackgroundResource(
                    R.color.bar_red);
                break;
            default:
        }
        return convertView;
    }
}
