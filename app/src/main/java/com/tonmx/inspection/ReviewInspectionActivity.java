package com.tonmx.inspection;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;

public class ReviewInspectionActivity extends Activity implements View.OnClickListener{

    int sheetlogid = -1;
    int sheetid = -1;
    String sheetname;
    private ImageView back,confirm,cancel;
    private TextView textname;
    private static final int MSG_REFRESH_VIEW = 1;
    private ReviewFormDBHelper mDB = null;
    private LinkedList<ReviewTempleteItem> mItemList = new LinkedList<>();
    private ReviewInspectionAdapter reviewInspectionAdapter;
    private ListView mlistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_inspection);

        final Intent intent=getIntent();
        sheetlogid=intent.getIntExtra("sheetlogid",-1);
        sheetid=intent.getIntExtra("sheetid",-1);
        //sheetname=intent.getStringExtra("sheetname");
        mDB = new ReviewFormDBHelper(this);
        reviewInspectionAdapter = new ReviewInspectionAdapter(this);
        initView();
        refreshList();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MSG_REFRESH_VIEW:
                    //刷新列表
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

        mItemList = mDB.readReviewList(sheetlogid);
        Log.i("BYZ","review size:"+mItemList.size());
        if (mItemList != null && mItemList.size() > 0) {
            reviewInspectionAdapter.setList(mItemList);
            reviewInspectionAdapter.notifyDataSetChanged();
        }

    }

    private void initView(){
        mlistview = (ListView) findViewById(R.id.review_list);
        mlistview.setAdapter(reviewInspectionAdapter);
        back = (ImageView) findViewById(R.id.back);
        confirm = (ImageView) findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
        cancel = (ImageView) findViewById(R.id.re_return);
        cancel.setOnClickListener(this);
        textname = (TextView) findViewById(R.id.sheetname);
        textname.setText(sheetname);
        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.back:
                startActivity(new Intent(ReviewInspectionActivity.this,UserInformation.class));
                break;
            case R.id.confirm:
                //发送复核确认广播

                Intent intent = new Intent("com.tonmx.inspection.reviewConfirm");
                intent.putExtra("sheetlogid", sheetlogid);

                sendBroadcast(intent);
                break;
            case R.id.re_return:
                //发送复核退会广播
                Intent intent2 = new Intent("com.tonmx.inspection.reviewReturn");
                intent2.putExtra("sheetlogid", sheetlogid);
                sendBroadcast(intent2);
                break;
        }
    }
}