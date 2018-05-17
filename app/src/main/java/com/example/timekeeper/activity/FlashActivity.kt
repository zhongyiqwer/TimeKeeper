package com.example.timekeeper.activity

import android.content.Intent
import android.os.Bundle
import com.example.timekeeper.R
import com.example.timekeeper.base.BaseActivity
import com.example.timekeeper.module_login_reg.login
import java.util.*


/**
 * Created by ZJX on 2018/5/3.
 */
class FlashActivity : BaseActivity() {

    internal var intent :Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flash_activity_layout)
        //进行一些初始化
        intent = Intent(this, MainActivity::class.java)
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
        val userId = preferences.getInt("userId", 0)
        if (userId != 0){
            intent!!.putExtra("userId",userId)
        }
    }

}