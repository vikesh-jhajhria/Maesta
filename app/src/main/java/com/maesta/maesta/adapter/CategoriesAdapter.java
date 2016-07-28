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
import com.maesta.maesta.ListingActivity;
import com.maesta.maesta.ProductDetailActivity;
import com.maesta.maesta.R;
import com.maesta.maesta.SubcatgoryActivity;
import com.maesta.maesta.utils.Config;
import com.maesta.maesta.utils.Utils;
import com.maesta.maesta.vo.Product;

import java.util.ArrayList;

/**
 * Created by vikesh.kumar on 7/19/2016.
 */
public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ProductHolder> {
    Context context;
    String categoryId;
    ArrayList<Product> list;

    public CategoriesAdapter(Context context, ArrayList<Product> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_home_category_item, parent, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductHolder holder, final int position) {
        holder.title.setText(list.get(position).title);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.get(position).haveSubCategories) {
                    Intent intent = new Intent(context, SubcatgoryActivity.class);
                    intent.putExtra("ID", (list.get(position).id));
                    intent.putExtra("HEADER_IMAGE", list.get(position).thumbURL);
                    intent.putExtra("TITLE", list.get(position).title);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, ListingActivity.class);
                    intent.putExtra("ID", (list.get(position).id));
                    intent.putExtra("TITLE", list.get(position).title);
                    context.startActivity(intent);
                }

            }
        });
        Glide.with(context).load(list.get(position).thumbURL).asBitmap()
                .placeholder(R.drawable.banner_1).centerCrop().into(holder.image);
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        TextView title, collection;
        ImageView image;

        public ProductHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.txt_title);
            collection = (TextView) itemView.findViewById(R.id.txt_collection);
            image = (ImageView) itemView.findViewById(R.id.img_product);
            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.txt_title), Config.MEDIUM);
            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.txt_collection), Config.REGULAR);
        }
    }
}
