package com.example.timekeeper.activity

import android.content.Intent
import android.os.Bundle
import com.example.timekeeper.R
import com.example.timekeeper.base.BaseActivity
import com.example.timekeeper.util.Common
import kotlinx.android.synthetic.main.activity_change_my.*

/**
 * Created by ZY on 2018/6/15.
 */
class ChangeMyActivity :BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_my)

        button2.setOnClickListener {
            if (editText.text.toString()!=""){
                //Common.userName = editText.text.toString()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}