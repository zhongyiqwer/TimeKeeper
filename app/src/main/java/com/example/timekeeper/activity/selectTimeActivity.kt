package com.example.timekeeper.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.timekeeper.R
import com.example.timekeeper.base.BaseActivity


/**
 * Created by Administrator on 2018/5/15.
 */
class selectTimeActivity :BaseActivity() ,View.OnClickListener,AdapterView.OnItemClickListener{

    internal var timeStyle :Int?=0

    lateinit internal var tv_name : TextView
    lateinit internal var tv_time : TextView
    lateinit internal var tv_introduce : TextView

    lateinit internal var have_time : ImageButton
    lateinit internal var no_time : ImageButton
    lateinit internal var no_select : ImageButton
    lateinit internal var confirm : Button
    lateinit internal var select_grid : GridView

    internal var select_type : Int?=0

    val dataList = ArrayList<Map<String, String>>()
    val flag = intArrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    val noText = intArrayOf(0,1,2,3,4,8,12,16)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getTimeStyle()

        setContentView(R.layout.select_time_activity)
        initView()

    }

    override fun onClick(v: View?) {
        if(v == have_time){
            select_type = 0
        }
        if(v == no_time){
            select_type = 1
        }
        if(v == no_select){
        }
        if(v == confirm){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (!noText.contains(position)){
            if (flag[position] == 0){
                if (select_type == 0){
                    flag[position] =1
                    view!!.setBackgroundResource(R.color.lightgreen)
                }else if (select_type == 1){
                    flag[position] =2
                    view!!.setBackgroundResource(R.color.light_red)
                }
            }else if (flag[position] == 1){
                if (select_type == 0){
                    flag[position] =0
                    view!!.setBackgroundResource(R.color.white)
                }else if (select_type == 1){
                    flag[position] =2
                    view!!.setBackgroundResource(R.color.light_red)
                }
            }else if(flag[position] == 2){
                if (select_type == 0){
                    flag[position] =1
                    view!!.setBackgroundResource(R.color.lightgreen)
                }else if (select_type == 1){
                    flag[position] =0
                    view!!.setBackgroundResource(R.color.white)
                }
            }
        }
    }

    private fun initView() {
        tv_name = findViewById(R.id.tv_name)
        tv_time = findViewById(R.id.tv_time)
        tv_introduce = findViewById(R.id.tv_introduce)
        have_time = findViewById(R.id.have_time)
        no_time = findViewById(R.id.no_time)
        no_select = findViewById(R.id.no_select)
        confirm = findViewById(R.id.confirm)
        select_grid = findViewById(R.id.gridView2)

        have_time.setOnClickListener(this)
        no_time.setOnClickListener(this)
        no_select.setOnClickListener(this)
        confirm.setOnClickListener(this)

        initData()
        select_grid.setOnItemClickListener(this)
    }

    private fun initData() {
        //后台获取数据
        tv_name.setText("打麻将")
        tv_time.setText("180分钟")
        tv_introduce.setText("打四川麻将")

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

    private fun getTimeStyle() {
       timeStyle = 0
    }
}