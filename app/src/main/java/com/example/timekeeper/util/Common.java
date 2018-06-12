package com.example.timekeeper.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;


public class Common {
	
//	public static final String APP_ID = "1007259";
//	public static final String SECRET_ID = "AKIDBH8PhKWqB7ZtpDFBjVNHIrt5iZp6stJo";
//	public static final String SECRET_KEY = "3kRpuIdcQlXuH0LBlAXLcJ8NOyxtWkdG";
//	public static Bitmap bitmap;
//	public static final String IMG_CACHE_PATH = "/PoseCamera/";
//	public static final String FULL_IMG_CACHE_PATH = "/mnt/sdcard"+IMG_CACHE_PATH;
//	public static String local_pic_path = Environment.getExternalStorageDirectory()+"/Android/data/cn.xdu.poscam/cache/imageCache/";
//	public static User user;
	public static final String TAG = "timeKeeper:";

	public static String userId;

	public static String fragParamName;
	public static String fragParam;

	public static boolean isVisible;
	public static AlertDialog mAlertDialog;

	public static ArrayList<HashMap<String,String>> fragmentData;

	public static String selecTimeActivityId;


	public static void putFragmentData(ArrayList<HashMap<String,String>> dataList){
		fragmentData = dataList;
	}

	//返回所有活动
	public static ArrayList<HashMap<String,String>> getFragmentData(){
		return fragmentData;
	}

	//返回未完成活动
	public static ArrayList<HashMap<String,String>> getFragmentNoDone(){
		ArrayList<HashMap<String, String>> maps = new ArrayList<>();
		for (HashMap map:fragmentData) {
			if (map.get("actionState").equals("0")){
				maps.add(map);
			}
		}
		return maps;
	}

	//返回我创建活动
	public static ArrayList<HashMap<String,String>> getFragmentMy(){
		ArrayList<HashMap<String, String>> maps = new ArrayList<>();
		for (HashMap map:fragmentData) {
			if (map.get("actionCreater").equals(userId)){
				maps.add(map);
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
			if (!noText().contains(i)){
				stringBuilder.append(flag[i]);
				stringBuilder.append("_");
			}
		}
		if (stringBuilder.length()>1){
			stringBuilder.deleteCharAt(stringBuilder.length()-1);
		}
		return stringBuilder.toString();
	}

	public static ArrayList<Integer> noText(){
		ArrayList<Integer> noText = new ArrayList();
		noText.add(0);
		noText.add(1);
		noText.add(2);
		noText.add(3);
		noText.add(4);
		noText.add(8);
		noText.add(12);
		noText.add(16);
		return noText;
	}

	public static ArrayList<String> initAdapterData(ArrayList<String> dataList){
		 String [] name = {"\\", "今天", "明天", "后天",
				"9:00", "", "", "",
				"15:00", "", "", "",
				"17:00", "", "", "",
				"19:00", "", "", ""};
		int j=0;
		for (int i=0;i<name.length;i++){
			if (!noText().contains(i) && !dataList.isEmpty()){
				if (dataList.size()>=j){
					name[i]=dataList.get(j);
				}
				j++;
			}
		}
		ArrayList<String> arrayList = new ArrayList<>();
		for (int i=0;i<name.length;i++){
			arrayList.add(name[i]);
		}
		return arrayList;
	}


}
