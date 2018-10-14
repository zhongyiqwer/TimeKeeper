package com.example.timekeeper.activity

import android.content.*
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.example.timekeeper.R
import com.example.timekeeper.base.BaseActivity
import com.example.timekeeper.fragment.AllActionFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import android.graphics.Bitmap
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AlertDialog
import android.text.method.ScrollingMovementMethod
import android.view.KeyEvent
import android.widget.TextView
import com.uuzuche.lib_zxing.activity.CaptureActivity
import android.widget.Toast
import com.example.timekeeper.ActionService
import com.example.timekeeper.fragment.RecommendFragment
import com.example.timekeeper.module_login_reg.login
import com.example.timekeeper.util.Common
import com.example.timekeeper.util.ImageUtil
import com.uuzuche.lib_zxing.activity.CodeUtils
import kotlinx.android.synthetic.main.dialog_actions.*
import kotlinx.android.synthetic.main.dialog_actions.view.*


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener ,BottomNavigationBar.OnTabSelectedListener{

    companion object {
        val REQUEST_CODE:Int=1
        val REQUEST_IMAGE:Int=2
    }

    lateinit internal var allFragment :AllActionFragment
    lateinit internal var noDoneFragment :AllActionFragment
    lateinit internal var myCreateFragment :AllActionFragment
    lateinit internal var recommendFragment:RecommendFragment

    lateinit internal var bottomNavigationBar: BottomNavigationBar
    internal var lastSelectedPosition = 0

    lateinit var localReceiver: LocalReceiver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initBottomNavi()

        //新建活动
        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
            finish()
        }

        //drawer_layout的进场与退场动画
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        val headerView = nav_view.getHeaderView(0)
        val name = headerView.findViewById<TextView>(R.id.user_name)
        //val email = headerView.findViewById<TextView>(R.id.user_emil)
        //val image = headerView.findViewById<TextView>(R.id.user_image)
        if (Common.userName != null){
            println(Common.userName)
            name.text = Common.userName
        }

        //开启通知服务
        val intent = Intent(this, ActionService::class.java)
        startService(intent)

        //开启本地广播
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.example.timekeeper.notify")
        localReceiver = LocalReceiver()
        LocalBroadcastManager.getInstance(this).registerReceiver(localReceiver,intentFilter)
    }

    inner class LocalReceiver:BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent) {
            when(intent.action){
                "com.example.timekeeper.notify"->{
                    println("收到本地广播")
                    val list = intent.getStringArrayListExtra("actions")
                    val noDone = Common.getFragmentNoDone()
                    val arrayList = ArrayList<HashMap<String, String>>()
                    for (map1 in noDone){
                        for (s in list){
                            if (map1["actionId"] == s){
                                arrayList.add(map1)
                            }
                        }
                    }
                    val view = View.inflate(this@MainActivity, R.layout.dialog_actions, null)
                    val builder = StringBuilder()
                    for (map2 in arrayList){
                        val actionName = map2["actionName"]
                        builder.append("活动:\"$actionName\"快要开始了\r\n")
                    }
                    view.actionTv.movementMethod = ScrollingMovementMethod.getInstance()
                    view.actionTv.text = builder.toString()
                    val alertDialog = AlertDialog.Builder(this@MainActivity)
                            .setView(view)
                            .show()
                    view.btnOk.setOnClickListener {
                        alertDialog.cancel()
                    }
                }
            }
        }

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                val intent = Intent(this, CaptureActivity::class.java)
                startActivityForResult(intent, REQUEST_CODE)
            }
            R.id.nav_gallery -> {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_IMAGE)
            }
            R.id.nav_slideshow -> {
                val edit = getSharedPreferences("config", 0).edit()
                edit.clear()
                edit.commit()
            }
            R.id.nav_manage -> {
                val intent = Intent(this, ChangeMyActivity::class.java)
                startActivity(intent)
                //finish()
            }
            R.id.nav_share -> {
                val edit = getSharedPreferences("config", 0).edit()
                edit.clear()
                edit.commit()
                val intent = Intent(this, login::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_send -> {
                KillAllActivitys()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun initBottomNavi(){
        allFragment = AllActionFragment.newInstance()
        noDoneFragment = AllActionFragment.newInstance()
        myCreateFragment = AllActionFragment.newInstance()
        recommendFragment = RecommendFragment()
        bottomNavigationBar = findViewById<BottomNavigationBar>(R.id.bottomNavigationBar)
        bottomNavigationBar.setTabSelectedListener(this)
        bottomNavigationBar.clearAll()
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_DEFAULT)
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_DEFAULT)
        bottomNavigationBar
                .addItem(BottomNavigationItem(R.drawable.ic_all_activity, "所有任务").setActiveColorResource(R.color.orange))
                .addItem(BottomNavigationItem(R.drawable.ic_nodone_activity, "未完成").setActiveColorResource(R.color.teal))
                .addItem(BottomNavigationItem(R.drawable.ic_myadd_activity, "我创建").setActiveColorResource(R.color.blue))
                .addItem(BottomNavigationItem(R.drawable.ic_sharefile, "智能推荐").setActiveColorResource(R.color.brown))
                .initialise()
        bottomNavigationBar.selectTab(if (lastSelectedPosition > 3) 3 else lastSelectedPosition, true)

        setDefaultFragment()
    }

    private fun setDefaultFragment() {

        supportFragmentManager.beginTransaction().apply {
            val bundle = Bundle()
            bundle.putString("fragmentType","0")
            allFragment.setArguments(bundle)
            toolbar.title = "Timekeeper"
            fab.visibility = View.VISIBLE
            replace(R.id.fragment_contain, allFragment)
        }.commitAllowingStateLoss()
    }

    override fun onTabReselected(position: Int) {

    }

    override fun onTabUnselected(position: Int) {

    }

    override fun onTabSelected(position: Int) {
        lastSelectedPosition = position
        replaceFragments(position)
    }

    private fun replaceFragments(position: Int) {
        supportFragmentManager.beginTransaction().apply {
            when (position) {
                0 -> {  val bundle = Bundle()
                        bundle.putString("fragmentType","0")
                        allFragment.setArguments(bundle)
                        toolbar.title = "Timekeeper"
                        fab.visibility = View.VISIBLE
                        replace(R.id.fragment_contain, allFragment)}
                1 -> {val bundle = Bundle()
                      bundle.putString("fragmentType","1")
                    noDoneFragment.setArguments(bundle)
                    toolbar.title = "Timekeeper"
                    fab.visibility = View.VISIBLE
                      replace(R.id.fragment_contain, noDoneFragment)}
                2 -> {val bundle = Bundle()
                      bundle.putString("fragmentType","2")
                    myCreateFragment.setArguments(bundle)
                    toolbar.title = "Timekeeper"
                    fab.visibility = View.VISIBLE
                      replace(R.id.fragment_contain, myCreateFragment)}
                3 -> { val bundle = Bundle()
                    recommendFragment.setArguments(bundle)
                    toolbar.title = "智能推荐"
                    fab.visibility = View.GONE
                       replace(R.id.fragment_contain, recommendFragment)}
            }
        }.commitAllowingStateLoss()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE){
            println("扫描返回")
           if (data != null){
               val bundle = data.extras
               if(bundle == null){
                   return
               }
               if (bundle.getInt(CodeUtils.RESULT_TYPE) === CodeUtils.RESULT_SUCCESS) {
                   val result = bundle.getString(CodeUtils.RESULT_STRING)
                   println("解析结果"+result)
                   val split = result!!.split("=")
                   val s = split[1]
                   println("id:"+s)

                   val intent = Intent(this@MainActivity, SelectTimeActivity::class.java)
                   intent.putExtra("activityId",s)
                   startActivity(intent)
                   finish()

               } else if (bundle.getInt(CodeUtils.RESULT_TYPE) === CodeUtils.RESULT_FAILED) {
                   println("解析二维码失败")
                   Toast.makeText(this@MainActivity, "解析二维码失败", Toast.LENGTH_LONG).show()
               }
           }
        }

        if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                val uri = data.data

                CodeUtils.analyzeBitmap(ImageUtil.getImageAbsolutePath(this, uri),object :CodeUtils.AnalyzeCallback{
                    override fun onAnalyzeSuccess(mBitmap: Bitmap?, result: String?) {
                        println("解析结果"+result)
                        val split = result!!.split("=")
                        val s = split[1]
                        println("id:"+s)

                        val intent = Intent(this@MainActivity, SelectTimeActivity::class.java)
                        intent.putExtra("activityId",s)
                        startActivity(intent)
                        finish()
                    }

                    override fun onAnalyzeFailed() {
                        Toast.makeText(this@MainActivity, "解析二维码失败", Toast.LENGTH_LONG).show();
                    }
                })

            }
        }
    }

    private var firstTime:Long=0
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode === KeyEvent.KEYCODE_BACK && event!!.getAction() === KeyEvent.ACTION_DOWN) {
            val secondTime = System.currentTimeMillis()
            if (secondTime - firstTime > 2000) {
                Toast.makeText(this@MainActivity, "再按一次退出程序", Toast.LENGTH_SHORT).show()
                firstTime = secondTime
                return true
            } else {
                System.exit(0)
            }
        }

        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(localReceiver)
    }
}
