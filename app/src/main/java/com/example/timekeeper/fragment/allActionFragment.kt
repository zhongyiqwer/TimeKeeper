package com.example.timekeeper.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.example.timekeeper.R
import java.util.ArrayList

/**
 * Created by Administrator on 2018/5/17.
 */
class allActionFragment : Fragment() {
    lateinit internal var dataList: ArrayList<String>
    lateinit internal var listView:ListView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragmant_all_action, container, false)
        listView = view.findViewById<ListView>(R.id.list_view)
        initData()
        val adapter = ArrayAdapter<String>(context!!, android.R.layout.simple_list_item_1, dataList)
        listView.setAdapter(adapter)
        return view
    }

    fun initData(){
       dataList = arrayListOf("a","b","c","a","b","c","a","b","c","a","b","c","a","b","c")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        listView.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(context!!," "+position,Toast.LENGTH_SHORT).show()
        }
    }
}