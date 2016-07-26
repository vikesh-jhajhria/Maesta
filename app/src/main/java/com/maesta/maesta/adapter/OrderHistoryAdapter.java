package com.maesta.maesta.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maesta.maesta.OrderHistoryDetailActivity;
import com.maesta.maesta.R;
import com.maesta.maesta.utils.Config;
import com.maesta.maesta.utils.Utils;
import com.maesta.maesta.vo.Order;

import java.util.List;

/**
 * Created by saloni.bhansali on 4/21/2016.
 */
public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    Context context;

    List<Order> orderhistory;
    public OrderHistoryAdapter(List<Order>  order, Context context){
        this.context=context;
        this.orderhistory=order;
    }
    @Override
    public  ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_order_history,parent,false);
    return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Order orderHistory = orderhistory.get(position);
        holder.date.setText(orderHistory.order_place);
        holder.orderid.setText(orderHistory.invoice_number);
        holder.price.setText(orderHistory.total_amount);
        holder.pending.setText(orderHistory.order_status);

        if (orderHistory.order_status.equalsIgnoreCase("delivered")) {
            holder.pending.setTextColor(context.getResources().getColor(R.color.green));
        }else if(orderHistory.order_status.equalsIgnoreCase("pending") ){
            holder.pending.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OrderHistoryDetailActivity.class);
                intent.putExtra("ID",orderHistory.order_id);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return  orderhistory.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder  {
        TextView  date,  status, price, orderid,total,pending,order;


        public ViewHolder(View itemView) {
            super(itemView);;
            date    =   (TextView) itemView.findViewById(R.id.date);
            status       =   (TextView) itemView.findViewById(R.id. textview_staus);
            price        =   (TextView) itemView.findViewById(R.id.price);
            orderid        =   (TextView) itemView.findViewById(R.id.textview_id);
            total        =   (TextView) itemView.findViewById(R.id.total);
            pending        =   (TextView) itemView.findViewById(R.id.textview_pending);
            order        =   (TextView) itemView.findViewById(R.id.textview_order);

            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.textview_staus), Config.REGULAR);
            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.price),Config.BOLD);
            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.textview_id), Config.MEDIUM);
            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.total),Config.MEDIUM);
            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.textview_pending), Config.BOLD);
            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.textview_order),Config.BOLD);
            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.date), Config.REGULAR);


        }

}

}
