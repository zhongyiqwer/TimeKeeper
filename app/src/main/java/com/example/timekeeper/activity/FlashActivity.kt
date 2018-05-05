package com.example.timekeeper.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.example.timekeeper.R
import com.example.timekeeper.base.BaseActivity
import com.example.timekeeper.module_b_main.MainActivity
import java.util.*


/**
 * Created by ZJX on 2018/5/3.
 */
class FlashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flash_activity_layout)
        //进行一些初始化
        val intent = Intent(this, MainActivity::class.java)
        val timer = Timer()
        val task = object : TimerTask(){
            override fun run() {
                setId()
                startActivity(intent)
                finish()
            }

        }
        timer.schedule(task,1400)


    }

    private fun setId(){
        val preferences = getSharedPreferences("config", 0)
        val userId = preferences.getString("userId", null)
        if (userId != null){
            //todo
        }
    }

}