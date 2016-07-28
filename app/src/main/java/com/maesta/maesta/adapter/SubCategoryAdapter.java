package com.maesta.maesta.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maesta.maesta.ListingActivity;
import com.maesta.maesta.ProductDetailActivity;
import com.maesta.maesta.R;

import com.maesta.maesta.utils.Config;
import com.maesta.maesta.utils.Utils;
import com.maesta.maesta.vo.Product;

import java.util.List;


public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {
    Context context;

    List<Product> subcategory_list;

    public SubCategoryAdapter(List<Product> subcat, Context context) {
        this.context = context;
        this.subcategory_list = subcat;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_subcategary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Product subcategory = subcategory_list.get(position);
        holder.productName.setText(subcategory.title);
        Glide.with(context).load(subcategory_list.get(position).iconURL).asBitmap()
                .placeholder(R.drawable.banner_1).centerCrop().into(holder.cat_img);
        if(subcategory_list.get(position).haveSubCategories) {
            holder.next_icon.setImageResource(R.drawable.subcatory_arrow);
            holder.next_icon.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(context, ListingActivity.class);
                //intent.putExtra("ID", subcategory_list.get(position).id);
               // context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subcategory_list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        ImageView cat_img, next_icon;
        public ViewHolder(View itemView) {
            super(itemView);
            productName = (TextView) itemView.findViewById(R.id.txt_product_name);
            cat_img = (ImageView) itemView.findViewById(R.id.img_user_icon) ;
            next_icon = (ImageView) itemView.findViewById(R.id.img_collapse);
            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.txt_product_name), Config.MEDIUM);

        }
    }

}
