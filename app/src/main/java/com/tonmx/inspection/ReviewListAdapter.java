package com.tonmx.inspection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

class ReviewListAdapter extends BaseAdapter {
    private LinkedList<Todo> items;

    private Context context;


    public ReviewListAdapter(Context context){
        this.context = context;
    }
    @SuppressWarnings("unchecked")
    public void setList(LinkedList<Todo> items) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ReviewListAdapter.ReviewListViewHolder reviewListViewHolder;
        if (convertView == null) {

            reviewListViewHolder = new ReviewListAdapter.ReviewListViewHolder();

            convertView = LayoutInflater.from(context).inflate(R.layout.other_review_item, null);

            reviewListViewHolder.listform = (TextView) convertView.findViewById(R.id.submit_form);
            reviewListViewHolder.listrename = (TextView) convertView.findViewById(R.id.submit_name);
            reviewListViewHolder.listtime = (TextView) convertView.findViewById(R.id.submit_time);
            reviewListViewHolder.status = (ImageView) convertView.findViewById(R.id.status);

            convertView.setTag(reviewListViewHolder);

        } else {

            reviewListViewHolder = (ReviewListAdapter.ReviewListViewHolder) convertView.getTag();
        }

        reviewListViewHolder.listform.setText(items.get(position).getSheetName());
        reviewListViewHolder.listrename.setText(items.get(position).getInspector());

        String time=  stampToDate(items.get(position).getUpdateDatetime());
        reviewListViewHolder.listtime.setText("提交时间："+time);
//        if(items.get(position).getState()==0){
//            //reviewListViewHolder.status.setVisibility(View.GONE);
//            //驳回
//
//        } else if(items.get(position).getState()==2){
//            reviewListViewHolder.status.setVisibility(View.VISIBLE);
            reviewListViewHolder.status.setImageResource(R.mipmap.status_review);
     //   }
        return convertView;
    }

    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }


    class ReviewListViewHolder{
        TextView listform;
        TextView listrename;
        TextView listtime;
        ImageView status;
    }
}
