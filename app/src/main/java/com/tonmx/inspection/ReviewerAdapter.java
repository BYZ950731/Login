package com.tonmx.inspection;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class ReviewerAdapter  extends BaseAdapter {
    private List<ReviewerDialogItem> itemall;
    private List<ReviewerDialogItem> items;
    private Context context;
    private boolean click = false;

    public ReviewerAdapter(Context context, List<ReviewerDialogItem> texts) {
        this.context = context;
        this.items = texts;
    }

    public ReviewerAdapter(Context context, List<ReviewerDialogItem> data,boolean click) {
        this.context = context;
        this.itemall = data;
        this.click = click;
        this.items = data;
    }

    public void setList(List<ReviewerDialogItem> items) {
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


    public List<ReviewerDialogItem> selectforshow(String select) {
        int i=0;
        Log.i("LKJ","1.5:"+items.size());
        Log.i("LKJ","1.6:"+itemall.size());
        List<ReviewerDialogItem> items1 = new ArrayList<>();
        if(select != null || select.length()>0){
            Log.i("LKJ","2:"+this.itemall.size());

            for(i=0;i<=this.itemall.size()-1;i++){
                Log.i("LKJ","3:"+itemall.get(i).getText());
                if(this.itemall.get(i).getText().contains(select)){
                    Log.i("LKJ","3.5:"+"add");
                    items1.add(this.itemall.get(i));
                }
            }
        } else {
            items1 = itemall;

        }


        return  items1;

        //notifyDataSetChanged();
    }

    public void select(int position) {
        Log.i("LKJ","items"+items.size());
        if (!items.get(position).isSelected()) {
            items.get(position).setSelected(true);
            for (int i = 0; i < items.size(); i++) {
                if (i != position) {
                    items.get(i).setSelected(false);
                }
            }
        }
        PrefData.setReviewerSelectName(items.get(position).getText());
        PrefData.setReviewerSelectId(items.get(position).getId());
        Log.i("LKJ","setReviewerSelectName:"+items.get(position).getText());
        Log.i("CVB","select notifyDataSetChanged");
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReviewerHolder holder;
        if (convertView == null) {
            holder = new ReviewerAdapter.ReviewerHolder();

            convertView = LayoutInflater.from(context).inflate(R.layout.reviewer_item, null);
            holder.name = (TextView) convertView.findViewById(R.id.click_name);
            holder.click =(ImageView) convertView.findViewById(R.id.click_cycle);
            convertView.setTag(holder);
        } else {
            holder = (ReviewerHolder) convertView.getTag();
        }

        holder.name.setText(items.get(position).getText());
        Log.i("CVB","select click:"+click);
        if(items.get(position).isSelected()){
            holder.click.setImageResource(R.mipmap.result_select);
        } else {
            holder.click.setImageResource(R.mipmap.not_select);
        }




        return convertView;

    }

    class ReviewerHolder{
        TextView name;
        ImageView click;
    }


}
