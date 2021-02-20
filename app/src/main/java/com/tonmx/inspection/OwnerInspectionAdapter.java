package com.tonmx.inspection;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

class OwnerInspectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private LayoutInflater inflater;
    private LinkedList<FormTempleteItem> items;
    private Context mcontext;
    private FormTemplateDBHelper formTemplateDBHelper;
    private onTextChangeListener mTextListener;
    private onTextChangeListener2 mTextListener2;


    public void setData(LinkedList<FormTempleteItem> listItems) {
        this.items = listItems;
        notifyDataSetChanged();
    }

    public OwnerInspectionAdapter(Context context, LinkedList<FormTempleteItem> listItems) {
        this.inflater = LayoutInflater.from(context);
        this.items = listItems;
        this.mcontext = context;
    }
    public void setOnTextChangeListener(onTextChangeListener onTextChangeListener){
        this.mTextListener=onTextChangeListener;
    }

    public void setOnTextChangeListener2(onTextChangeListener2 onTextChangeListener2){
        this.mTextListener2=onTextChangeListener2;
    }
    public interface onTextChangeListener {
        void onTextChanged(int pos,String str);
    }

    public interface onTextChangeListener2 {
        void onTextChanged2(int pos,String str);
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = inflater.inflate(R.layout.form_information, parent, false);
        formTemplateDBHelper = new FormTemplateDBHelper(mcontext);
        return new OwnerInspectionHodler(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final FormTempleteItem item = items.get(position);
        final OwnerInspectionHodler ownerInspectionHodler =(OwnerInspectionHodler) holder;
        //holder.setIsRecyclable(false);
       // ownerInspectionHodler.contentView.setTag(item);

        Resources res = mcontext.getResources();
        final Drawable blue = res. getDrawable(R.drawable.shape_round_bluetextview);
        final Drawable white = res. getDrawable(R.drawable.shape_round_whitetextview);
        if(items.get(position).getClick()==1){
            ownerInspectionHodler.clickstatus.setBackground(blue);
        } else {
            ownerInspectionHodler.clickstatus.setBackground(white);
        }
        ownerInspectionHodler.clickstatus.setText(String.valueOf(item.getNumber()));
        ownerInspectionHodler.deviceSeat.setText("设备位置："+item.getDeviceSeat());
        ownerInspectionHodler.deviceName.setText("设备名称："+item.getDeviceName());
        ownerInspectionHodler.review.setText("巡视内容："+item.getReview());

        if(item.getRemarks()!=null && item.getRemarks().length()>0){
            ownerInspectionHodler.remarks.setText(item.getRemarks());
        }

        if(item.getType()==1){
            ownerInspectionHodler.status.setVisibility(View.GONE);
            ownerInspectionHodler.result_select.setVisibility(View.VISIBLE);
            ownerInspectionHodler.normal.setVisibility(View.VISIBLE);
            ownerInspectionHodler.not_select.setVisibility(View.VISIBLE);
            ownerInspectionHodler.abnormal.setVisibility(View.VISIBLE);
            if(item.getStatus().equalsIgnoreCase("正常")){
                ownerInspectionHodler.result_select.setImageResource(R.mipmap.result_select);
                ownerInspectionHodler.not_select.setImageResource(R.mipmap.not_select);
            } else if(item.getStatus().equalsIgnoreCase("异常")){
                ownerInspectionHodler.result_select.setImageResource(R.mipmap.not_select);
                ownerInspectionHodler.not_select.setImageResource(R.mipmap.result_select);
            } else {
                ownerInspectionHodler.result_select.setImageResource(R.mipmap.not_select);
                ownerInspectionHodler.not_select.setImageResource(R.mipmap.not_select);
            }
        } else {
            ownerInspectionHodler.status.setVisibility(View.VISIBLE);
            ownerInspectionHodler.result_select.setVisibility(View.GONE);
            ownerInspectionHodler.not_select.setVisibility(View.GONE);
            ownerInspectionHodler.normal.setVisibility(View.GONE);
            ownerInspectionHodler.abnormal.setVisibility(View.GONE);



            if (ownerInspectionHodler.status.getTag() instanceof TextWatcher) {
                ownerInspectionHodler.status.removeTextChangedListener((TextWatcher) ownerInspectionHodler.status.getTag());
            }
            if(item.getStatus()!=null&& item.getStatus().length()>0){
                ownerInspectionHodler.status.setText(item.getStatus());
            }

            final TextWatcher textWatcher=new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    Log.e("textWatcher",ownerInspectionHodler.getAdapterPosition()+"");
                    if(ownerInspectionHodler.status.hasFocus()){//判断当前EditText是否有焦点在
                        //通过接口回调将数据传递到Activity中
                      //  mTextListener.onTextChanged(position,ownerInspectionHodler.status.getText().toString());
                        item.setStatus(s.toString());
                        item.setClick(1);
                        if(TextUtils.isEmpty(s.toString())){
                            item.setClick(0);
                            item.setStatus("");
                        }
                        if(items.get(position).getClick()==1){
                            ownerInspectionHodler.clickstatus.setBackground(blue);
                        } else {
                            ownerInspectionHodler.clickstatus.setBackground(white);
                        }
                        formTemplateDBHelper.updateStatus(item);


                    }
                }
            };

            ownerInspectionHodler.status.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        ownerInspectionHodler.status.addTextChangedListener(textWatcher);


                        Log.e("addTextChanged",position+"");
                    }else{
                        ownerInspectionHodler.status.removeTextChangedListener(textWatcher);
                        Log.e("removeTextChanged",position+"");
                    }
                }
            });

        }

        ownerInspectionHodler.result_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setClick(1);
                item.setStatus("正常");
                formTemplateDBHelper.updateClick(item);
                notifyDataSetChanged();
            }
        });
        ownerInspectionHodler.not_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("QWE","notselect");
                item.setClick(1);
                item.setStatus("异常");
                //formTemplateDBHelper = new FormTemplateDBHelper(mcontext);
                formTemplateDBHelper.updateClick(item);
                notifyDataSetChanged();
            }
        });
        ownerInspectionHodler.normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setClick(1);
                item.setStatus("正常");
                //formTemplateDBHelper = new FormTemplateDBHelper(mcontext);
                formTemplateDBHelper.updateClick(item);
                notifyDataSetChanged();
            }
        });
        ownerInspectionHodler.abnormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("QWE","abnormal");
                item.setClick(1);
                item.setStatus("异常");
                //formTemplateDBHelper = new FormTemplateDBHelper(mcontext);
                formTemplateDBHelper.updateClick(item);
                notifyDataSetChanged();
            }
        });

        ownerInspectionHodler.remarks.setText(item.getRemarks());
        final TextWatcher watcher1 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence sequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence sequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //通过接口回调将数据传递到Activity中,修改list里的bean
                if (ownerInspectionHodler.remarks.hasFocus()) {//如果获得焦点再去回调
                    //mTextListener2.onTextChanged2(position, ownerInspectionHodler.remarks.getText().toString());
                    item.setRemarks(editable.toString());
                    formTemplateDBHelper.updateRemarks(item);
                }

            }
        };
        //设置EditText的焦点监听器判断焦点变化，当有焦点时addTextChangedListener，失去焦点时removeTextChangedListener
        ownerInspectionHodler.remarks.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    ownerInspectionHodler.remarks.addTextChangedListener(watcher1);
                }else {
                    ownerInspectionHodler.remarks.removeTextChangedListener(watcher1);
                }
            }
        });








    }

    public class OwnerInspectionHodler extends RecyclerView.ViewHolder {
        TextView deviceSeat;
        TextView deviceName;
        TextView review;
        TextView clickstatus;
        TextView normal;
        TextView abnormal;
        ImageView result_select;
        ImageView not_select;
        EditText status;
        EditText remarks;
        View contentView;

        public OwnerInspectionHodler(View itemView) {
            super(itemView);
            deviceSeat = (TextView) itemView.findViewById(R.id.deviceSeat);
            deviceName = (TextView) itemView.findViewById(R.id.deviceName);
            review = (TextView) itemView.findViewById(R.id.review);
            result_select = (ImageView) itemView.findViewById(R.id.result_select);
            not_select = (ImageView) itemView.findViewById(R.id.not_select);
            normal = (TextView) itemView.findViewById(R.id.normal);
            abnormal = (TextView) itemView.findViewById(R.id.abnormal);
            clickstatus = (TextView) itemView.findViewById(R.id.click_status);
            status = (EditText) itemView.findViewById(R.id.status);
            remarks = (EditText) itemView.findViewById(R.id.remarks);
            contentView = itemView;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
