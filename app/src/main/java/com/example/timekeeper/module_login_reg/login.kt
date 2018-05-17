package com.example.timekeeper.module_login_reg

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import com.example.timekeeper.R
import com.example.timekeeper.activity.MainActivity
import com.example.timekeeper.base.BaseActivity
import com.example.timekeeper.util.Common
import com.example.timekeeper.util.EncodeAndDecode
import com.example.timekeeper.util.HttpHelper
import com.example.timekeeper.util.URL
import kotlinx.android.synthetic.main.login_layout.*
import kotlinx.android.synthetic.main.reg_layout.*
import org.json.JSONException
import java.io.IOException
import java.util.HashMap

/**
 * Created by ZJX on 2018/5/4.
 */
class login : BaseActivity() ,View.OnClickListener{

    internal var intentParam :Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intentParam = intent.getIntExtra("userId", 0)
        if (intentParam != 0){
            val paramHM = HashMap<String, String>()
            paramHM.set("userId",intentParam.toString())
            val json = HttpHelper.postData(URL.LOGIN_ById,paramHM,null)
            val code = HttpHelper.getCode(json)
            if (code == 200){
                val intent = Intent()
                finish()
                intent.setClass(this@login, MainActivity::class.java)
                startActivity(intent)
            }
        }

        setContentView(R.layout.login_layout)
        btn_login.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        if (v == editV__register_toregister){
            val intent = Intent()
            finish()
            intent.setClass(this@login, reg::class.java)
            startActivity(intent)
        }
        if (v == editV_forget_password){
            val intent = Intent()
            finish()
            intent.setClass(this@login, findPW::class.java)
            startActivity(intent)
        }

        if (v == btn_login){
            val flag = Common.isNetworkAvailable(this)
            if (flag == 0) {
                Common.display(this, "请开启手机网络")
            } else {

                if (editV_frag_login_phone.getText().toString() != "" && editV_frag_login_password.getText().toString() != "") {
                    try {

                        if (intentParam != null) {
                            /*if (edLoginname.getText().toString() != userAL.get(0).getLoginName() || edPassword.getText().toString() != userAL.get(0).getPassword())
                            //判断用户是否修改了文本框
                            {
                                md5pw = EncodeAndDecode.getMD5Str(edPassword.getText().toString())
                                val json = postData(edLoginname.getText().toString(), md5pw)
                                val userHM = HttpHelper.AnalysisUid(json)
                                if (userHM != null) {
                                    Common.userId = userHM!!.get("userid")
                                    dbServer = DBServer(this)
                                    dbServer.updateUser(Common.userId,
                                            edLoginname.getText().toString(), md5pw)

                                    val intent = Intent()
                                    finish()
                                    intent.setClass(this@LoginActivity, UserActivity::class.java!!)
                                    startActivity(intent)
                                } else {
                                    Common.display(this@LoginActivity, "登录失败,用户名或密码错误")
                                }

                            } else {
                                val intent = Intent()
                                finish()
                                intent.setClass(this@LoginActivity, UserActivity::class.java!!)
                                startActivity(intent)
                            }*/
                        } else {
                            val md5pw = EncodeAndDecode.getMD5Str(editV_frag_login_password.getText().toString())
                            val json = postData(editV_frag_login_phone.getText().toString(), md5pw)
                            val userHM = HttpHelper.AnalysisUid(json)
                            if (userHM != null) {
                                Common.userId = userHM.get("userId") as Int
                                val sharedPreferences = this.getSharedPreferences("config", android.content.Context.MODE_PRIVATE)
                                val edit = sharedPreferences.edit()
                                edit.putInt("userId",Common.userId)
                                edit.commit()

                                val intent = Intent()
                                finish()
                                intent.setClass(this@login, MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                Common.display(this@login, "登录失败,用户名或密码错误")
                            }
                        }
                    } catch (e: JSONException) {
                        // TODO Auto-generated catch block
                        Common.display(this@login, "登录失败")
                        e.printStackTrace()
                    } catch (e: IOException) {
                        Common.display(this@login, "登录失败")
                        e.printStackTrace()
                    } catch (e: Exception) {
                        Common.display(this@login, "登录失败")
                        e.printStackTrace()
                    }

                }
            }
        }
    }

    @Throws(Exception::class)
    fun postData(ln: String, pw: String): String {
        val paramHM = HashMap<String, String>()
        paramHM.put("username", ln)
        paramHM.put("password", pw)
        return HttpHelper.postData(URL.LOGIN_URL, paramHM, null)
    }


}