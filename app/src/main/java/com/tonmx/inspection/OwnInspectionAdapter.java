package com.tonmx.inspection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.wrbug.editspinner.EditSpinner;

import java.util.HashMap;
import java.util.LinkedList;


class OwnInspectionAdapter extends BaseAdapter {
    private LinkedList<FormTempleteItem> items;
    private Context context;
    private FormTemplateDBHelper formTemplateDBHelper;
    OwnInspectionHolder ownInspectionHolder;
    private int selectedEditTextPosition = -1;

    public OwnInspectionAdapter(Context context,LinkedList<FormTempleteItem> items){
        this.items =items;
        this.context = context;
    }
    public void setList(LinkedList<FormTempleteItem> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        if (items != null) {
            return  items.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (items != null) {
            return items.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.form_information, null);

            ownInspectionHolder = new OwnInspectionHolder(convertView,position);

            convertView.setTag(ownInspectionHolder);
        } else {
            ownInspectionHolder = (OwnInspectionHolder) convertView.getTag();
        }

        final FormTempleteItem item = items.get(position);
//        ownInspectionHolder.status.setTag(position);
//        ownInspectionHolder.status.clearFocus();
//        ownInspectionHolder.remarks.setTag(position);
//        ownInspectionHolder.remarks.clearFocus();
//
//        if (ownInspectionHolder.status.getTag() instanceof TextWatcher) {
//            ownInspectionHolder.status.removeTextChangedListener((TextWatcher) (ownInspectionHolder.status.getTag()));
//        }
//        if (ownInspectionHolder.remarks.getTag() instanceof TextWatcher) {
//            ownInspectionHolder.remarks.removeTextChangedListener((TextWatcher) (ownInspectionHolder.remarks.getTag()));
//        }
        ownInspectionHolder.remarks.;
        ownInspectionHolder.remarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //int pos = (int) ownInspectionHolder.remarks.getTag();
               // FormTempleteItem item1= items.get(pos);
                formTemplateDBHelper = new FormTemplateDBHelper(context);
                item.setRemarks(s.toString());
                if(TextUtils.isEmpty(s.toString())){
                  item.setRemarks("");
                }
               // Log.i("afterTextChanged","item number"+item1.getNumber());
                formTemplateDBHelper.updateRemarks(item);
            }
        });

        if(item.getType()==1){
            ownInspectionHolder.status.setVisibility(View.GONE);
            ownInspectionHolder.result_select.setVisibility(View.VISIBLE);
            ownInspectionHolder.normal.setVisibility(View.VISIBLE);
            ownInspectionHolder.not_select.setVisibility(View.VISIBLE);
            ownInspectionHolder.abnormal.setVisibility(View.VISIBLE);
            if(item.getStatus().equalsIgnoreCase("正常")){
                ownInspectionHolder.result_select.setImageResource(R.mipmap.result_select);
                ownInspectionHolder.not_select.setImageResource(R.mipmap.not_select);
            } else if(item.getStatus().equalsIgnoreCase("异常")){
                ownInspectionHolder.result_select.setImageResource(R.mipmap.not_select);
                ownInspectionHolder.not_select.setImageResource(R.mipmap.result_select);
            }
        } else {
            ownInspectionHolder.status.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    //int pos = (int) ownInspectionHolder.status.getTag();
                    //FormTempleteItem item2= items.get(pos);
                    formTemplateDBHelper = new FormTemplateDBHelper(context);
                    item.setClick(1);
                    item.setStatus(s.toString());
                    if(TextUtils.isEmpty(s.toString())){
                        item.setClick(0);
                        item.setStatus("");
                    }
                    formTemplateDBHelper.updateStatus(item);

                }
            });
            ownInspectionHolder.status.setVisibility(View.VISIBLE);
            ownInspectionHolder.result_select.setVisibility(View.GONE);
            ownInspectionHolder.not_select.setVisibility(View.GONE);
            ownInspectionHolder.normal.setVisibility(View.GONE);
            ownInspectionHolder.abnormal.setVisibility(View.GONE);
            if(item.getStatus()!=null&& item.getStatus().length()>0){
                ownInspectionHolder.status.setText(item.getStatus());
            }

        }

        ownInspectionHolder.result_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setClick(1);
                item.setStatus("正常");
                formTemplateDBHelper = new FormTemplateDBHelper(context);
                formTemplateDBHelper.updateClick(item);
                notifyDataSetChanged();
            }
        });
        ownInspectionHolder.not_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("QWE","notselect");
                item.setClick(1);
                item.setStatus("异常");
                formTemplateDBHelper = new FormTemplateDBHelper(context);
                formTemplateDBHelper.updateClick(item);
                notifyDataSetChanged();
            }
        });
        ownInspectionHolder.normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setClick(1);
                item.setStatus("正常");
                formTemplateDBHelper = new FormTemplateDBHelper(context);
                formTemplateDBHelper.updateClick(item);
                notifyDataSetChanged();
            }
        });
        ownInspectionHolder.abnormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("QWE","abnormal");
                item.setClick(1);
                item.setStatus("异常");
                formTemplateDBHelper = new FormTemplateDBHelper(context);
                formTemplateDBHelper.updateClick(item);
                notifyDataSetChanged();
            }
        });




        Resources res = context.getResources();
        Drawable blue = res. getDrawable(R.drawable.shape_round_bluetextview);
        Drawable white = res. getDrawable(R.drawable.shape_round_whitetextview);
        if(items.get(position).getClick()==1){
            ownInspectionHolder.clickstatus.setBackground(blue);
        } else {
            ownInspectionHolder.clickstatus.setBackground(white);
        }
        ownInspectionHolder.clickstatus.setText(String.valueOf(items.get(position).getNumber()));
        ownInspectionHolder.deviceSeat.setText("设备位置："+items.get(position).getDeviceSeat());
        ownInspectionHolder.deviceName.setText("设备名称："+items.get(position).getDeviceName());
        ownInspectionHolder.review.setText("巡视内容："+items.get(position).getReview());




        if(item.getRemarks()!=null && item.getRemarks().length()>0){
            ownInspectionHolder.remarks.setText(items.get(position).getRemarks());
        }


        return convertView;
    }

    class OwnInspectionHolder{
        TextView deviceSeat;
        TextView deviceName;
        TextView review;
        TextView clickstatus;
        TextView normal;
        TextView abnormal;
        ImageView result_select;
        ImageView not_select;
        EditText status;
        //EditText remarks;
        EditSpinner remarks;


        public OwnInspectionHolder(View convertView,int position){
            deviceSeat = (TextView) convertView.findViewById(R.id.deviceSeat);
            deviceName = (TextView) convertView.findViewById(R.id.deviceName);
            review = (TextView) convertView.findViewById(R.id.review);
            result_select = (ImageView) convertView.findViewById(R.id.result_select);
            not_select = (ImageView) convertView.findViewById(R.id.not_select);
            normal = (TextView) convertView.findViewById(R.id.normal);
            abnormal = (TextView) convertView.findViewById(R.id.abnormal);
            clickstatus = (TextView) convertView.findViewById(R.id.click_status);
            status = (EditText) convertView.findViewById(R.id.status);
            //remarks = (EditText) convertView.findViewById(R.id.remarks);
            remarks = (EditSpinner) convertView.findViewById(R.id.remarks);

        }
    }
}
