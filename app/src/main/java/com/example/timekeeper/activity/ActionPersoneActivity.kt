package com.example.timekeeper.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.widget.*
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.example.timekeeper.R
import com.example.timekeeper.base.BaseActivity
import com.example.timekeeper.adapter.ActionPersoneAdapter
import com.example.timekeeper.listener.LevelChange
import com.example.timekeeper.util.Common
import com.example.timekeeper.util.HttpHelper
import com.example.timekeeper.util.URL
import kotlinx.android.synthetic.main.take_member_layout.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * Created by Administrator on 2018/5/18.
 */
class ActionPersoneActivity : BaseActivity() ,AdapterView.OnItemClickListener,LevelChange{

    lateinit internal var listView: ListView
    lateinit internal var textView3: TextView
    lateinit internal var name: TextView
    lateinit internal var image: ImageView

    lateinit internal var adpater: ActionPersoneAdapter
    lateinit internal var findAdpater: ActionPersoneAdapter

    internal var actionId :String?="0"
    internal var isMy :Boolean?=false

    lateinit internal var dataList: ArrayList<HashMap<String, String>>

     lateinit var userLevel: HashMap<String, String>

     lateinit var userIdList: JSONArray


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionId = intent.getStringExtra("actionId")
        isMy = intent.getBooleanExtra("isMy",false)
        setContentView(R.layout.take_member_layout)
        initview()
        setList()
        initDate()
    }

    private fun initDate() {
      /*  //假数据
        for (i in 1 .. 20){
            val hashMap = HashMap<String, String>()
            hashMap.put("image","www.baidu.com"+i)
            hashMap.put("name","zhangsan"+i)
            hashMap.put("level","中")
            dataList.add(hashMap)
        }*/
        val map = HashMap<String, String>()
        map.put("activityId",this!!.actionId!!)
        HttpHelper.sendOkHttpRequest(URL.GET_ONE_Action_Person,map,object :Callback{
            override fun onFailure(call: Call?, e: IOException?) {
            }

            override fun onResponse(call: Call?, response: Response?) {
                val body = response!!.body()!!.string()
                println(body)
                if ("success".equals(HttpHelper.getMessage(body))){
                    val jsonObject = JSON.parseObject(body)
                    val array = jsonObject.getString("array")

                    val parseArray = JSON.parseArray(array)
                    val size = parseArray.size-1
                    for (i in 0..size){
                        val parseObject = parseArray.getJSONObject(i)
                        val hashMap = HashMap<String, String>()
                        hashMap.put("userId",parseObject.getString("userId"))
                        hashMap.put("userName",parseObject.getString("userName"))
                        hashMap.put("userLevel",parseObject.getString("userLevel"))
                        dataList.add(hashMap)
                    }
                    runOnUiThread {
                        adpater.notifyDataSetChanged()
                    }
                }
            }

        })
    }

    private fun setList() {
        dataList = ArrayList<HashMap<String, String>>()
        adpater = ActionPersoneAdapter(this, dataList,isMy!!)
        listView.setAdapter(adpater)
        listView.setOnItemClickListener(this)

        adpater.setLevelChangeListener(this)

        userLevel = LinkedHashMap<String, String>()
        userLevel.put("activityId",actionId!!)

        userIdList = JSONArray()
    }


    private fun initview() {
        listView = findViewById(R.id.listView)
        //textView3 = findViewById(R.id.textView3)
        var findList = ArrayList<HashMap<String, String>>()
        findAdpater = ActionPersoneAdapter(this,findList,isMy!!)
        findAdpater.setLevelChangeListener(this)
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (TextUtils.isEmpty(query)){
                    Toast.makeText(this@ActionPersoneActivity, "请输入查找内容！", Toast.LENGTH_SHORT).show()
                    listView.adapter = adpater
                    adpater.notifyDataSetChanged()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (TextUtils.isEmpty(newText)){
                    listView.adapter = adpater
                    adpater.notifyDataSetChanged()
                }else{
                    findList.clear()
                    for (i in dataList.indices){
                        val name = dataList[i].get("userName")
                        if (Common.contains(name,newText)){
                            findList.add(dataList[i])
                        }
                    }
                    listView.adapter = findAdpater
                    findAdpater.notifyDataSetChanged()
                }
                return true
            }
        })
    }

    //listview的点击事件
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            println("进入back")
            if (userLevel.size>1 && userIdList.size>0){
                //val jsonString = JSON.toJSONString(userIdList)
                val jsonString = JSONArray.toJSONString(userIdList)
                userLevel.put("userIds",jsonString)
                println("$jsonString")
                println("userLevel $userLevel")
                postLevelChange()
            }else{
                val intent = Intent(this@ActionPersoneActivity, DetailMyAddActivity::class.java)
                intent.putExtra("activityId",actionId)
                startActivity(intent)
                finish()
            }
            return true
        }

        return super.onKeyDown(keyCode, event)
    }

    private fun postLevelChange() {
        HttpHelper.sendOkHttpRequest(URL.Change_Level,userLevel, object :Callback{
            override fun onFailure(call: Call?, e: IOException?) {
                runOnUiThread {
                    Common.display(this@ActionPersoneActivity,"更改权限失败")
                    val intent = Intent(this@ActionPersoneActivity, DetailMyAddActivity::class.java)
                    intent.putExtra("activityId",actionId)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onResponse(call: Call?, response: Response) {
                val body = response.body()!!.string()
                println("level $body")
                if("success" == HttpHelper.getMessage(body)){
                    runOnUiThread {
                        Common.display(this@ActionPersoneActivity,"更改权限成功")
                    }
                }
                val intent = Intent(this@ActionPersoneActivity, DetailMyAddActivity::class.java)
                intent.putExtra("activityId",actionId)
                startActivity(intent)
                finish()
            }

        })
    }

    override fun userLevelChanghe(userId: String, level: String) {
        userLevel.put(userId,level)
        userIdList.add(userId)
        println("$userId : $level")
    }

}