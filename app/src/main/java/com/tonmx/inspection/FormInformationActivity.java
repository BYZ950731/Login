package com.tonmx.inspection;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tonmx.inspection.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FormInformationActivity extends Activity implements AdapterView.OnItemClickListener,View.OnClickListener{

    int sheetid = -1;
    int state = -1;
    int sheetlogid = -1;
    String sheetname;
    String reviewer;
    private FormTemplateDBHelper mDB = null;
    private FormNameDBHelper  mformDB = null;
    private TextView title;
    private ImageView previousPage,nextPage ,back;
    private ListView mlistview = null;
    private OwnInspectionAdapter ownInspectionAdapter;
    private static final int MSG_REFRESH_VIEW = 1;
    private String[] pages= new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T"};
    private int count = 0;
    private LinkedList<FormTempleteItem> mItemList = new LinkedList<>();
    private boolean end =false;
    private InspectionDialog mDialog = null;
    private List<ReviewerItem> reviewerlist = null;
    private Dialog reviewDialog;
    private String reviewername =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_information);
        final Intent intent=getIntent();
        sheetid=intent.getIntExtra("sheetid",-1);
        state = intent.getIntExtra("state",-1);
        sheetname=intent.getStringExtra("sheetname");
        sheetlogid = intent.getIntExtra("sheetlogid",-1);
        reviewer=intent.getStringExtra("reviewer");
        Log.i("BYZ","FormInformationActivity sheetlogid:"+sheetlogid);
        initView();
        //获取数据库
        if(sheetid>=0){
            refreshList();
        }
        count = 0;
        Log.i("ZXC","count:"+count);


    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what) {
                case MSG_REFRESH_VIEW:
                    //刷新列表
                    Log.i("BYZ","MSG_REFRESH_VIEW");
                    refreshList();
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessage(MSG_REFRESH_VIEW);
    }

    private void refreshList() {
        if (mItemList != null || mItemList.size() > 0) {
            mItemList.clear();
        }

            mItemList = mDB.readPagingForShow(sheetid,pages[count]);
            Log.i("BYZ","leng:"+mItemList.size());
            if (mItemList != null && mItemList.size() > 0) {
                ownInspectionAdapter = new OwnInspectionAdapter(this,mItemList);
                mlistview.setAdapter(ownInspectionAdapter);
                ownInspectionAdapter.setList(mItemList);
                ownInspectionAdapter.notifyDataSetChanged();
            } else {
                return;
            }

            if(mDB.readPagingForShow(sheetid,pages[count+1]).size()== 0){
                Log.i("ASDF","提交");
                end = true;
                previousPage.setImageResource(R.mipmap.previous_page_light);
                previousPage.setEnabled(true);
                nextPage.setImageResource(R.mipmap.submit);
                nextPage.setEnabled(true);
            }

    }

    private void initView(){
        mDB = new FormTemplateDBHelper(this);
        mformDB = new FormNameDBHelper(this);
        reviewerlist = PrefData.getReviewerInspection();
        for(ReviewerItem s : reviewerlist){
            Log.i("ASDF","form reviewer:"+s);
        }

        ownInspectionAdapter = new OwnInspectionAdapter(this,mItemList);
        mlistview = (ListView) findViewById(R.id.form_list);
        mlistview.setAdapter(ownInspectionAdapter);
        title = (TextView) findViewById(R.id.sheetname);
        title.setText(sheetname);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state == 3){
                    Intent intent = new Intent("com.tonmx.inspection.refreshUserInformation");
                    sendBroadcast(intent);
                } else {
                    showBackDialog();
                }

                //startActivity(new Intent(FormInformationActivity.this,UserInformation.class));
            }
        });
        previousPage = (ImageView) findViewById(R.id.previous);
        previousPage.setEnabled(false);
        previousPage.setOnClickListener(this);
        nextPage = (ImageView) findViewById(R.id.next);
        nextPage.setEnabled(true);
        nextPage.setOnClickListener(this);

    }

    private void showIncompleteDialog(int number){


        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        final InspectionDialog.Builder builder1=new InspectionDialog.Builder(this);
        builder1.setTitle("未完成项");
        builder1.setMessage("当前表单有未完成序列 "+number);
        builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //跳转登录界面
                dialog.dismiss();
            }
        });
//        builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                FormItem item = new FormItem();
//                item.setState(0);
//                item.setId(sheetid);
//                mDB.deleteNormal(sheetid);
//                mformDB.updateState(PrefData.getUserId(),1,item);
//                startActivity(new Intent(FormInformationActivity.this, UserInformation.class));
//            }
//        });
        builder1.create().show();

    }


    private void showBackDialog(){


        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        final InspectionDialog.Builder builder1=new InspectionDialog.Builder(this);
        builder1.setTitle("返回");
        builder1.setMessage("是否保留当前巡检记录？");
        builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //跳转登录界面
                Hand item = new Hand();
                item.setState(1);
                item.setId(sheetid);
                mformDB.updateState(PrefData.getUserId(),item);
                Intent intent = new Intent("com.tonmx.inspection.refreshUserInformation");
                sendBroadcast(intent);

               // startActivity(new Intent(FormInformationActivity.this, UserInformation.class));
            }
        });
        builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Hand item = new Hand();
                item.setState(0);
                item.setId(sheetid);
                item.setSheetLogId(sheetlogid);
                mDB.deleteNormal(sheetid);
                mformDB.updateState(PrefData.getUserId(),item);
                Intent intent = new Intent("com.tonmx.inspection.refreshUserInformation");
                sendBroadcast(intent);

                //startActivity(new Intent(FormInformationActivity.this, UserInformation.class));
            }
        });
        builder1.create().show();

    }

    private void setListViewPos(int pos) {
        if (android.os.Build.VERSION.SDK_INT >= 8) {
            mlistview.smoothScrollToPosition(pos);
        } else {
            mlistview.setSelection(pos);
        }
    }




        @Override
    public void onClick(View v) {
            //DialogUtil.showReviewerDialog(FormInformationActivity.this,sheetid,sheetname).show();
        switch (v.getId()) {

            case R.id.next:
                Log.i("ASDF","count:"+count);
                Log.i("ASDF","end:"+end);
                if(!end){
                    if(count == 0){
                        previousPage.setEnabled(true);
                        previousPage.setImageResource(R.mipmap.previous_page_light);
                    }
                    setListViewPos(0);
                        count++;

                } else {
                    int number = mDB.readincompleteNumber(sheetid);
                    if(number > 0){
                        showIncompleteDialog(number);
                    } else {
                        //复核人选择
//                        String a= PrefData.getReviewerInspection().get(0);
//                        Log.i("BYZ","reviewer:"+a);
                        if(state == 3){

                        }else {
                            DialogUtil.showReviewerDialog(FormInformationActivity.this,sheetid,sheetname,state,sheetlogid,0).show();

                        }
                        //startActivity(new Intent(FormInformationActivity.this,UserInformation.class));
                    }

                }
                mHandler.sendEmptyMessage(MSG_REFRESH_VIEW);
                break;
            case R.id.previous:
                if(count == 0){
                    previousPage.setEnabled(false);
                    previousPage.setImageResource(R.mipmap.previous_page);
                    mHandler.sendEmptyMessage(MSG_REFRESH_VIEW);
                    return;
                } else {
                    previousPage.setEnabled(true);
                    previousPage.setImageResource(R.mipmap.previous_page_light);
                    mHandler.sendEmptyMessage(MSG_REFRESH_VIEW);
                    if(count>0){
                        setListViewPos(0);
                        count--;
                        end =false;
                        nextPage.setImageResource(R.mipmap.next_page);
                    }

                    if(count == 0 ){
                        previousPage.setEnabled(false);
                        previousPage.setImageResource(R.mipmap.previous_page);
                        mHandler.sendEmptyMessage(MSG_REFRESH_VIEW);
                    }
                }
                mHandler.sendEmptyMessage(MSG_REFRESH_VIEW);

                break;

            default:
                break;
        }

    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}