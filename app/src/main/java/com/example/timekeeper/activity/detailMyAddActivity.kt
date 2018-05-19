package com.example.timekeeper.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.GridView
import android.widget.SimpleAdapter
import com.example.timekeeper.R
import com.example.timekeeper.base.BaseActivity

/**
 * Created by Administrator on 2018/5/15.
 */
class detailMyAddActivity :BaseActivity(),View.OnClickListener{

    lateinit internal var select_grid : GridView
    lateinit internal var button : Button
    internal var timeStyle :Int?=0

    val dataList = ArrayList<Map<String, String>>()
    val flag = intArrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    val noText = intArrayOf(0,1,2,3,4,8,12,16)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getTimeStyle()

        setContentView(R.layout.detail_action_layout)
        initView()

    }

    private fun initView() {
        select_grid = findViewById(R.id.gridView2)
        button = findViewById(R.id.button)
        button.setOnClickListener(this)
        initData()
        //select_grid.setOnItemClickListener(this)
    }

    private fun initData() {
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

        val from = arrayOf("text")
        val to = intArrayOf(R.id.grid_tv)
        val adapter = SimpleAdapter(this,dataList,R.layout.gridview_item_layout,from,to)
        select_grid.setAdapter(adapter)
    }

    override fun onClick(v: View?) {
        if (v == button){
            val intent = Intent(this, actionPersoneActivity::class.java)
            startActivity(intent)
            //finish()
        }
    }

    private fun getTimeStyle() {
        timeStyle = 0
    }
}