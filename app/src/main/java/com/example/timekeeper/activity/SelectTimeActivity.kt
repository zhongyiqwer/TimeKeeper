package com.example.timekeeper.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.widget.*
import com.alibaba.fastjson.JSON
import com.example.timekeeper.R
import com.example.timekeeper.base.BaseActivity
import com.example.timekeeper.dao.Action
import com.example.timekeeper.adapter.GridViewAdapter
import com.example.timekeeper.util.Common
import com.example.timekeeper.util.HttpHelper
import com.example.timekeeper.util.URL
import kotlinx.android.synthetic.main.select_time_activity.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.util.ArrayList


/**
 * Created by Administrator on 2018/5/15.
 */
class SelectTimeActivity :BaseActivity() ,View.OnClickListener,AdapterView.OnItemClickListener{

    internal var timeStyle :Int?=0

    lateinit internal var tv_name : TextView
    lateinit internal var tv_time : TextView
    lateinit internal var tv_introduce : TextView

    lateinit internal var confirm : Button
    lateinit internal var select_grid : GridView

    lateinit internal var adpater: GridViewAdapter

    lateinit var dataList: ArrayList<String>
    lateinit var timedata: ArrayList<String>

    internal var select_type : Int?=3
    internal var last_selectType : Int?=3

    internal lateinit var activityId : String

    lateinit var flag :Array<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getTimeStyle()
        setContentView(R.layout.select_time_activity)
        activityId = intent.getStringExtra("activityId")
        initView()
        initData()
    }

    override fun onClick(v: View?) {
        if(v == select_nocan){
            last_selectType = select_type
            select_type = 0
            select_nocan.background = resources.getDrawable(R.drawable.btn_select_red)
            changeBtnType()
        }
        if(v == select_1can){
            last_selectType = select_type
            select_type = 1
            select_1can.background = resources.getDrawable(R.drawable.btn_select_orange)
            changeBtnType()
        }
        if(v == select_2can){
            last_selectType = select_type
            select_type = 2
            select_2can.background = resources.getDrawable(R.drawable.btn_select_ligthgreen)
            changeBtnType()
        }
        if (v == select_can){
            last_selectType = select_type
            select_type = 3
            select_can.background = resources.getDrawable(R.drawable.btn_select_green)
            changeBtnType()
        }


        if(v == confirm ){
            println("confirm:")
            if (!gridTime()){
                Common.display(this,"不能为空")
            }else{
                val map = HashMap<String, String>()
                map.put("userId",Common.userId)
                map.put("activityId",this!!.activityId!!)
                map.put("time_campute",Common.getTimeString(flag.toIntArray()))
                HttpHelper.sendOkHttpRequest(URL.TAKE_Action_Post,map,object :Callback{
                    override fun onFailure(call: Call?, e: IOException?) {
                        runOnUiThread {
                            Common.display(this@SelectTimeActivity,"加入失败")
                        }
                    }
                    override fun onResponse(call: Call?, response: Response?) {
                        if ("success".equals(HttpHelper.getMessage(response!!.body()!!.string()))){
                            runOnUiThread {
                                Common.display(this@SelectTimeActivity,"加入成功")
                                val intent = Intent(this@SelectTimeActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                })
            }
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (adpater.getItem(position) != "0"){
            println(flag[position].toString()+" "+select_type.toString())
           /* if (flag[position] == 0){
                if (select_type == 0){
                    flag[position] =1
                    dataList[position] = "2"
                    //view!!.setBackgroundResource(R.color.lightgreen)
                }else if (select_type == 1){
                    flag[position] =2
                    dataList[position] = "5"
                    //view!!.setBackgroundResource(R.color.light_red)
                }
            }else if (flag[position] == 1){
                if (select_type == 0){
                    flag[position] =0
                    dataList[position] = "1"
                    //view!!.setBackgroundResource(R.color.white)
                }else if (select_type == 1){
                    flag[position] =2
                    dataList[position] = "5"
                    //view!!.setBackgroundResource(R.color.light_red)
                }
            }else if(flag[position] == 2){
                if (select_type == 0){
                    flag[position] =1
                    dataList[position] = "2"
                    //view!!.setBackgroundResource(R.color.lightgreen)
                }else if (select_type == 1){
                    flag[position] =0
                    dataList[position] = "1"
                    //view!!.setBackgroundResource(R.color.white)
                }
            }*/
            if (select_type == 3){
                flag[position] =3
                dataList[position] = "5"
                //view!!.setBackgroundResource(R.color.bar_deepgreen)
            }
            if (select_type == 2){
                flag[position] =2
                dataList[position] = "4"
                //view!!.setBackgroundResource(R.color.bar_lightgreen)
            }
            if (select_type == 1){
                flag[position] =1
                dataList[position] = "3"
                //view!!.setBackgroundResource(R.color.bar_orange)
            }
            if (select_type == 0){
                flag[position] =0
                dataList[position] = "2"
                //view!!.setBackgroundResource(R.color.bar_red)
            }
            adpater.notifyDataSetChanged()
        }
    }

    private fun initView() {
        tv_name = findViewById(R.id.tv_name)
        tv_time = findViewById(R.id.tv_time)
        tv_introduce = findViewById(R.id.tv_introduce)
        /*have_time = findViewById(R.id.have_time)
        no_time = findViewById(R.id.no_time)
        no_select = findViewById(R.id.no_select)*/
        confirm = findViewById(R.id.confirm)
        select_grid = findViewById(R.id.gridView2)

        select_1can.setOnClickListener(this)
        select_2can.setOnClickListener(this)
        select_nocan.setOnClickListener(this)
        select_can.setOnClickListener(this)

        confirm.setOnClickListener(this)

        select_grid.setOnItemClickListener(this)

        dataList = ArrayList<String>()
        timedata = ArrayList<String>()

        var indroduce = true
        tv_introduce.setOnClickListener {
            if (indroduce){
                indroduce = false
                tv_introduce.ellipsize = null
                tv_introduce.setSingleLine(indroduce)
            }else{
                indroduce =true
                tv_introduce.ellipsize = TextUtils.TruncateAt.END
                tv_introduce.setSingleLine(indroduce)
            }
        }

        var palce = true
        tv_place.setOnClickListener {
            if (palce){
                palce = false
                tv_place.ellipsize = null
                tv_place.setSingleLine(palce)
            }else{
                palce =true
                tv_place.ellipsize = TextUtils.TruncateAt.END
                tv_place.setSingleLine(palce)
            }
        }
    }

    private fun initData() {

        val map = HashMap<String, String>()
        println("selectTime的activityId = "+activityId)
        map.put("activityId",activityId!!)
        HttpHelper.sendOkHttpRequest(URL.GET_Select_Acion,map,object :Callback{
            override fun onFailure(call: Call?, e: IOException?) {
            }

            override fun onResponse(call: Call?, response: Response?) {
                val body = response!!.body()!!.string()
                if ("success".equals(HttpHelper.getMessage(body))){
                    //发序列化数据到Action
                    val activity = JSON.parseObject(body).getString("activity")
                    val action = JSON.parseObject(activity, Action::class.java)
                    println(action.toString())
                    runOnUiThread {
                        tv_name.text = action.activityName
                        tv_type.text = action.activityType
                        tv_num.text = action.activityNum
                        tv_place.text = action.activityPlace
                        tv_time.text = action.lastingTime+"小时"
                        tv_introduce.text = action.description

                        val activityContinueTime = action.lastingTime
                        val activitySelectDate = action.activitySelectDate

                        val range = Common.getRange(activityContinueTime,activitySelectDate)
                        flag = Array(range,{0})

                        val spiltString = Common.spiltString(action.start_time)
                        var data = Common.arrToArrarList(spiltString)
                        dataList = data
                        timedata = Common.initAdapterData(activityContinueTime,activitySelectDate)
                        adpater = GridViewAdapter(this@SelectTimeActivity,dataList,timedata)
                        select_grid.setAdapter(adpater)
                        //adpater.notifyDataSetChanged()
                    }
                }
            }
        })

    }

    private fun getTimeStyle() {
       timeStyle = 0
    }

    private fun gridTime() : Boolean{
        for (i in flag){
            if (i != 0){
                return true
            }
        }
        return false
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }

    fun changeBtnType(){
        if (last_selectType != select_type){
            when(last_selectType){
                0 -> { select_nocan.background = resources.getDrawable(R.drawable.btn_big_red) }
                1 ->{select_1can.background = resources.getDrawable(R.drawable.btn_orange)}
                2 ->{select_2can.background = resources.getDrawable(R.drawable.btn_ligthgreen)}
                3 ->{select_can.background = resources.getDrawable(R.drawable.btn_deepgreen)}
            }
        }
    }
}