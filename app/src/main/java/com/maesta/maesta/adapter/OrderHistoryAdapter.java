package com.maesta.maesta.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maesta.maesta.R;
import com.maesta.maesta.vo.OrderHistoryDetailVO;
import com.maesta.maesta.vo.OrderHistoryVO;

import java.util.List;

/**
 * Created by saloni.bhansali on 4/21/2016.
 */
public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    Context context;

    List<OrderHistoryVO> orderhistory;
    public OrderHistoryAdapter(List<OrderHistoryVO>  order, Context context){
        this.context=context;
        this.orderhistory=order;
    }
    @Override
    public  ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout. item_oder_history,parent,false);
    return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final OrderHistoryVO orderHistory = orderhistory.get(position);
        holder.date.setText(orderHistory.date);
        holder.order.setText(orderHistory.order);
        holder.orderid.setText(orderHistory.orderId);
        holder.price.setText(orderHistory.price);
        holder.pending.setText(orderHistory.pending);

        holder.status.setText(orderHistory.status);
        holder.total.setText(orderHistory.total);
        if (orderHistory.pending.equalsIgnoreCase("pending")) {
            holder.pending.setTextColor(context.getResources().getColor(R.color.green));
        }else if(orderHistory.pending.equalsIgnoreCase("status") ){
            holder.pending.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }
    }
    @Override
    public int getItemCount() {
        return  orderhistory.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder  {
        TextView  date,  status, price, orderid,total,pending,order;

       // CardView product_detail_card;

        public ViewHolder(View itemView) {
            super(itemView);;
            date    =   (TextView) itemView.findViewById(R.id.date);
            status       =   (TextView) itemView.findViewById(R.id. textview_staus);
            price        =   (TextView) itemView.findViewById(R.id.price);
            orderid        =   (TextView) itemView.findViewById(R.id.textview_id);
            total        =   (TextView) itemView.findViewById(R.id.total);
            pending        =   (TextView) itemView.findViewById(R.id.textview_pending);
            order        =   (TextView) itemView.findViewById(R.id.textview_order);
           // product_detail_card    =   (CardView) itemView.findViewById(R.id.product_detail_card);



        }
}

}
