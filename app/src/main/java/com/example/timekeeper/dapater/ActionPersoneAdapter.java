package com.example.timekeeper.dapater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.timekeeper.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/19.
 */

public class ActionPersoneAdapter extends BaseAdapter {

    private Context context;
    private List<HashMap<String,String>> mapList;

    private ArrayList<String> spinneData;

    private String defualtLevel ="中";

    public ActionPersoneAdapter(Context context, ArrayList<HashMap<String,String>> mapList) {
        this.context = context;
        this.mapList = mapList;
        initdata();
    }
    private class ViewHolder {
         ImageView imageView;
         TextView name;
         TextView level;
         Spinner spinner;
    }

    @Override
    public int getCount() {
        return mapList.size();
    }

    @Override
    public Object getItem(int position) {
        return mapList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        HashMap<String,String> map = mapList.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.take_member_item_layout, parent, false); //加载布局
            holder = new ViewHolder();
            holder.imageView =  convertView.findViewById(R.id.imageView2);
            holder.name = convertView.findViewById(R.id.textView5);
            //holder.level = convertView.findViewById(R.id.textView7);
            holder.spinner = convertView.findViewById(R.id.spinner);

            ArrayAdapter<String> adapter1 = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,spinneData);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.spinner.setAdapter(adapter1);
            holder.spinner.setTag(position);
            holder.spinner.setOnItemSelectedListener(new ItemClickSelectListener(holder));

            convertView.setTag(holder);
        } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String imageUrl = map.get("image");
            //可以上glid
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_home_white_24dp));
            holder.name.setText(map.get("name"));
            //holder.level.setText(map.get("level"));
            holder.spinner.setSelection(getSpinnerLevel(map.get("level")));
        return convertView;
    }

    private void initdata() {
        spinneData = new ArrayList<>();
        spinneData.add("低");
        spinneData.add("中");
        spinneData.add("高");
    }

    private int getSpinnerLevel(String level){
        return spinneData.indexOf(level);
    }

    private class ItemClickSelectListener implements AdapterView.OnItemSelectedListener{
        ViewHolder holder;

        public ItemClickSelectListener(ViewHolder holder) {
            this.holder = holder;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            holder.spinner.setSelection(position);
            //holder.level.setText(spinneData.get(position));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
