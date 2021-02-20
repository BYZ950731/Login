package com.tonmx.inspection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

class OwnerListAdapter extends BaseAdapter {
    private LinkedList<Hand> items;

    private Context context;


    public OwnerListAdapter(Context context){
        this.context = context;
    }

    @SuppressWarnings("unchecked")
    public void setList(LinkedList<Hand> items) {
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
        final OwnerListViewHolder ownerListViewHolder;
        if (convertView == null) {

            ownerListViewHolder = new OwnerListViewHolder();

            convertView = LayoutInflater.from(context).inflate(R.layout.owner_listview_item, null);

            ownerListViewHolder.listform = (TextView) convertView.findViewById(R.id.own_form);
            ownerListViewHolder.listtime = (TextView) convertView.findViewById(R.id.own_time);
            ownerListViewHolder.status = (ImageView) convertView.findViewById(R.id.own_status);

            convertView.setTag(ownerListViewHolder);

        } else {

            ownerListViewHolder = (OwnerListViewHolder) convertView.getTag();
        }
        String time=  stampToDate(items.get(position).getUpdateDatetime());
        ownerListViewHolder.listform.setText(items.get(position).getSheetName());
        ownerListViewHolder.listtime.setText("更新时间："+time);
        if(items.get(position).getState()==0){
            ownerListViewHolder.status.setVisibility(View.GONE);

        } else if(items.get(position).getState()==1){
            ownerListViewHolder.status.setVisibility(View.VISIBLE);
            ownerListViewHolder.status.setImageResource(R.mipmap.status_in);
        } else if(items.get(position).getState()==4){//本地待复核
            ownerListViewHolder.status.setVisibility(View.VISIBLE);
            ownerListViewHolder.status.setImageResource(R.mipmap.status_review);
        } else if (items.get(position).getState()==3) {//已驳回
            ownerListViewHolder.status.setVisibility(View.VISIBLE);
            ownerListViewHolder.status.setImageResource(R.mipmap.rejected);
        }
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


     class OwnerListViewHolder{
        TextView listform;
        TextView listtime;
        ImageView status;
    }
}
