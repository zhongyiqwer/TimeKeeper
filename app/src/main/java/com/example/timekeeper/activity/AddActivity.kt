package com.example.timekeeper.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.GridView
import android.widget.SimpleAdapter
import com.example.timekeeper.R
import com.example.timekeeper.base.BaseActivity


/**
 * Created by ZJX on 2018/5/7.
 */
class AddActivity : BaseActivity() {

    val dataList = ArrayList<Map<String, String>>()

    val flag = intArrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_activity_gridview_layout)
        val gridView2: GridView = findViewById<GridView>(R.id.gridView2)

        initData()

        val from = arrayOf("text")
        val to = intArrayOf(R.id.grid_tv)
        val noText = intArrayOf(0,1,2,3,4,8,12,16)
        val adapter = SimpleAdapter(this,dataList,R.layout.gridview_item_layout,from,to)
        gridView2.setAdapter(adapter)
        gridView2.setOnItemClickListener { parent, view, position, id ->
            if (!noText.contains(position)){
                if (flag[position] == 0){
                    flag[position] =1
                    println("dianjishijian"+position)
                    view.setBackgroundResource(R.color.lightgreen)
                    //view.setBackgroundColor(R.drawable.touch_add)
                    //这里只能用Color里面的颜色，有colors.xml里面的无效
                    //view.setBackgroundColor(Color.rgb(0x00,0xEE,0x00))
                }else if (flag[position] == 1){
                    flag[position] =0
                    view.setBackgroundResource(R.color.white)
                    //view.setBackgroundColor(R.drawable.touch_move)
                    //view.setBackgroundColor(Color.rgb(0xFF,0xFF,0xFF))

                }

            }
        }
    }

    private fun initData(){
        val name = arrayOf("\\", "今天", "明天", "后天",
                "9:00", "", "", "",
                "15:00", "", "", "",
                "17:00", "", "", "",
                "19:00", "", "", "")
        for (text1 in name){
            val data = HashMap<String,String>()
            data.put("text",text1)
            dataList.add(data)
        }
    }
}