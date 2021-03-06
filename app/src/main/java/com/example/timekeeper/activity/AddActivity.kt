package com.example.timekeeper.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.GridView
import android.widget.SimpleAdapter
import com.example.timekeeper.R
import com.example.timekeeper.R.id.bt_fabu
import com.example.timekeeper.base.BaseActivity
import kotlinx.android.synthetic.main.add_layout.*


/**
 * Created by ZJX on 2018/5/7.
 */
class AddActivity : BaseActivity() ,View.OnClickListener{

    val dataList = ArrayList<Map<String, String>>()

    val flag = intArrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //防止键盘弹出改变布局
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        setContentView(R.layout.add_layout)
        val gridView2: GridView = findViewById<GridView>(R.id.gridView2)
        bt_fabu.setOnClickListener(this)
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
                }else if (flag[position] == 1){
                    flag[position] =0
                    view.setBackgroundResource(R.color.white)
                    //view.setBackgroundColor(R.drawable.touch_move)
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

    override fun onClick(v: View?) {
       //点击事件
        if (v == bt_fabu){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}