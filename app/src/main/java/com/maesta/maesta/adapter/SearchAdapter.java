package com.maesta.maesta.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.maesta.maesta.vo.Collection;

import java.util.List;

/**
 * Created by saloni.bhansali on 4/21/2016.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    Context context;

    List<Collection> searchs;

    public SearchAdapter(List<Collection> searchs, Context context) {
        this.context = context;
        this.searchs = searchs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Collection search = searchs.get(position);
        holder.product_name.setText(search.product_name);
        holder.price.setText(search.price);
        Glide.with(context).load(search.thumbURL).asBitmap()
                .placeholder(R.drawable.default_image).fitCenter().into(holder.product_img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("ID",searchs.get(position).id);
                intent.putExtra("TITLE",searchs.get(position).product_name );
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView product_name, price;
        ImageView product_img;

        public ViewHolder(View itemView) {
            super(itemView);

            product_name = (TextView) itemView.findViewById(R.id.txtview_product_name);
            product_img = (ImageView) itemView.findViewById(R.id.img_product);
            price = (TextView) itemView.findViewById(R.id.txt_view_price);

            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.txtview_product_name), Config.BOLD);
            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.txt_view_price), Config.BOLD);

        }
    }

}
