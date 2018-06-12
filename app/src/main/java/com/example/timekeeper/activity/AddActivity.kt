package com.example.timekeeper.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.GridView
import android.widget.SimpleAdapter
import com.example.timekeeper.R
import com.example.timekeeper.R.id.bt_fabu
import com.example.timekeeper.base.BaseActivity
import com.example.timekeeper.util.Common
import com.example.timekeeper.util.HttpHelper
import com.example.timekeeper.util.URL
import kotlinx.android.synthetic.main.add_layout.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException


/**
 * Created by ZJX on 2018/5/7.
 */
class AddActivity : BaseActivity() ,View.OnClickListener{

    val dataList = ArrayList<Map<String, String>>()

    val flag = intArrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    val noText = intArrayOf(0,1,2,3,4,8,12,16)

    internal var select_type : Int?=0

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //防止键盘弹出改变布局
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        setContentView(R.layout.add_layout)
        val gridView2: GridView = findViewById<GridView>(R.id.gridView2)
        bt_fabu.setOnClickListener(this)
        IB_can.setOnClickListener(this)
        IB_nocan.setOnClickListener(this)
        initData()

        val from = arrayOf("text")
        val to = intArrayOf(R.id.grid_tv)

        val adapter = SimpleAdapter(this,dataList,R.layout.gridview_item_layout,from,to)
        gridView2.setAdapter(adapter)
        gridView2.setOnItemClickListener { parent, view, position, id ->

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

               /* if (flag[position] == 0){
                    flag[position] =1
                    println("dianjishijian"+position)
                    view.setBackgroundResource(R.color.lightgreen)

                }else if (flag[position] == 1){
                    flag[position] =0
                    view.setBackgroundResource(R.color.white)

                }*/

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
        if (v == IB_can){
            select_type = 0
            IB_can.background = resources.getDrawable(R.drawable.btn_select_green)
            IB_nocan.background = resources.getDrawable(R.drawable.btn_big_red)
        }

        if (v == IB_nocan){
            select_type = 1
            IB_can.background = resources.getDrawable(R.drawable.btn_big_deepgreen)
            IB_nocan.background = resources.getDrawable(R.drawable.btn_select_red)
        }


        if (v == bt_fabu){
            if (editText_name.getText().toString() == "" || editText_time.getText().toString() == "" ||
                    editText_des.getText().toString() == "" || !gridTime()){
                Common.display(this,"不能为空")
            }else{

                val map = HashMap<String, String>()
                map.put("creater",Common.userId)
                map.put("activityName",editText_name.getText().toString())
                map.put("lastingTime",editText_time.getText().toString())
                map.put("description",editText_des.getText().toString())
                map.put("time", Common.getTimeString(flag))

                HttpHelper.sendOkHttpRequest(URL.ADD_Action,map,object :Callback{
                    override fun onFailure(call: Call?, e: IOException?) {
                        runOnUiThread {
                            Common.display(this@AddActivity,"添加失败")
                        }
                    }

                    override fun onResponse(call: Call?, response: Response?) {
                        val reponseString = response!!.body()!!.string()
                        Log.e(Common.TAG,"add:"+ reponseString)
                        if ("success".equals(HttpHelper.getMessage(reponseString))){
                            runOnUiThread {
                                Common.display(this@AddActivity,"添加成功")
                                val intent = Intent(this@AddActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                })
            }
        }
    }

    private fun gridTime() : Boolean{
        for (i in flag){
            if (i != 0){
                return true
            }
        }
        return false
    }
}