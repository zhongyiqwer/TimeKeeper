package com.example.timekeeper.module_login_reg

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import com.example.timekeeper.R
import com.example.timekeeper.activity.MainActivity
import com.example.timekeeper.activity.selectTimeActivity
import com.example.timekeeper.base.BaseActivity
import com.example.timekeeper.util.Common
import com.example.timekeeper.util.EncodeAndDecode
import com.example.timekeeper.util.HttpHelper
import com.example.timekeeper.util.URL
import kotlinx.android.synthetic.main.login_layout.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONException
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by ZJX on 2018/5/4.
 */
class login : BaseActivity() ,View.OnClickListener{

    //internal var intentParam :String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)

        btn_login.setOnClickListener(this)
        editV__register_toregister.setOnClickListener(this)
        editV_forget_password.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        if (v == editV__register_toregister){
            val intent = Intent()
            intent.setClass(this@login, reg::class.java)
            startActivity(intent)
            finish()
        }
        if (v == editV_forget_password){
            Common.display(this@login, "暂未开放")
           /* val intent = Intent()
            intent.setClass(this@login, findPW::class.java)
            startActivity(intent)
            finish()*/
        }

        if (v == btn_login){
            /*val intent = Intent()
            finish()
            intent.setClass(this@login, MainActivity::class.java)
            startActivity(intent)*/

            val flag = Common.isNetworkAvailable(this)
            if (flag == 0) {
                Common.display(this, "请开启手机网络")
            } else {

                if (editV_frag_login_phone.getText().length == 11 && editV_frag_login_password.getText().toString() != "") {
                    val username = editV_frag_login_phone.getText().toString()
                    val md5pw = EncodeAndDecode.getMD5Str(editV_frag_login_password.getText().toString())
                    val map = getMap(username,md5pw)
                    HttpHelper.sendOkHttpRequest(URL.LOGIN_URL,map,object :Callback{
                        override fun onFailure(call: Call?, e: IOException?) {
                            runOnUiThread {
                                Common.display(this@login, "登录失败")
                            }
                        }
                        override fun onResponse(call: Call?, response: Response?) {
                            if ("success".equals(HttpHelper.getMessage(response!!.body()!!.string()))){
                                runOnUiThread {
                                    val edit = this@login.getSharedPreferences("config", 0).edit()
                                    edit.putString("userId",username)
                                    edit.putString("password",md5pw)
                                    edit.commit()
                                    Common.userId = username

                                    println("lonig:"+Common.selecTimeActivityId)

                                    if (Common.selecTimeActivityId!=null){
                                        val intent = Intent()
                                        intent.setClass(this@login, selectTimeActivity::class.java)
                                        intent.putExtra("activityId",Common.selecTimeActivityId)
                                        Common.selecTimeActivityId = null
                                        startActivity(intent)
                                        finish()
                                    }else{
                                        val intent = Intent()
                                        intent.setClass(this@login, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                            }else{
                                Looper.prepare()
                                Common.display(this@login, "登录失败")
                                Looper.loop()
                            }
                        }
                    })
                }else{
                    Common.display(this@login, "用户名或密码错误")
                }
            }
        }
    }

    fun getMap(ln: String, pw: String): HashMap<String,String> {
        val paramHM = HashMap<String, String>()
        paramHM.put("id", ln)
        paramHM.put("password", pw)
        return paramHM
    }


}