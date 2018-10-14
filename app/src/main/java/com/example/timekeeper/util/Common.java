package com.example.timekeeper.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.location.Address;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.roger.catloadinglibrary.CatLoadingView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;


public class Common {
	
//	public static final String APP_ID = "1007259";
//	public static final String SECRET_ID = "AKIDBH8PhKWqB7ZtpDFBjVNHIrt5iZp6stJo";
//	public static final String SECRET_KEY = "3kRpuIdcQlXuH0LBlAXLcJ8NOyxtWkdG";
//	public static Bitmap bitmap;
//	public static final String IMG_CACHE_PATH = "/PoseCamera/";
//	public static final String FULL_IMG_CACHE_PATH = "/mnt/sdcard"+IMG_CACHE_PATH;
//	public static String local_pic_path = Environment.getExternalStorageDirectory()+"/Android/data/cn.xdu.poscam/cache/imageCache/";
	public static final String TAG = "timeKeeper:";

	public static String userId;
	public static String userName;

	public static AlertDialog mAlertDialog;

	public static ArrayList<HashMap<String,String>> fragmentData;

	public static String selecTimeActivityId;

	public static CatLoadingView catLoadingView;

	public static com.baidu.location.Address myAdress;

	public static void showCat(FragmentManager fragmentManager,String tag){
		if (catLoadingView == null){
			catLoadingView = new CatLoadingView();
		}
		catLoadingView.show(fragmentManager,tag);
	}

	public static void dissCat(){
		if (catLoadingView!=null){
			catLoadingView.dismiss();
		}
	}

	public static void putFragmentData(ArrayList<HashMap<String,String>> dataList){
		fragmentData = dataList;
	}

	//返回所有活动
	public static ArrayList<HashMap<String,String>> getFragmentData(){
		if (fragmentData!=null && !fragmentData.isEmpty()){
			return fragmentData;
		}
		return null;
	}

	//返回未完成活动
	public static ArrayList<HashMap<String,String>> getFragmentNoDone(){
		ArrayList<HashMap<String, String>> maps = new ArrayList<>();
		if (fragmentData!=null && !fragmentData.isEmpty()){
			for (HashMap map:fragmentData) {
				if (map.get("actionState")!=null && "0".equals(map.get("actionState"))){
					maps.add(map);
				}
			}
		}
		return maps;
	}

	//返回我创建活动
	public static ArrayList<HashMap<String,String>> getFragmentMy(){
		ArrayList<HashMap<String, String>> maps = new ArrayList<>();
		if (fragmentData!=null && !fragmentData.isEmpty()) {
			for (HashMap map : fragmentData) {
				if (map.get("actionCreater") != null && map.get("actionCreater").equals(userId)) {
					maps.add(map);
				}
			}
		}
		return maps;
	}

	public static String [] spiltString(String spiltdata){
		String[] splits = spiltdata.split("_");
		return splits;
	}

	public static ArrayList<String> arrToArrarList(String[] spiltString){
		ArrayList<String> dataList = new ArrayList<>();
		for (String date : spiltString){
			dataList.add(date);
		}
		return dataList;
	}

	public static void display(Context context , String str) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}


	public static int isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager)
				context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if(networkInfo == null || !networkInfo.isAvailable())
		{
			return 0;
		}
		else
		{
			return 1;
		}

	}

	//////////////UI组件
	public static void showProgressDialog(final String msg, final Context context) {
		final Activity activity = (Activity) context;
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (!activity.isFinishing()) {
					if (mAlertDialog == null) {
						mAlertDialog = new GenericProgressDialog(context);
					}
					mAlertDialog.setMessage(msg);
					((GenericProgressDialog) mAlertDialog)
							.setProgressVisiable(true);
					mAlertDialog.setCancelable(false);
					mAlertDialog.setOnCancelListener(null);
					mAlertDialog.show();
					mAlertDialog.setCanceledOnTouchOutside(false);
				}
			}
		});
	}

	public static void dismissProgressDialog(final Context context) {
		final Activity activity = (Activity) context;
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (mAlertDialog != null && mAlertDialog.isShowing()
						&& !activity.isFinishing()) {
					mAlertDialog.dismiss();
					mAlertDialog = null;
				}
			}
		});
	}

	public static String getTimeString(int[] flag){
		int length = flag.length;
		StringBuilder stringBuilder = new StringBuilder();
		for (int i=0;i<length;i++) {
			stringBuilder.append(flag[i]);
			stringBuilder.append("_");
		}
		if (stringBuilder.length()>1){
			stringBuilder.deleteCharAt(stringBuilder.length()-1);
		}
		return stringBuilder.toString();
	}

	public static int getRange(String selectTime,String selectDate){
		double time = Double.parseDouble(selectTime);
		int date = 24;
		int len = selectDate.split("_").length;
		if (len==1){
			date = 24;
		}else if (len==2){
			date = 48;
		}
		int range = date+1 - (int)time;
		return range;
	}

	public static ArrayList<String> initAdapterData(String selectTime,String selectDate){
		double time = Double.parseDouble(selectTime);
		String[] split1 = selectDate.split("_");
		int len = DateTimeHelper.daysBetween(split1[0], split1[1]);
		int date = 24*len;

		int range = date - (int)time;
		Log.e("initAdapterData","range="+range+" data="+date);
		ArrayList<String> arrayList = new ArrayList<>();
		for (int i=0;i<=range;i++){
			int start = i%24;
			double end = (i+time-1)%24;
			String endString = String.valueOf(end);
			//Log.e("initAdapterData","endBool="+ endString.endsWith(".0")+" qu="+endString.equals(".0"));
			//String[] split = endString.split(".");
			//Log.e("initAdapterData","i = "+i);
			if (endString.endsWith(".5")){
				arrayList.add(start+":00~"+((int)end+1)%24+":30");
			}else {
				arrayList.add(start+":00~"+(int)end+":59");
			}
		}
		Log.e("initAdapterData","arrayList = "+arrayList);
		return arrayList;
	}

	//邮箱验证
	public static boolean verifyEamil(String emil){
		final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		boolean matches = Pattern.matches(REGEX_EMAIL, emil);
		return matches;
	}

	public static boolean contains(String s1,String s2){
		if (!s1.isEmpty()){
			if (s1.contains(s2)){
				return true;
			}
		}
		return false;
	}

}
