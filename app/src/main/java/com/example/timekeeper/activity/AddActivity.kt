package com.example.timekeeper.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.*
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener
import com.baidu.mapapi.search.sug.SuggestionResult
import com.baidu.mapapi.search.sug.SuggestionSearch
import com.baidu.mapapi.search.sug.SuggestionSearchOption
import com.example.timekeeper.R
import com.example.timekeeper.base.BaseActivity
import com.example.timekeeper.util.Common
import com.example.timekeeper.util.DateTimeHelper
import com.example.timekeeper.util.HttpHelper
import com.example.timekeeper.util.URL
import kotlinx.android.synthetic.main.add_activity_gridview_layout.*
import kotlinx.android.synthetic.main.add_layout.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.util.*


/**
 * Created by ZJX on 2018/5/7.
 */
class AddActivity : BaseActivity() ,View.OnClickListener,OnGetSuggestionResultListener{

    val dataList = ArrayList<Map<String, String>>()

    var flag :Array<Int> = Array(24,{0})

    internal var select_type : Int?=0
    lateinit var mSuggestionSearch: SuggestionSearch
    lateinit var suggest:MutableList<String>
    lateinit var sugAdapter:ArrayAdapter<String>
    lateinit var actionPlace:AutoCompleteTextView
    lateinit var mLocationClient:LocationClient

    lateinit var adapter:SimpleAdapter

    private var mSpinnerType = ""
    private var mSpinnerNum = ""

    private var mTimeSelectType = 0
    private var mDateSelectType = 0

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.add_layout)
        val gridView2: GridView = findViewById<GridView>(R.id.gridView2)
        bt_fabu.setOnClickListener(this)
        IB_can.setOnClickListener(this)
        IB_nocan.setOnClickListener(this)
        //获取定位数据
        startLocate()
        // 初始化建议搜索模块，注册建议搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance()
        mSuggestionSearch.setOnGetSuggestionResultListener(this)
        actionPlace = findViewById<AutoCompleteTextView>(R.id.edit_action_place)
        sugAdapter = ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line)
        actionPlace.setAdapter(sugAdapter)
        actionPlace.threshold = 1
        actionPlace.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.length<=0){ return }
                 // 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                mSuggestionSearch.requestSuggestion(SuggestionSearchOption()
                        .keyword(s.toString()).city(Common.myAdress.city))
            }
        })

        val array1 = this.resources.getStringArray(R.array.spinnarr_type)
        mSpinnerType = array1[0]
        val linkedList1 = LinkedList<String>()
        linkedList1.addAll(array1)
        spinner_type.attachDataSource(linkedList1)
        spinner_type.setOnItemSelectedListener(object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //得到position或position的文字到时候发后台
                mSpinnerType = array1[position]
            }
        })

        val array2 = this.resources.getStringArray(R.array.spinnarr_num)
        mSpinnerNum = array2[0]
        val linkedList2 = LinkedList<String>()
        linkedList2.addAll(array2)
        spinner_num.attachDataSource(linkedList2)
        spinner_num.setOnItemSelectedListener(object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mSpinnerNum = array2[position]
            }
        })

        /*val array3 = this.resources.getStringArray(R.array.spinnarr_tiem)
        val linkedList3 = LinkedList<String>()
        linkedList3.addAll(array3)
        spinner_time.attachDataSource(linkedList3)
        spinner_time.setOnItemSelectedListener(object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mSpinnerTime = array3[position]
            }
        })*/

        val array4 = this.resources.getStringArray(R.array.spinnarr_tiemSelection)
        val linkedList4 = LinkedList<String>()
        linkedList4.addAll(array4)
        spinner_timeSelection.attachDataSource(linkedList4)
        spinner_timeSelection.setOnItemSelectedListener(object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mTimeSelectType = position
                changeGridData()
            }
        })

        val array5 = this.resources.getStringArray(R.array.spinnarr_dateSelection)
        val linkedList5 = LinkedList<String>()
        linkedList5.addAll(array5)
        spinner_dateSelection.attachDataSource(linkedList5)
        spinner_dateSelection.setOnItemSelectedListener(object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mDateSelectType = position
                changeGridData()
            }
        })


        val from = arrayOf("text")
        val to = intArrayOf(R.id.grid_tv)

        adapter = SimpleAdapter(this,dataList,R.layout.gridview_item_layout,from,to)
        gridView2.setAdapter(adapter)
        gridView2.setOnItemClickListener { parent, view, position, id ->
            if (flag[position] == 0){
                if (select_type == 0){
                    flag[position] =1
                    view!!.setBackgroundResource(R.color.bar_deepgreen)
                }else if (select_type == 1){
                    flag[position] =2
                    view!!.setBackgroundResource(R.color.bar_red)
                }
            }else if (flag[position] == 1){
                if (select_type == 0){
                    flag[position] =0
                    view!!.setBackgroundResource(R.color.white)
                }else if (select_type == 1){
                    flag[position] =2
                    view!!.setBackgroundResource(R.color.bar_red)
                }
            }else if(flag[position] == 2){
                if (select_type == 0){
                    flag[position] =1
                    view!!.setBackgroundResource(R.color.bar_deepgreen)
                }else if (select_type == 1){
                    flag[position] =0
                    view!!.setBackgroundResource(R.color.white)
                }
            }
        }

        changeGridData()
    }

    private fun startLocate() {
        mLocationClient = LocationClient(this);     //声明LocationClient类
        mLocationClient.registerLocationListener(object :BDAbstractLocationListener(){
            override fun onReceiveLocation(location: BDLocation?) {
                if (location!=null){
                    Common.myAdress = location.address
                }
            }
        })
        val option = LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving)//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
        //开启定位
        mLocationClient.start()

    }

    private fun changeGridData(){

        for (i in flag.indices){
            flag[i] = 0
            val view = gridView2.getChildAt(i)
            if (view!=null){
                view!!.setBackgroundResource(R.color.white)
            }
        }
        dataList.clear()
        var time = 1.0
        var date = 24
        if(mDateSelectType==0){
            date = 24
        }else if(mDateSelectType==1){
            date = 48
        }

        if (mTimeSelectType==1){
            time = 1.5
        }else if (mTimeSelectType==2){
            time = 2.0
        }else if (mTimeSelectType==3){
            time = 3.0
        }else if (mTimeSelectType==4){
            time = 4.0
        }else if (mTimeSelectType==5){
            time = 6.0
        }else if(mTimeSelectType == 6){
            time = 12.0
        }else if(mTimeSelectType == 7){
            time = 24.0
        }
        if (mTimeSelectType<8){
            date = date - time.toInt()
            flag = Array(date+1,{0})
            for (i in 0..date){
                val start  = i%24
                val end :Double= (i+time-1)%24
                val data = HashMap<String,String>()
                val split = end.toString().split('.')
                if (split[1] == "5"){
                    data.put("text",""+start+":00~"+(split[0].toInt()+1)%24+":30")
                }else{
                    println(end.toString()+"  "+split[0].toInt())
                    data.put("text",""+start+":00~"+split[0]+":59")
                }
                dataList.add(data)
            }
        }else{
            /*val range:Int = (date/time).toInt()
            flag = Array(range,{0})
            for (i in 0..range-1){
                val temp :Double= (i*time)%24
                val data = HashMap<String,String>()
                val split = temp.toString().split('.')
                if (split[1] == "5"){
                    data.put("text",""+split[0]+":30")
                }else{
                    data.put("text",""+split[0]+":00")
                }
                dataList.add(data)
            }*/
        }

        adapter.notifyDataSetChanged()
    }

    override fun onClick(v: View?) {
       //点击事件
        if (v == IB_can){
            select_type = 0
            IB_can.background = resources.getDrawable(R.drawable.btn_select_green)
            IB_nocan.background = resources.getDrawable(R.drawable.btn_big_red)
        }

        if (v == IB_nocan){
            select_type = 1
            IB_nocan.background = resources.getDrawable(R.drawable.btn_select_red)
            IB_can.background = resources.getDrawable(R.drawable.btn_deepgreen)
        }


        if (v == bt_fabu){
            addOneAciton()
        }
    }

    fun addOneAciton(){
        if (editText_name.getText().toString().trim() == "" ||
                editText_des.getText().toString().trim() == "" ||
                !gridTime() || edit_action_place.text.toString().trim() == ""){
            Common.display(this,"不能为空")
        }else{

            val map = HashMap<String, String>()
            map.put("activityCreator",Common.userId)
            map.put("activityName",editText_name.text.toString().trim())
            map.put("activityType",mSpinnerType)
            map.put("activityNum",mSpinnerNum)
            map.put("activityPlace",edit_action_place.text.toString().trim())
            map.put("activityDescription",editText_des.text.toString().trim())

            val arr = this.resources.getStringArray(R.array.spinnarr_tiemSelection)
            val activityContinueTime = arr[mTimeSelectType].split("小时")[0]
            map.put("activityContinueTime",""+activityContinueTime)

            var activitySelectDate = DateTimeHelper.getCurrentDateTime2()+"_"+DateTimeHelper.getCurrentDateTime2()
            if(mDateSelectType==1){
                activitySelectDate = DateTimeHelper.getCurrentDateTime2()+"_"+DateTimeHelper.getTomorrowDateTime()
            }else if(mDateSelectType==2){
                //todo
            }
            map.put("activitySelectDate",activitySelectDate)
            //把flag中的2全替换为0
            changeFlag()
            map.put("activityTime", Common.getTimeString(flag.toIntArray()))
            println("map数据为："+map.toString())
            HttpHelper.sendOkHttpRequest(URL.ADD_Action,map,object :Callback{
                override fun onFailure(call: Call?, e: IOException?) {
                    runOnUiThread {
                        Common.display(this@AddActivity,"添加失败")
                    }
                }

                override fun onResponse(call: Call?, response: Response?) {
                    val body = response!!.body()!!.string()
                    println("add:$body")
                    if ("success" == HttpHelper.getMessage(body)){
                        runOnUiThread {
                            Common.display(this@AddActivity,"添加成功")
                            val intent = Intent(this@AddActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            })
        }
    }

    private fun gridTime() : Boolean{
        for (i in flag){
            if (i != 0){
                return true
            }
        }
        return false
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }

    fun changeFlag(){
        for (i in flag.indices){
            if(flag[i] == 2){
                flag[i] = 0
            }
        }
    }

    /**
     * 获取在线建议搜索结果，得到requestSuggestion返回的搜索结果
     */
    override fun onGetSuggestionResult(res: SuggestionResult?) {
        if (res == null || res.getAllSuggestions() == null) {
            return
        }
        suggest = ArrayList<String>()
        for (info in res.getAllSuggestions()) {
            if (info.key != null) {
                suggest.add(info.key)
            }
        }
        sugAdapter = ArrayAdapter<String>(this@AddActivity, android.R.layout.simple_dropdown_item_1line, suggest)
        actionPlace.setAdapter(sugAdapter)
        sugAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        mSuggestionSearch.destroy()
        mLocationClient.stop()
    }
}