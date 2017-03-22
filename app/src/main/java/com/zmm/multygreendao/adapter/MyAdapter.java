package com.zmm.multygreendao.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zmm.multygreendao.R;
import com.zmm.multygreendao.model.Customer;
import com.zmm.multygreendao.utils.LogUtils;
import com.zmm.multygreendao.utils.ToastUtils;

import java.util.List;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/3/22
 * Time:上午9:56
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Customer> mCustomers;
    private int mLayoutPosition = -1;
    private long mFirstKeyId;
    private MyViewHolder mMyViewHolder;

    public MyAdapter(List<Customer> customers) {
        mCustomers = customers;
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void OnItemClick(int position,long keyId);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  View.inflate(parent.getContext(), R.layout.my_item, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        mMyViewHolder = holder;

        holder.setData(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取当前点击的位置
                mLayoutPosition = holder.getLayoutPosition();
                LogUtils.d("选中的点:  "+mLayoutPosition+",position = "+position);
                notifyDataSetChanged();
                if(onItemClickListener != null){
                    onItemClickListener.OnItemClick(mLayoutPosition,mCustomers.get(position).getId());

                }
            }
        });

        //更改状态
        if(position == mLayoutPosition){
            holder.tv_item.setBackgroundResource(R.drawable.bg_unselect);
            holder.tv_item.setTextColor(Color.RED);
        }else{
            holder.tv_item.setBackgroundResource(R.drawable.bg_select);
            holder.tv_item.setTextColor(Color.BLUE);
        }
    }

    @Override
    public int getItemCount() {
        return mCustomers.size();
    }

    public long getFirstKeyId() {
        mFirstKeyId = mCustomers.get(0).getId();
        return mFirstKeyId;
    }

    public void add(Customer customer, int position) {
        if(mLayoutPosition == position){
            mLayoutPosition = -1;
        }
        mCustomers.add(position,customer);
        notifyItemInserted(position);
    }

    public void delete(int position){
        if(mCustomers.size() == 0){
            ToastUtils.SimpleToast("当前无数据!");
            return;
        }
        mCustomers.remove(position);
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private final TextView tv_item;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_item = (TextView) itemView.findViewById(R.id.tv_item);
        }

        /**
         * 根据position位置获取数据填充位置
         * @param position
         */
        public void setData(int position) {
            itemView.setTag(position);
            tv_item.setText(mCustomers.get(position).getName());
            tv_item.setBackgroundResource(R.drawable.bg_select);
            tv_item.setTextColor(Color.BLUE);
        }
    }
}
