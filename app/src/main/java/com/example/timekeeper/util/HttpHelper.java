package com.example.timekeeper.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;


/**
 * Created by Administrator on 2018/5/15.
 */
public class HttpHelper {
    private static final String TAG = "uploadFile";
    private static final int TIME_OUT = 10 * 1000; // 超时时间
    private static final String CHARSET = "utf-8"; // 设置编码


    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url    发送请求的URL
     * @return URL所代表远程资源的响应
     */
    public static String sendGet(String url) {

        final String[] str = {""};
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

       client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                str[0] = response.body().toString();
            }
        });

        return str.toString();
    }

    /**
     * 同时传送文件和字符串
     */

    public static String postData(String url, HashMap<String, String>
            map, HashMap<String, String> fileHM) throws Exception {

        final String[] str = {""};
        OkHttpClient client = new OkHttpClient();
        //FormBody.Builder body = new FormBody.Builder();
        MultipartBody.Builder requestBody = new MultipartBody.Builder();
        if (map != null && !map.isEmpty()) {
            //上传参数
            for (String key : map.keySet()) {
                //body.add(key, map.get(key));
                requestBody.addFormDataPart(key,map.get(key));
            }


        }
        if (fileHM != null && !fileHM.isEmpty()){
            for (String key : map.keySet()){
                File file = new File(map.get(key));
                requestBody.addFormDataPart(key,file.getPath(),RequestBody.create(MediaType.parse("image/png"),file));
            }

        }

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody.build())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                str[0] = response.body().toString();
            }
        });
        return str.toString();
    }


    public static int getCode(String jsonStr) throws JSONException {
        int code = 0;
        if (jsonStr == null) {
            code = 0;
        } else {
            JSONObject jsonObject = new JSONObject(jsonStr);
            code = jsonObject.getInt("code");
        }
        return code;
    }

    public static InputStream getStreamFromURL(String imageURL) {
        InputStream in = null;
        try {
            java.net.URL url = new java.net.URL(imageURL);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            in = connection.getInputStream();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return in;

    }




    // 把一个url的网络图片变成一个本地的BitMap
    public static Bitmap getHttpBitmap(String url) {
        java.net.URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new java.net.URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;


    }

   /* public static ArrayList<YouTuTag> getTags(JSONObject jsonObject) {
        ArrayList<YouTuTag> tags = new ArrayList<>();
        YouTuTag youTuTag;
        if (jsonObject == null) {
            youTuTag = new YouTuTag();
            youTuTag.setTagName("error");
            youTuTag.setTagConfidence(0);
            tags.add(youTuTag);
        } else {
            try {
                if (jsonObject.getInt("errorcode") == 0
                        && jsonObject.getString("errormsg").equals("OK")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("tags");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        youTuTag = new YouTuTag();
                        youTuTag.setTagName(jsonArray.getJSONObject(i).getString("tag_name"));
                        youTuTag.setTagConfidence(jsonArray.getJSONObject(i).getInt("tag_confidence"));
                        tags.add( youTuTag);

                    }
                }
                else {
                    youTuTag = new YouTuTag();
                    youTuTag.setTagName("error");
                    youTuTag.setTagConfidence(0);
                    tags.add(youTuTag);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tags;
    }*/

    /**
     * 解析
     *
     * @throws JSONException
     */
    public static HashMap<String, Object> AnalysisUid(
            String jsonStr) throws JSONException {

        if (getCode(jsonStr) == 100) {
            JSONObject jsonObject = new JSONObject(jsonStr);
            int uid = jsonObject.getInt("obj");
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("userid", uid);
            return hm;
        } else {
            return null;
        }

    }
    /**
     * 解析
     *
     * @throws JSONException
     */
    public static HashMap<String, Object> AnalysisUserInfo(
            String jsonStr) throws JSONException {

        if (getCode(jsonStr) == 100) {

            JSONObject jsonObject = new JSONObject(jsonStr);
            String objStr = jsonObject.getString("obj");
            JSONObject obj = new JSONObject(objStr);
            HashMap<String, Object> hm = new HashMap<>();

            hm.put("userid", obj.getInt("userId"));
            hm.put("username", obj
                    .getString("userName"));
            hm.put("userphone", obj
                    .getString("userPhone"));
            hm.put("userpb", obj
                    .getInt("userPb"));
            hm.put("taskpic_url", obj
                    .getString("userPicUrl"));
            hm.put("password", obj
                    .getString("passWord"));
            return hm;
        } else {
            return null;
        }

    }

    /**
     * 解析
     *
     * @throws JSONException
     */
    public static HashMap<String, Object> AnalysisSinglePos(
            String jsonStr) throws JSONException {

        if (getCode(jsonStr) == 100) {

            JSONObject jsonObject = new JSONObject(jsonStr);
            String objStr = jsonObject.getString("obj");
            JSONObject obj = new JSONObject(objStr);
            HashMap<String, Object> hm = new HashMap<>();

            hm.put("posid", obj
                    .getInt("postId"));
            hm.put("typeid",obj
                    .getInt("typeId"));
            hm.put("tags",obj
                    .getString("tags"));
            hm.put("userid",obj
                    .getInt("userId"));
            hm.put("pospb", obj
                    .getInt("posePb"));
            hm.put("posname", obj
                    .getString("poseName"));
            hm.put("pos_pic_url", obj
                    .getString("posPicUrl"));
            hm.put("poscontent", obj
                    .getString("posContent"));

            return hm;
        } else {
            return null;
        }

    }


    /**
     * 解析
     *
     * @throws JSONException
     */
    public static  ArrayList<HashMap<String, Object>> AnalysisUserInfo2(
            String jsonStr) throws JSONException {
        ArrayList<JSONObject> jsonArrayList = TOJsonArray(jsonStr);
        if (jsonArrayList == null) {
            return null;
        } else {

            ArrayList<HashMap<String, Object>> pArrayList = new ArrayList<>();

            for (int i = 0; i < jsonArrayList.size(); i++) {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("userid", jsonArrayList.get(i)
                        .getInt("userId"));
                hm.put("username",jsonArrayList.get(i)
                        .getString("userName"));
                hm.put("userphone",jsonArrayList.get(i)
                        .getString("userPhone"));
                hm.put("userpb",jsonArrayList.get(i)
                        .getInt("userPb"));
                hm.put("taskpic_url", jsonArrayList.get(i)
                        .getString("userPicUrl"));
                hm.put("password", jsonArrayList.get(i)
                        .getString("passWord"));
                pArrayList.add(hm);

            }
            return pArrayList;


        }


    }

    /**
     * 解析
     *
     * @throws JSONException
     */
    public static ArrayList<HashMap<String, Object>> AnalysisPosInfo(
            String jsonStr ) throws JSONException {
        ArrayList<JSONObject> jsonArrayList = TOJsonArray(jsonStr);
        if (jsonArrayList == null) {
            return null;
        } else {

            ArrayList<HashMap<String, Object>> pArrayList = new ArrayList<>();

            for (int i = 0; i < jsonArrayList.size(); i++) {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("posid", jsonArrayList.get(i)
                        .getInt("postId"));
                hm.put("typeid",jsonArrayList.get(i)
                        .getInt("typeId"));
                hm.put("tags",jsonArrayList.get(i)
                        .getString("tags"));
                hm.put("pospb",jsonArrayList.get(i)
                        .getInt("posePb"));
                hm.put("posname", jsonArrayList.get(i)
                        .getString("poseName"));
                hm.put("pos_pic_url", jsonArrayList.get(i)
                        .getString("posPicUrl"));
                hm.put("poscontent", jsonArrayList.get(i)
                        .getString("posContent"));
                hm.put("userid",jsonArrayList.get(i)
                        .getInt("userId"));
                pArrayList.add(hm);

            }
            return pArrayList;


        }
    }

    /**
     * 解析
     *
     * @throws JSONException
     */
    public static ArrayList<HashMap<String, Object>> AnalysisTagInfo(
            String jsonStr ) throws JSONException {
        ArrayList<JSONObject> jsonArrayList = TOJsonArray(jsonStr);
        if (jsonArrayList == null) {
            return null;
        } else {

            ArrayList<HashMap<String, Object>> pArrayList = new ArrayList<>();

            for (int i = 0; i < jsonArrayList.size(); i++) {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("tagid", jsonArrayList.get(i)
                        .getInt("tagId"));
                hm.put("tag",jsonArrayList.get(i)
                        .getString("tag"));
                pArrayList.add(hm);

            }
            return pArrayList;


        }
    }



    /**
     * 解析
     *
     * @throws JSONException
     */
    public static ArrayList<HashMap<String, Object>> AnalysisPosInfo2(
            String jsonStr ) throws JSONException {
        ArrayList<JSONObject> jsonArrayList = TOJsonArray2(jsonStr);
        if (jsonArrayList == null) {
            return null;
        } else {
            ArrayList<HashMap<String, Object>> pArrayList = new ArrayList<>();
            for (int i = 0; i < jsonArrayList.size(); i++) {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("posid", jsonArrayList.get(i)
                        .getInt("postId"));
                hm.put("typeid",jsonArrayList.get(i)
                        .getInt("typeId"));
                hm.put("tags",jsonArrayList.get(i)
                        .getString("tags"));
                hm.put("pospb",jsonArrayList.get(i)
                        .getInt("posePb"));
                hm.put("posname", jsonArrayList.get(i)
                        .getString("poseName"));
                hm.put("pos_pic_url", jsonArrayList.get(i)
                        .getString("posPicUrl"));
                hm.put("poscontent", jsonArrayList.get(i)
                        .getString("posContent"));
                hm.put("userid",jsonArrayList.get(i)
                        .getInt("userId"));
                pArrayList.add(hm);

            }
            return pArrayList;


        }
    }



    /**
     * 解析
     *
     * @throws JSONException
     */
    public static ArrayList<JSONObject> TOJsonArray(
            String jsonStr) throws JSONException {
        if (jsonStr == null) {
            return null;
        } else {
            JSONObject jsonObject = new JSONObject(jsonStr);
            if (jsonObject.getInt("code") == 100) {
                JSONArray jsonArray = jsonObject.getJSONArray("obj");

                ArrayList<JSONObject> pArrayList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    pArrayList.add(jsonArray.getJSONObject(i));
                }

                return pArrayList;

            } else
                return null;
        }
    }

    /**
     * 解析
     *
     * @throws JSONException
     */
    public static ArrayList<JSONObject> TOJsonArray2(
            String jsonStr) throws JSONException {
        if (jsonStr == null) {
            return null;
        } else {
            JSONArray jsonArray = new JSONArray(jsonStr);
            ArrayList<JSONObject> pArrayList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                pArrayList.add(jsonArray.getJSONObject(i));
            }
            return pArrayList;
        }
    }


}

