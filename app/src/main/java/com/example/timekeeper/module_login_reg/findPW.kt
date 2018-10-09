package com.example.timekeeper.module_login_reg

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.example.timekeeper.R
import com.example.timekeeper.base.BaseActivity
import com.example.timekeeper.util.Common
import com.example.timekeeper.util.HttpHelper
import com.example.timekeeper.util.URL
import kotlinx.android.synthetic.main.activity_fin_pw.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

/**
 * Created by Administrator on 2018/5/15.
 */
class findPW :BaseActivity(),View.OnClickListener{

    private var state:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fin_pw)
        btn_back.setOnClickListener(this)
        btn_find_byphone.setOnClickListener(this)
        btn_find_byemail.setOnClickListener(this)
        btn_confirm.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            btn_back->{
                goBack(this,login::class.java)
            }
            btn_find_byphone->{
                if (state){
                    edit_findPw.hint = "请输入手机号..."
                    state = false
                }
            }
            btn_find_byemail->{
                if (!state){
                    edit_findPw.hint = "请输入邮箱..."
                    state = true
                }
            }
            btn_confirm->{
                val string = edit_findPw.text.trim().toString()
                val map = HashMap<String, String>()
                if(!state){
                    if (string.length == 11){
                        map.put("state","0")
                        map.put("phone",string)
                        //findPw(map)
                    }else{
                        Common.display(this,"手机号错误")
                    }
                }else{
                    if(Common.verifyEamil(string)){
                        map.put("state","1")
                        map.put("phone",string)
                        //findPw(map)
                    }else{
                        Common.display(this,"邮箱错误")
                    }
                }
            }
        }
    }

    private fun findPw(map: HashMap<String, String>) {
        HttpHelper.sendOkHttpRequest(URL.FindPw_Action,map,object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {

            }

            override fun onResponse(call: Call?, response: Response?) {
                val body = response!!.body()!!.string()
                println(body)
                if ("success".equals(HttpHelper.getMessage(body))){
                    runOnUiThread {
                        //Common.display(this@findPW,"修该成功")
                    }
                }
            }
        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            goBack(this,login::class.java)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun <T> goBack(activity: Activity, java: Class<T>) {
        val intent = Intent(activity, java)
        startActivity(intent)
        activity.finish()
    }

}