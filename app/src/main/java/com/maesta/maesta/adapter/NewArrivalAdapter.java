package com.maesta.maesta.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
    public void onBindViewHolder(ProductHolder holder, int position) {
        holder.title.setText(list.get(position).title);
        holder.price.setText(list.get(position).price);
    }



    class ProductHolder extends RecyclerView.ViewHolder {
        TextView title, price;
        ImageView image;
        public ProductHolder (View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.txt_title);
            price = (TextView) itemView.findViewById(R.id.txt_price);
            image = (ImageView) itemView.findViewById(R.id.img_product);

            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.txt_title), Config.BOLD);
            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.txt_price),Config.BOLD);
        }
    }
}
