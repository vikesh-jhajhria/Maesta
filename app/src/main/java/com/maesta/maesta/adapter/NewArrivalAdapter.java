package com.maesta.maesta.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maesta.maesta.ProductDetailActivity;
import com.maesta.maesta.R;
import com.maesta.maesta.utils.Config;
import com.maesta.maesta.utils.Utils;
import com.maesta.maesta.vo.Product;

import java.util.ArrayList;

/**
 * Created by vikesh.kumar on 7/19/2016.
 */
public class NewArrivalAdapter extends RecyclerView.Adapter<NewArrivalAdapter.ProductHolder>{
    Context context;
    ArrayList<Product> list;

    public NewArrivalAdapter(Context context, ArrayList<Product> list) {
        this.context = context;
        this.list = list;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_new_arrival_item, parent, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {
        holder.title.setText(list.get(position).title);
        holder.price.setText(list.get(position).price);
        holder.new_arrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("ID",list.get(position).id);
                intent.putExtra("TITLE", list.get(position).title);
                context.startActivity(intent);

            }
        });
        Glide.with(context).load(list.get(position).thumbURL).asBitmap()
                .placeholder(R.drawable.default_image).fitCenter().into(holder.image);
    }



    class ProductHolder extends RecyclerView.ViewHolder {
        TextView title, price;
        ImageView image;
        CardView new_arrival;
        public ProductHolder (View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.txt_title);
            price = (TextView) itemView.findViewById(R.id.txt_price);
            image = (ImageView) itemView.findViewById(R.id.img_product);
            new_arrival=(CardView)itemView.findViewById(R.id.card_newarrival);
            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.txt_title), Config.BOLD);
            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.txt_price),Config.BOLD);
        }
    }
}
