package com.tonmx.inspection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

class ReviewInspectionAdapter extends BaseAdapter {
    private LinkedList<ReviewTempleteItem> items;

    private Context context;

    public ReviewInspectionAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        if (items != null) {
            return  items.size();
        }
        return 0;
    }

    public void setList(LinkedList<ReviewTempleteItem> items) {
        this.items = items;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ReviewInspectionAdapter.ReviewInspectionHolder reviewInspectionHolder;
        if (convertView == null) {

            reviewInspectionHolder = new ReviewInspectionAdapter.ReviewInspectionHolder();

            convertView = LayoutInflater.from(context).inflate(R.layout.review_information, null);

            reviewInspectionHolder.deviceSeat = (TextView) convertView.findViewById(R.id.deviceSeat);
            reviewInspectionHolder.deviceName = (TextView) convertView.findViewById(R.id.deviceName);
            reviewInspectionHolder.review = (TextView) convertView.findViewById(R.id.review);
            reviewInspectionHolder.clickstatus = (TextView) convertView.findViewById(R.id.click_status);
            reviewInspectionHolder.status = (TextView) convertView.findViewById(R.id.status);
            reviewInspectionHolder.remarks = (TextView) convertView.findViewById(R.id.remarks);

            convertView.setTag(reviewInspectionHolder);

        } else {
            reviewInspectionHolder = (ReviewInspectionAdapter.ReviewInspectionHolder) convertView.getTag();
        }

        reviewInspectionHolder.deviceSeat.setText("设备位置："+items.get(position).getDeviceSeat());
        reviewInspectionHolder.deviceName.setText("设备名称："+items.get(position).getDeviceName());
        reviewInspectionHolder.review.setText("巡视内容："+items.get(position).getReview());
        reviewInspectionHolder.status.setText("结论："+items.get(position).getStatus());
        reviewInspectionHolder.remarks.setText("备注："+items.get(position).getRemarks());
        reviewInspectionHolder.clickstatus.setText(String.valueOf(items.get(position).getNumber()));

        return  convertView;
    }

    class ReviewInspectionHolder{
        TextView deviceSeat;
        TextView deviceName;
        TextView review;
        TextView clickstatus;
        TextView status;
        TextView remarks;
    }
}
