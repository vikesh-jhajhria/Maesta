package com.maesta.maesta.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maesta.maesta.R;
import com.maesta.maesta.vo.OrderHistoryDetailVO;

import java.util.List;

/**
 * Created by saloni.bhansali on 4/21/2016.
 */
public class OrderHistoryDetailAdapter extends RecyclerView.Adapter<OrderHistoryDetailAdapter.ViewHolder> {
    Context context;

    List<OrderHistoryDetailVO> order;
    public OrderHistoryDetailAdapter(List<OrderHistoryDetailVO>  order, Context context){
        this.context=context;
        this.order=order;
    }
    @Override
    public  ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_order_history_detail,parent,false);
    return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final OrderHistoryDetailVO  orderDetail = order .get(position);
        holder.product_name.setText(orderDetail.product_name );
        holder.quantityno.setText(orderDetail. quantity_number );
        holder.quantity.setText(orderDetail.quantity );
        holder.price.setText(orderDetail.price );

    }
    @Override
    public int getItemCount() {
        return  order.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder  {
        TextView  product_name, quantity, price, quantityno;

       // CardView product_detail_card;

        public ViewHolder(View itemView) {
            super(itemView);;
            product_name    =   (TextView) itemView.findViewById(R.id.txtview_product_name);
            quantity       =   (TextView) itemView.findViewById(R.id. txtview_quantity);
            price        =   (TextView) itemView.findViewById(R.id.txt_view_price);
            quantityno        =   (TextView) itemView.findViewById(R.id.txtview_quantity_number);
           // product_detail_card    =   (CardView) itemView.findViewById(R.id.product_detail_card);



        }
}

}
