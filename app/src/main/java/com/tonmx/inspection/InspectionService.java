package com.tonmx.inspection;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.GpsSatellite;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tonmx.inspection.ui.login.LoginActivity;
import com.tonmx.inspection.ui.login.LoginItem;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;

public class InspectionService extends Service {

    private FormNameDBHelper mfomDB = null;
    private ReviewNameDBHelper reviewNameDBHelper = null;
    private FormTemplateDBHelper mtemDB = null;
    private ReviewFormDBHelper mreDB = null;
    private boolean locking =false;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver();
        initOkHttpClient();
        initHandler();
        acquireWakeLock();
        mfomDB = new FormNameDBHelper(this);
        reviewNameDBHelper = new ReviewNameDBHelper(this);
        mtemDB = new FormTemplateDBHelper(this);
        mreDB = new ReviewFormDBHelper(this);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver();
        releaseWakeLock();
        super.onDestroy();

    }

    private PowerManager.WakeLock mWakeLock;
    protected OkHttpClient mOkHttpClient;
    private Handler mainHandler;

    private void initOkHttpClient() {
        File sdcache = getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
        mOkHttpClient = builder.build();
    }

    private void initHandler() {
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public void acquireWakeLock() {
        if (mWakeLock == null) {
            mWakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK, InspectionService.class.getName());
            mWakeLock.setReferenceCounted(false);
        }

        if (mWakeLock != null && !mWakeLock.isHeld()) {
            mWakeLock.acquire();
        }
    }

    public void releaseWakeLock() {
        if (mWakeLock != null && mWakeLock.isHeld()) {
            mWakeLock.release();
        }
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.tonmx.inspection.login");
        intentFilter.addAction("com.tonmx.inspection.revisePwd");
        intentFilter.addAction("com.tonmx.inspection.formtemplete");
        intentFilter.addAction("com.tonmx.inspection.reviewInspection");
        intentFilter.addAction("com.tonmx.inspection.reviewConfirm");
        intentFilter.addAction("com.tonmx.inspection.reviewReturn");
        intentFilter.addAction("com.tonmx.inspection.submit");
        intentFilter.addAction("com.tonmx.inspection.resubmit");
        intentFilter.addAction("com.tonmx.inspection.refreshUserInformation");
        intentFilter.addAction("com.tonmx.inspection.refreshUserInformationOnly");
        registerReceiver(mbroadcastreceiver, intentFilter);
    }

    private void unregisterReceiver() {
        unregisterReceiver(mbroadcastreceiver);
    }

    private BroadcastReceiver mbroadcastreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //Bundle bundle = intent.getExtras();
            if("com.tonmx.inspection.login".equalsIgnoreCase(action)){

                ////连接平台，回调，跳转界面
                String name = intent.getStringExtra("username");
                String pwd = intent.getStringExtra("password");
                String ip = intent.getStringExtra("ip");
                sendLoginPost(name,pwd,ip);

            } else if("com.tonmx.inspection.revisePwd".equalsIgnoreCase(action)){
                //修改密码接口
                String pwd = intent.getStringExtra("password");

                sendRevisePwdPost(pwd);
            } else if ("com.tonmx.inspection.formtemplete".equalsIgnoreCase(action)){
                //获取表单模板
                int sheetid = intent.getIntExtra("sheetid",-1);
                String sheetname = intent.getStringExtra("sheetname");
                int sheetlogid = intent.getIntExtra("sheetlogid",-1);
                int state = intent.getIntExtra("state",-1);
                if(sheetid<0){

                } else {
                    sendTempletePost(sheetid,sheetname,sheetlogid,state);
                }
            } else if ("com.tonmx.inspection.reviewInspection".equalsIgnoreCase(action)){
                int sheetid = intent.getIntExtra("sheetId",-1);
                int sheetlogid = intent.getIntExtra("sheetlogid",-1);
                sendReviewInspectionPost(sheetlogid,sheetid);

            } else if ("com.tonmx.inspection.reviewConfirm".equalsIgnoreCase(action)){
                //int sheetid = intent.getIntExtra("sheetId",-1);
                int sheetlogid = intent.getIntExtra("sheetlogid",-1);
                Log.i("BYZ","confirm 1:"+sheetlogid);
                sendReviewConfirmPost(sheetlogid);

            } else if ("com.tonmx.inspection.reviewReturn".equalsIgnoreCase(action)){
                //int sheetid = intent.getIntExtra("sheetId",-1);
                int sheetlogid = intent.getIntExtra("sheetlogid",-1);
                sendReviewReturnPost(sheetlogid);

            } else if ("com.tonmx.inspection.submit".equalsIgnoreCase(action)){
                int sheetid = intent.getIntExtra("sheetId",-1);
                //String reviewer = intent.getStringExtra("reviewer");
                String sheetname = intent.getStringExtra("sheetname");
                sendSubmitPost(sheetid,sheetname);
            } else if ("com.tonmx.inspection.resubmit".equalsIgnoreCase(action)){
                int sheetid = intent.getIntExtra("sheetId",-1);
                //String reviewer = intent.getStringExtra("reviewer");
                String sheetname = intent.getStringExtra("sheetname");
                String reviewer = intent.getStringExtra("reviewer");
                int sheetlogid = intent.getIntExtra("sheetlogid",-1);
                sendReSubmitPost(sheetid,sheetname,sheetlogid,reviewer);
            }else if ("com.tonmx.inspection.refreshUserInformation".equalsIgnoreCase(action)){
                sendFormGetPost();
            } else if ("com.tonmx.inspection.refreshUserInformationOnly".equalsIgnoreCase(action)){
                sendFormGetPostOnly();
            }
        }
    };

    public void sendLoginPost(final String name, final String pwd,final String ip) {//登录接口

        String url = "http://" + ip + ":" + "8081" + "/api/tmx/loginAPP";
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", name)
                .addFormDataPart("password",pwd);
        Log.i("BYZ","Login ip:"+ip);
        Log.i("BYZ","Login usernme:"+name);
        Log.i("BYZ","Login pwd:"+pwd);
        RequestBody requestBody = requestBodyBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                showMsg("登录失败，请检查网络！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.i("BYZ","Login body:"+body);
                JsonElement root = new JsonParser().parse(body);
                JsonObject parent = root.getAsJsonObject();
                if ("SUCCESS".equals(parent.get("result").getAsString())){
                    JsonObject data = parent.get("data").getAsJsonObject();
                    int userid = data.get("id").getAsInt();
                    //LoginItem item=new LoginItem();
                    String fullname = data.get("fullName").getAsString();
                    String departmentName = data.get("departmentName").getAsString();
                    String token = data.get("token").getAsString();
                    PrefData.setHisName(name);
                    PrefData.setHisPwd(pwd);
                    PrefData.setHisIP(ip);
                    PrefData.setUserId(userid);
                    PrefData.setFullName(fullname);
                    PrefData.setDepartName(departmentName);
                    PrefData.setToken(token);
                    PrefData.setLoginStatus(1);//登录状态置1
                    sendFormGetPost();//获取表单列表




                } else {
                    showMsg("用户名或密码错误");
                }

            }
        });
    }


    public void sendRevisePwdPost(final String pwd) {//修改密码接口
        String url = "http://" + PrefData.getHisIP() + ":" + "8081" + "/api/tmx/changePassword";
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id",String.valueOf(PrefData.getUserId()))
                .addFormDataPart("password",PrefData.getHisPwd())
                .addFormDataPart("newPassword",pwd);
        RequestBody requestBody = requestBodyBuilder.build();
        Request request = new Request.Builder()
                .url( url)
                .addHeader("token",PrefData.getToken())
                .post(requestBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                showMsg("请检查网络!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.i("BYZ","RevisePwd body:"+body);
                JsonElement root = new JsonParser().parse(body);
                JsonObject parent = root.getAsJsonObject();
                if ("SUCCESS".equals(parent.get("result").getAsString())){
                    JsonObject data = parent.get("data").getAsJsonObject();
                    PrefData.setHisPwd(pwd);
                    startLoginActivity(getApplicationContext());
                } else {
                    showMsg("修改密码失败");
                }

            }
        });
    }

    public void sendReviewerGetPost() { //获取复核人接口

        String url = "http://" + PrefData.getHisIP() + ":" + "8081" + "/api/tmx/getReviewPeople";
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id",String.valueOf(PrefData.getUserId()));
        RequestBody requestBody = requestBodyBuilder.build();
        Request request = new Request.Builder()
                .url( url)
                .addHeader("token",PrefData.getToken())
                .post(requestBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.i("ASDF","body:"+body);
                JsonElement root = new JsonParser().parse(body);
                JsonObject parent = root.getAsJsonObject();
                ReviewerBean reviewerBean = GsonUtil.getInstance().getGson().fromJson(parent,ReviewerBean.class);
                if ("SUCCESS".equals(parent.get("result").getAsString())){
                    List<ReviewerItem> reviewerItems = reviewerBean.getData();
                    List<ReviewerItem> satelliteList = new ArrayList<ReviewerItem>();
                    if(reviewerItems!=null){
                        satelliteList.addAll(reviewerItems);
                        //startUserInformationActivity(getApplication());
                    }

                    PrefData.setReviewerInspection(satelliteList);


                } else {
                    //showMsg("");
                }

            }
        });
    }


    public void sendFormGetPost() { //获取所有表单接口

        String url = "http://" + PrefData.getHisIP() + ":" + "8081" + "/api/tmx/querySheetApp";
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("userId",String.valueOf(PrefData.getUserId()));
        RequestBody requestBody = requestBodyBuilder.build();
        Request request = new Request.Builder()
                .url( url)
                .addHeader("token",PrefData.getToken())
                .post(requestBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                showMsg("获取表单失败，请检查网络!");
                locking =false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                locking =false;
                String body = response.body().string();
                //Log.i("BYZ","sendFormGetPost body:"+body);
                JsonElement root = new JsonParser().parse(body);
                JsonObject parent = root.getAsJsonObject();
                FormBean formBean = GsonUtil.getInstance().getGson().fromJson(parent,FormBean.class);
                if ("SUCCESS".equals(parent.get("result").getAsString())){
                    List<Hand> formhandList = formBean.getData().getHand();
                    List<Todo> formtodoList = formBean.getData().getTodo();

                    //删除数据库中所有复核表单
                    reviewNameDBHelper.deleteReview();
                    //存在未开始的的表格则删除再插入
                    mfomDB.deleteNormal();
                   // }
                    if(formtodoList!=null){
                        for(Todo todo : formtodoList){
                            //直接插入待处理数据
                            reviewNameDBHelper.insertTodoForm(todo);
                        }

                    }

                        for (Hand hand : formhandList) {
                            boolean exist = mfomDB.DataExist(hand);
                            int state = mfomDB.querystate(hand.getId());

                            if(hand.getState()==2){//待处理，不做操作
                                hand.setState(4);
                                if(exist){//本地存在的话，更新state为待复核
                                    mfomDB.updateSheetLogId(PrefData.getUserId(),hand);
                                }else {//不存在的话，插入一条待复核数据
                                    mfomDB.insertHandForm(hand);
                                }

                            } else if(hand.getState()==3){//已驳回
                                if(exist){//多台设备登录，先查询本地是否存在表单，没有则插入,有就更新state和sheetlogid
                                    mfomDB.updateSheetLogId(PrefData.getUserId(),hand);
                                } else {
                                    mfomDB.insertHandForm(hand);
                                }
                            } else if(hand.getState()== 0 ){//普通表格或通过表单
                                if(exist){//本地存在
                                    if(state ==0){
                                        //不可能出现，上面已经删除所有崭新表单了
                                       // mfomDB.insertHandForm(hand);
                                        mfomDB.updateSheetLogId(PrefData.getUserId(),hand);
                                    } else if(state ==1){
                                        //更新sheetlogid
                                        hand.setState(1);
                                        mfomDB.updateSheetLogId(PrefData.getUserId(),hand);
                                    } else if(state == 3){
                                        //驳回后，其他设备二次提交且审核通过的情况
                                        mfomDB.deleteReject();//平台通过，删除本地驳回表单
                                        hand.setState(0);
                                        mfomDB.insertHandForm(hand);//重新插入新表
                                    } else if(state == 4){//本地待复核
                                        mfomDB.deletePass();//平台通过，删除本地待复核表单
                                        hand.setState(0);
                                        mfomDB.insertHandForm(hand);//重新插入新表
                                    }

                                }else {//本地不存在
                                    hand.setState(0);
                                    mfomDB.insertHandForm(hand);
                                }
                            }

                        }
                        sendReviewerGetPost();
                        startUserInformationActivity(getApplication());



                } else {
                    //showMsg("");
                }

            }
        });
    }

    public void sendFormGetPostOnly() { //获取所有表单接口

        String url = "http://" + PrefData.getHisIP() + ":" + "8081" + "/api/tmx/querySheetApp";
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("userId",String.valueOf(PrefData.getUserId()));
        RequestBody requestBody = requestBodyBuilder.build();
        Request request = new Request.Builder()
                .url( url)
                .addHeader("token",PrefData.getToken())
                .post(requestBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                showMsg("获取表单失败，请检查网络!");
                locking =false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                locking =false;
                String body = response.body().string();
                Log.i("BYZ","sendFormGetPost body:"+body);
                JsonElement root = new JsonParser().parse(body);
                JsonObject parent = root.getAsJsonObject();
                FormBean formBean = GsonUtil.getInstance().getGson().fromJson(parent,FormBean.class);
                if ("SUCCESS".equals(parent.get("result").getAsString())){
                    List<Hand> formhandList = formBean.getData().getHand();
                    List<Todo> formtodoList = formBean.getData().getTodo();
                    //删除数据库中所有复核表单
                    reviewNameDBHelper.deleteReview();
                    //存在未开始的的表格则删除再插入
                    mfomDB.deleteNormal();
                    // }
                    if(formtodoList!=null){
                        for(Todo todo : formtodoList){
                            //直接插入待处理数据
                            reviewNameDBHelper.insertTodoForm(todo);
                        }

                    }
                    if(formhandList!=null){
                        for (Hand hand : formhandList) {
                            boolean exist = mfomDB.DataExist(hand);
                            Log.i("VVV","exist:"+exist);
                            int state = mfomDB.querystate(hand.getId());
                            Log.i("VVV","state:"+state);

                            if(hand.getState()==2){//待处理，不做操作
                                hand.setState(4);
                                if(exist){//本地存在的话，更新state为待复核
                                    mfomDB.updateSheetLogId(PrefData.getUserId(),hand);
                                }else {//不存在的话，插入一条待复核数据
                                    mfomDB.insertHandForm(hand);
                                }

                            } else if(hand.getState()==3){//已驳回
                                if(exist){//多台设备登录，先查询本地是否存在表单，没有则插入,有就更新state和sheetlogid
                                    mfomDB.updateSheetLogId(PrefData.getUserId(),hand);
                                } else {
                                    mfomDB.insertHandForm(hand);
                                }
                            } else if(hand.getState()== 0 ){//普通表格或通过表单
                                if(exist){//本地存在
                                    if(state ==0){
                                        //mfomDB.insertHandForm(hand);
                                            //不可能出现，上面已经删除所有崭新表单了
                                    } else if(state ==1){
                                            //更新sheetlogid
                                        hand.setState(1);
                                        mfomDB.updateSheetLogId(PrefData.getUserId(),hand);
                                    } else if(state == 3){
                                        //驳回后，其他设备二次提交且审核通过的情况
                                        mfomDB.deleteReject();//平台通过，删除本地驳回表单
                                        hand.setState(0);
                                        mfomDB.insertHandForm(hand);//重新插入新表
                                    } else if(state == 4){//本地待复核
                                        mfomDB.deletePass();//平台通过，删除本地待复核表单
                                        hand.setState(0);
                                        mfomDB.insertHandForm(hand);//重新插入新表
                                    }

                                }else {//本地不存在
                                    hand.setState(0);
                                    mfomDB.insertHandForm(hand);
                                }
                            }

                        }

//                        for (Hand hand : formhandList) {
//                            Log.i("BYZ","hand:"+hand.getState());
//                            boolean exist = mfomDB.DataExist(hand);
//                            int state = mfomDB.querystate();
//                            Log.i("VVV","exist:"+exist);
//                            Log.i("VVV","state:"+state);
//                            if(exist){
//                                // if()
//                            }
//
//                            if(hand.getState()==2){//待处理，不做操作
//
//                            } else if(hand.getState()==3){//已驳回
////                                if(exist){//多台设备登录，先查询本地是否存在表单，没有则插入,有就更新state
////                                    mfomDB.updateSheetLogId(PrefData.getUserId(),hand);
////                                    showMsg("本地没有记录，插入数据");
////                                } else {
////                                    mfomDB.insertHandForm(hand);
////                                }
//
//                                mfomDB.updateSheetLogId(PrefData.getUserId(),hand);
//
//                            } else if(hand.getState()== 0 ){//普通表格或通过表单
//                                mfomDB.deletePass();//平台通过，删除本地待复核表单
//                                int sheetid = hand.getId();
//                                LinkedList<Hand>  linkedList =mfomDB.readDoingList(sheetid);
//                                if(linkedList != null && linkedList.size() > 0){
//
//                                } else {
//                                    hand.setState(0);
//                                    mfomDB.insertHandForm(hand);
//                                }
//                            }
//
//                        }
                    }

                    showMsg("已刷新");
                } else {
                    //showMsg("");
                }

            }
        });
    }

    public void sendTempletePost(final int sheetid,final String sheetname,final  int sheetlogid,final  int state) {//获取表单模板接口
        String url = "http://" + PrefData.getHisIP() + ":" + "8081" + "/api/tmx/findTemplateTable";
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("sheetId",String.valueOf(sheetid));
        RequestBody requestBody = requestBodyBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("token",PrefData.getToken())
                .post(requestBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                showMsg("获取表单失败，请检查网络!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                LogUtil.e("BYZ","sendTempletePost:"+body);
                JsonElement root = new JsonParser().parse(body);
                JsonObject parent = root.getAsJsonObject();
                FormTempleteBean templeteBean = GsonUtil.getInstance().getGson().fromJson(parent,FormTempleteBean.class);
                if ("SUCCESS".equals(parent.get("result").getAsString())){
                    //JsonObject data = parent.get("data").getAsJsonObject();
                    List<FormTempleteItem> templeteItemList = templeteBean.getData();
                    //String token = data.get("token").getAsString();
//                    if(token!=null && token.length()>0){
//                        PrefData.setToken(token);
//                    }
                    Log.i("BYZ","onResponse:"+body);
                    Log.i("BYZ","onResponse  tostring:"+templeteItemList.toString());
                    Log.i("BYZ","onResponse size:"+templeteItemList.size());
                    mtemDB.deleteNormal(sheetid);
                    //int count =1;
                    if(templeteItemList.size()>0){
                        for (FormTempleteItem templeteItem : templeteItemList) {
                            //初始化值
                            //count++;
                            templeteItem.setUserid(PrefData.getUserId());

                            templeteItem.setRemarks("");
                            if(templeteItem.getType()==1){
                                templeteItem.setStatus("正常");
                                templeteItem.setClick(1);
                            }else {
                                templeteItem.setStatus("");
                                templeteItem.setClick(0);
                            }

                            mtemDB.insertForm(templeteItem);

                        }

                    }
                    startFormTempActivity(getApplication(),sheetid,sheetname,sheetlogid,state);


                } else {
                    //showMsg("");
                }

            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_REDELIVER_INTENT;
    }

    public void sendReviewInspectionPost(final int sheetlogid, final int sheetid){//获取已审核表单接口
        String url = "http://" + PrefData.getHisIP() + ":" + "8081" + "/api/tmx/seeSheetLog";
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("sheetLogId",String.valueOf(sheetlogid));
        RequestBody requestBody = requestBodyBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("token",PrefData.getToken())
                .post(requestBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        Log.i("XXXB","sheetLogId:"+sheetlogid);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                showMsg("获取审核表单失败，请检查网络!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                JsonElement root = new JsonParser().parse(body);
                JsonObject parent = root.getAsJsonObject();
                ReviewTempleteBean reviewTempleteBean = GsonUtil.getInstance().getGson().fromJson(parent,ReviewTempleteBean.class);
                if ("SUCCESS".equals(parent.get("result").getAsString())){
                    Log.i("XXXB","onRespnse"+body);
                    List<ReviewTempleteItem> templeteItemList = reviewTempleteBean.getItem();
                    mreDB.deleteReviewForm(sheetlogid);
                    if(templeteItemList!=null){
                        for (ReviewTempleteItem templeteItem : templeteItemList) {
                            //初始化值
                           if(templeteItem.getRemarks().equalsIgnoreCase("-1")){
                               templeteItem.setRemarks("无");
                           }
                            mreDB.insertForm(templeteItem,sheetlogid);

                        }

                    }
                    startReviewTempActivity(getApplicationContext(),sheetlogid,sheetid);

                    //跳转至回放界面


                } else {
                    //showMsg("");
                }

            }
        });
    }


    public void sendReviewConfirmPost(final int sheetid){//确认巡视表接口
        Log.i("BYZ","confirm 1");
        String url = "http://" + PrefData.getHisIP() + ":" + "8081" + "/api/tmx/confirmSheetLog";
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("sheetLogId",String.valueOf(sheetid));
        RequestBody requestBody = requestBodyBuilder.build();
        Request request = new Request.Builder()
                .url( url)
                .addHeader("token",PrefData.getToken())
                .post(requestBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        Log.i("BYZ","confirm 2:"+sheetid);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                showMsg("获取表单失败，请检查网络!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.i("BYZ","confirm 3:"+body);
                JsonElement root = new JsonParser().parse(body);
                JsonObject parent = root.getAsJsonObject();
                if ("SUCCESS".equals(parent.get("result").getAsString())){
                    //跳转用户界面
                    sendFormGetPost();
                } else {
                    //showMsg("");
                }

            }
        });
    }


    public void sendReviewReturnPost(final int sheetid){//退会巡视表接口
        Log.i("BYZ","return 1");
        String url = "http://" + PrefData.getHisIP() + ":" + "8081" + "/api/tmx/returnSheetLog";
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("sheetLogId",String.valueOf(sheetid));
        RequestBody requestBody = requestBodyBuilder.build();
        Request request = new Request.Builder()
                .url( url)
                .addHeader("token",PrefData.getToken())
                .post(requestBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        Log.i("BYZ","return 2");
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                showMsg("获取表单失败，请检查网络!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.i("BYZ","return 3"+body);
                JsonElement root = new JsonParser().parse(body);
                JsonObject parent = root.getAsJsonObject();
                if ("SUCCESS".equals(parent.get("result").getAsString())){
                    //跳转用户界面
                    sendFormGetPost();
                } else {
                    //showMsg("");
                }

            }
        });
    }

    public void sendSubmitPost(final int sheetid,final  String sheetname){//提交表单接口
        Log.i("BYZ","  do not sendSubmitPost ");
        String url = "http://" + PrefData.getHisIP() + ":" + "8081" + "/api/tmx/saveSheetLog";
        LinkedList<FormTempleteItem>  formTempleteItems = mtemDB.readALL(sheetid);
        ReviewerPostBean reviewerPostBean = new ReviewerPostBean();
        reviewerPostBean.setId(sheetid);
        reviewerPostBean.setExecuteState("异常");
        reviewerPostBean.setInspector(PrefData.getFullName());
        reviewerPostBean.setSheetName(sheetname);
        reviewerPostBean.setReviewer(PrefData.getReviewerSelectName());
        reviewerPostBean.setRid(PrefData.getReviewerSelectId());
        reviewerPostBean.setUserId(PrefData.getUserId());
        reviewerPostBean.setTemplateTableList(formTempleteItems);
//
        Gson gson = new Gson();
        //使用Gson将对象转换为json字符串
        String para = gson.toJson(reviewerPostBean);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON,para);
        Request request = new Request.Builder()
                .url( url)
                .addHeader("token",PrefData.getToken())
                .post(requestBody)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                showMsg("提交表单，请检查网络!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.i("POIU","submit body:"+body);
                JsonElement root = new JsonParser().parse(body);
                JsonObject parent = root.getAsJsonObject();
                if ("SUCCESS".equals(parent.get("result").getAsString())){
                    //将表单值state改为4//待复核
                    Hand item = new Hand();
                    item.setState(4);
                    item.setId(sheetid);
                    mfomDB.updateState(PrefData.getUserId(),item);
                    //mfomDB.updateState(PrefData.getUserId(),item);
                    sendFormGetPost();
                } else {
                    //将表单值state改为1//进行中
//                    if()
//                    Hand item = new Hand();
//                    item.setState(1);
//                    item.setId(sheetid);
//                    mfomDB.updateState(PrefData.getUserId(),item);
                    showMsg("提交表单失败");
                }

            }
        });
    }

    public void sendReSubmitPost(final int sheetid,final  String sheetname,final int sheetlogid,final String reviewer){//二次提交表单接口
        String url = "http://" + PrefData.getHisIP() + ":" + "8081" + "/api/tmx/resetSheetLog";
        LinkedList<FormTempleteItem>  formTempleteItems = mtemDB.readALL(sheetid);
        ReviewerPostBean reviewerPostBean = new ReviewerPostBean();
        reviewerPostBean.setId(sheetid);
        reviewerPostBean.setSheetLogId(sheetlogid);
        Log.i("POIU","setSheetLogId:"+sheetlogid);
        reviewerPostBean.setExecuteState("异常");
        reviewerPostBean.setInspector(PrefData.getFullName());
        reviewerPostBean.setSheetName(sheetname);
        reviewerPostBean.setReviewer(reviewer);
        reviewerPostBean.setRid(PrefData.getReviewerSelectId());
        reviewerPostBean.setUserId(PrefData.getUserId());
        reviewerPostBean.setTemplateTableList(formTempleteItems);
        Log.i("BYZ","sendReSubmitPost reviewer:"+reviewer);
        Gson gson = new Gson();
        //使用Gson将对象转换为json字符串
        String para = gson.toJson(reviewerPostBean);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON,para);
        Request request = new Request.Builder()
                .url( url)
                .addHeader("token",PrefData.getToken())
                .post(requestBody)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                showMsg("提交表单失败，请检查网络!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.i("POIU","submit body:"+body);
                JsonElement root = new JsonParser().parse(body);
                JsonObject parent = root.getAsJsonObject();
                if ("SUCCESS".equals(parent.get("result").getAsString())){
                    //将表单值state改为4//待复核
                    Hand item = new Hand();
                    item.setState(4);
                    item.setId(sheetid);
                    mfomDB.updateState(PrefData.getUserId(),item);
                    sendFormGetPost();
                } else {
                    showMsg("提交表单失败");
                }

            }
        });
    }



    public void startUserInformationActivity(Context context){
        Intent intent = new Intent(context, UserInformation.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void startLoginActivity(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void startFormTempActivity(Context context,int sheetid,String sheetname,int sheetlogid,int state){
        Intent intent = new Intent(context, OwnerInformationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("sheetid",sheetid);
        intent.putExtra("sheetname",sheetname);
        intent.putExtra("sheetlogid",sheetlogid);
        intent.putExtra("state",state);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void startReviewTempActivity(Context context,int sheetlogid,int sheetid){
        Intent intent = new Intent(context, ReviewInspectionActivity.class);
        intent.putExtra("sheetlogid",sheetlogid);
        intent.putExtra("sheetid",sheetid);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void showMsg(final String txt) {
        if (txt == null) {
            return;
        }
        showMsg(txt, 0);
    }

    public void showMsg(final String txt, final long delay) {
        if (txt == null) {
            return;
        }
        try {
            if (mainHandler != null) {
                mainHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShortToast(InspectionService.this, txt);

                    }
                }, delay);
            }
        } catch (RuntimeException e) {

        }

    }
}
