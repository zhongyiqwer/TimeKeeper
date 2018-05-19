package com.example.timekeeper.activity

import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.timekeeper.R
import com.example.timekeeper.base.BaseActivity
import com.example.timekeeper.dapater.ActionPersoneAdapter
import java.util.*

/**
 * Created by Administrator on 2018/5/18.
 */
class actionPersoneActivity : BaseActivity() ,AdapterView.OnItemClickListener{

    lateinit internal var listView: ListView
    lateinit internal var textView3: TextView
    lateinit internal var name: TextView
    lateinit internal var image: ImageView

    lateinit internal var dataList: ArrayList<HashMap<String, String>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.take_member_layout)
        initview()
        initDate()
        setList()
    }

    private fun initDate() {
        dataList = ArrayList<HashMap<String, String>>()
        //假数据
        for (i in 1 .. 20){
            val hashMap = HashMap<String, String>()
            hashMap.put("image","www.baidu.com"+i)
            hashMap.put("name","zhangsan"+i)
            hashMap.put("level","中")
            dataList.add(hashMap)
        }
    }

    private fun setList() {
        val adapter = ActionPersoneAdapter(this, dataList)
        listView.setAdapter(adapter)
        listView.setOnItemClickListener(this)
    }


    private fun initview() {
        listView = findViewById(R.id.listView)
        textView3 = findViewById(R.id.textView3)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }
}