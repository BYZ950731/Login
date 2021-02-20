package com.tonmx.inspection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.tonmx.inspection.ui.login.LoginActivity;

import java.util.LinkedList;

public class UserInformation extends Activity implements View.OnClickListener{

    private TextView username,departname;
    private ImageView user_menu ,refresh;
    private ListView mOwnListView,mReviewListView = null;
    private static final int REVICE_PASSWORD = 0;
    private static final int LOG_OUT = 1;
    private UserMenu mMenu;
    private Context mContext;
    private FormNameDBHelper mDB = null;
    private ReviewNameDBHelper reviewNameDBHelper = null;
    private FormTemplateDBHelper formTemplateDBHelper = null;
    private InspectionDialog mDialog = null;
    private static final int MSG_REFRESH_VIEW = 1;
    private OwnerListAdapter ownerListAdapter;
    private ReviewListAdapter reviewListAdapter;
    private LinkedList<Hand> mOwnItemList = new LinkedList<>();
    private LinkedList<Todo> mReviewItemList = new LinkedList<>();
    private void refreshList() {
        Log.i("BYZ","refreshList");
        if (mOwnItemList != null && mOwnItemList.size() > 0) {
            mOwnItemList.clear();
        }

        if (mReviewItemList != null && mReviewItemList.size() > 0) {
            mReviewItemList.clear();
        }
        String resource=PrefData.getHisName();
        if(!TextUtils.isEmpty(String.valueOf(resource))){
            mOwnItemList = mDB.readOwnList();
            mReviewItemList = reviewNameDBHelper.readReviewList();
            if (mOwnItemList != null && mOwnItemList.size() > 0) {
                mOwnListView.setVisibility(View.VISIBLE);
                ownerListAdapter.setList(mOwnItemList);
                ownerListAdapter.notifyDataSetChanged();
            } else {
                mOwnListView.setVisibility(View.GONE);
                ownerListAdapter.notifyDataSetChanged();
            }

            if (mReviewItemList != null && mReviewItemList.size() > 0) {
                mReviewListView.setVisibility(View.VISIBLE);
                reviewListAdapter.setList(mReviewItemList);
                reviewListAdapter.notifyDataSetChanged();
            } else {
                mReviewListView.setVisibility(View.GONE);
                reviewListAdapter.notifyDataSetChanged();
            }
        } else {
            //mOwnListView.setEmptyView();
            mOwnListView.setVisibility(View.GONE);
            mReviewListView.setVisibility(View.GONE);
            ownerListAdapter.notifyDataSetChanged();
            reviewListAdapter.notifyDataSetChanged();
        }
    }


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MSG_REFRESH_VIEW:
                    //刷新列表
                    refreshList();
                    break;
                default:
                    break;
        }
            return false;
    }});

    private void initData() {
        mDB = new FormNameDBHelper(this);
        reviewNameDBHelper = new ReviewNameDBHelper(this);
        formTemplateDBHelper = new FormTemplateDBHelper(this);
        ownerListAdapter = new OwnerListAdapter(this);
        reviewListAdapter = new ReviewListAdapter(this);
        mOwnListView.setAdapter(ownerListAdapter);
        mOwnListView.setOnItemClickListener(new OwnItemClick());
        mReviewListView.setAdapter(reviewListAdapter);
        mReviewListView.setOnItemClickListener(new ReviewItemClick());
        mContext = this;
    }

     class OwnItemClick  implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Hand formItem = (Hand)ownerListAdapter.getItem(position);
            int formid = formItem.getId();
            int state = formItem.getState();
            int sheetlogid = formItem.getSheetLogId();
            int rid = formItem.getrId();
            if(rid>0){
                PrefData.setReviewerSelectId(rid);
                Log.i("BYZ","onItemClick setReviewerSelectId:"+rid);
            }
            String  reviewer = formItem.getReviewer();
            String sheetname = formItem.getSheetName();
            Log.i("BYZ","onItemClick sheetlogid:"+sheetlogid);
            Log.i("BYZ","onItemClick sheetname:"+sheetname);
            Log.i("BYZ","onItemClick formid:"+formid);
            Log.i("BYZ","onItemClick state:"+state);
            Log.i("BYZ","onItemClick reviewer:"+reviewer);
            Log.i("BYZ","onItemClick rid:"+rid);

            //0:普通表单 1：进行中 3:已驳回 4：本地待复核(无任何操作)  无2

            if(state == 0){
                //调用接口，获取表单数据
                Intent intent = new Intent("com.tonmx.inspection.formtemplete");
                intent.putExtra("sheetid",formid);
                intent.putExtra("sheetname",sheetname);
                intent.putExtra("sheetlogid",sheetlogid);
                intent.putExtra("state",state);
                sendBroadcast(intent);

            } else if(state == 1 || state ==3){

                if(formTemplateDBHelper.readPagingForShow(formid,"A").size()<=0){
                    //若本地没有数据，通过接口获取
                    Intent intent = new Intent("com.tonmx.inspection.formtemplete");
                    intent.putExtra("sheetid",formid);
                    intent.putExtra("sheetname",sheetname);
                    intent.putExtra("sheetlogid",sheetlogid);
                    intent.putExtra("state",state);
                    intent.putExtra("reviewer",reviewer);
                    intent.putExtra("rid",rid);
                    sendBroadcast(intent);
                } else {
                    //直接跳转界面，通过数据库数据填充界面list
                    Intent intent = new Intent(getApplicationContext(), OwnerInformationActivity.class);
                    intent.putExtra("sheetid", formid);
                    intent.putExtra("sheetname", sheetname);
                    intent.putExtra("state", state);
                    intent.putExtra("sheetlogid", sheetlogid);
                    intent.putExtra("reviewer",reviewer);
                    intent.putExtra("rid",rid);
                    startActivity(intent);
                }
                //startActivity(new Intent(UserInformation.this,FormInformationActivity.class));

            } else if(state == 4){//待处理
                //进入回看界面，但不显示按钮
                ToastUtil.showShortToast(UserInformation.this,"待审核");

            }

        }
    }

     class ReviewItemClick  implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.i("BBB","ReviewItemClick 1");
            Todo formItem = (Todo)reviewListAdapter.getItem(position);
            int sheetlogid = formItem.getSheetLogId();
            int sheetid = formItem.getId();
            Log.i("BBB","ReviewItemClick 2 sheetlogid:"+sheetlogid);
            Log.i("BBB","ReviewItemClick 2 sheetid:"+sheetid);
            //直接调用接口查询
            //String sheetname = formItem.getSheetName();
            //Log.i("BBB","ReviewItemClick 2 sheetname:"+sheetname);
            sendReviewBroadcast(sheetlogid,sheetid);
        }
    }

    private void sendReviewBroadcast(int sheetlogid,int sheetid){
        Intent intent = new Intent("com.tonmx.inspection.reviewInspection");
        intent.putExtra("sheetId", sheetid);
        intent.putExtra("sheetlogid", sheetlogid);
        //intent.putExtra("sheetname", sheetname);
        sendBroadcast(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("UserInformation111","onCreate ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        initMenu();
        initData();


    }


    private void initMenu() {
        mOwnListView = (ListView) findViewById(R.id.own_listview);
        mReviewListView = (ListView) findViewById(R.id.review_listview);
        username = (TextView) findViewById(R.id.user_name);
        username.setText(PrefData.getFullName());
        departname = (TextView) findViewById(R.id.dept_name);
        departname.setText(PrefData.getDepartName());
        refresh = (ImageView) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("BYZ","image click");
                Intent intent = new Intent("com.tonmx.inspection.refreshUserInformationOnly");
                sendBroadcast(intent);
                mHandler.sendEmptyMessageDelayed(MSG_REFRESH_VIEW,100);
            }
        });
        user_menu = (ImageView) findViewById(R.id.user_menu);

        user_menu.setOnClickListener(this);
        mMenu = new UserMenu(this);
        mMenu.addItem(R.string.revise_password, REVICE_PASSWORD);
        mMenu.addItem(R.string.log_out, LOG_OUT);
        mMenu.setOnItemSelectedListener(new PopMenu.OnItemSelectedListener() {
            @Override
            public void selected(View view, PopMenu.Item item, int position) {
                switch (item.id) {
                    case REVICE_PASSWORD:
                        startActivity(new Intent(UserInformation.this, RevisePwdActivity.class));
                        break;
                    case LOG_OUT:
                        showInspectionDialog();
                        break;
                }
            }
        });
    }



    private void showInspectionDialog(){


        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        final InspectionDialog.Builder builder1=new InspectionDialog.Builder(this);
        builder1.setTitle("退出登录");
        builder1.setMessage("是否确认退出？");
        builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //share登录标志位置0
                //跳转登录界面
                PrefData.setLoginStatus(0);
                startActivity(new Intent(UserInformation.this, LoginActivity.class));
            }
        });
        builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder1.create().show();

    }

    @Override
    protected void onResume() {
        //Log.i("UserInformation111","onResume");
        super.onResume();
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), InspectionService.class);
        startService(intent);
        mHandler.sendEmptyMessage(MSG_REFRESH_VIEW);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_menu:
                mMenu.showAsDropDown(v);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        Log.i("UserInformation111","onDestroy");
        super.onDestroy();
    }
}