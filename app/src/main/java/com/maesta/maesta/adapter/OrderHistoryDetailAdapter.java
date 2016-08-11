package com.maesta.maesta.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maesta.maesta.R;
import com.maesta.maesta.utils.Config;
import com.maesta.maesta.utils.Utils;
import com.maesta.maesta.vo.Collection;

import java.util.List;

/**
 * Created by saloni.bhansali on 4/21/2016.
 */
public class OrderHistoryDetailAdapter extends RecyclerView.Adapter<OrderHistoryDetailAdapter.ViewHolder> {
    Context context;

    List<Collection> order;
    public OrderHistoryDetailAdapter(List<Collection>  order, Context context){
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
        final Collection orderDetail = order .get(position);
        holder.product_name.setText(orderDetail.product_name );
        holder.quantityno.setText(orderDetail. quantity_number );
        holder.quantity.setText(orderDetail.quantity );
        holder.price.setText(orderDetail.price );

        Glide.with(context).load(orderDetail.thumbURL).asBitmap()
                .placeholder(R.drawable.default_image).fitCenter().into(holder.product_image);

    }
    @Override
    public int getItemCount() {
        return  order.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder  {
        TextView  product_name, quantity, price, quantityno;
        ImageView product_image;

        public ViewHolder(View itemView) {
            super(itemView);;
            product_name    =   (TextView) itemView.findViewById(R.id.txtview_product_name);
            quantity       =   (TextView) itemView.findViewById(R.id. txtview_quantity);
            price        =   (TextView) itemView.findViewById(R.id.txt_view_price);
            quantityno        =   (TextView) itemView.findViewById(R.id.et_quantity_number);
            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.txtview_product_name), Config.BOLD);
            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.txt_view_price),Config.BOLD);
            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.txtview_quantity), Config.MEDIUM);
            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.et_quantity_number), Config.REGULAR);
            product_image =   (ImageView) itemView.findViewById(R.id.img_item);



        }
}

}
