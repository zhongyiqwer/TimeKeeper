package com.example.timekeeper.module_login_reg

import android.content.Intent
import android.os.Bundle
import com.example.timekeeper.R
import com.example.timekeeper.base.BaseActivity
import kotlinx.android.synthetic.main.reg_layout.*

/**
 * Created by Administrator on 2018/5/15.
 */
class reg : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reg_layout)
        btn_reg.setOnClickListener {
            val intent = Intent()
            finish()
            intent.setClass(this@reg, login::class.java)
            startActivity(intent)
        }
    }
}