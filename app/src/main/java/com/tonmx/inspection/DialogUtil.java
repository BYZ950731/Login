package com.tonmx.inspection;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class DialogUtil {
    private static Dialog mDialog;


    public  static Dialog  showReviewerDialog(final Context context,final int sheetid,final String sheetname,final int state,final int sheetlogid,final int rid){
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.hide();
        }
        String defaultname =PrefData.getReviewerSelectName();
        if(defaultname!=null && defaultname.length()>0){
            PrefData.setReviewerSelectName("");
            PrefData.setReviewerSelectId(-1);
        }
        mDialog =  new Dialog(context, R.style.InspectionDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.reviewer_dialog_item, null);
        // ImageView imageView = (ImageView) view.findViewById(R.id.click_cycle);
        //  TextView textView = (TextView) view.findViewById(R.id.click_name);
        ImageView ok = (ImageView) view.findViewById(R.id.reviewer_ok);
        ImageView cancel = (ImageView) view.findViewById(R.id.reviewer_cancel);
        final SearchView searchView = (SearchView) view.findViewById(R.id.name_search);
        final ListView listView = (ListView) view.findViewById(R.id.name_list);
        List<ReviewerDialogItem> listViewItems = new ArrayList<ReviewerDialogItem>();
        List<ReviewerItem>  texts = PrefData.getReviewerInspection();
        for (int i = 0; i < texts.size(); i++) {
            listViewItems.add(i,new ReviewerDialogItem((String) texts.get(i).getFullName(), texts.get(i).getId(),i == -1));
        }
        Log.i("LKJ","0:"+listViewItems.size());
        final ReviewerAdapter adapter = new ReviewerAdapter(context, listViewItems,false);
        listView.setTextFilterEnabled(true);
        searchView.setSubmitButtonEnabled(true);
        listView.setAdapter(adapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("LKJ","1:"+newText);
                adapter.setList(adapter.selectforshow(newText));;
                Log.i("LKJ","list:"+adapter.selectforshow(newText));
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        listView.setLayoutParams(params);
        mDialog.setContentView(view);
        mDialog.setCancelable(false);
        mDialog.create();
        mDialog.show();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.select(position);
                Log.i("CVB","position"+position);
                ReviewerDialogItem  reviewerDialogItem = (ReviewerDialogItem)adapter.getItem(position);
                Log.i("CVB","name:"+reviewerDialogItem.getText());
                Log.i("CVB","rid:"+reviewerDialogItem.getId());
                PrefData.setReviewerSelectName(reviewerDialogItem.getText());
                PrefData.setReviewerSelectId(reviewerDialogItem.getId());
               // reviewerDialogItem.getText();
                //sendSubmitBroadcast(context,PrefData.getReviewerSelectName(),sheetid,sheetname);

                if (mDialog != null && false) {
                    mDialog.dismiss();
                }
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = PrefData.getReviewerSelectName();
                int rid = PrefData.getReviewerSelectId();
                Log.i("LKJ","name:"+name);
                if(name!=null && name.length()>0){
                    //
                    Log.i("BYZ","submit sheetlogid:"+sheetlogid);
                    if(sheetlogid<=0){
                        Log.i("BYZ","sheetlogid<=0");
                        Intent intent = new Intent("com.tonmx.inspection.submit");
                        intent.putExtra("reviewer", name);
                        intent.putExtra("sheetId", sheetid);
                        intent.putExtra("sheetname",sheetname);
                        intent.putExtra("rid",rid);

                        context.sendBroadcast(intent);
                    } else {
                        Log.i("BYZ","sheetlogid>0");
                        Intent intent = new Intent("com.tonmx.inspection.resubmit");
                        intent.putExtra("reviewer", name);
                        intent.putExtra("sheetId", sheetid);
                        intent.putExtra("sheetname",sheetname);
                        intent.putExtra("sheetlogid",sheetlogid);
                        intent.putExtra("rid",rid);
                        context.sendBroadcast(intent);

                    }

                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                }else {
                    ToastUtil.getShortToastByString(context,"请选择复核人");
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
            }
        });

        WindowManager m = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams attributes = mDialog.getWindow().getAttributes();
        attributes.height = d.getHeight()/3;
        attributes.width = d.getWidth() /3*2;
        mDialog.getWindow().setAttributes(attributes);

        return mDialog;
    }


}
