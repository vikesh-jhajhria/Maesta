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
import com.maesta.maesta.vo.ListingVO;

import java.util.List;

/**
 * Created by saloni.bhansali on 4/21/2016.
 */
public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ViewHolder> {
    Context context;

    List<ListingVO> listing;
    String title;

    public ListingAdapter(List<ListingVO> listingList, Context context, String title) {
        this.context = context;
        this.listing = listingList;
        this.title = title;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_listing_category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ListingVO productlist = listing.get(position);
        holder.product_name.setText(productlist.textTitile);
        holder.price.setText(productlist.price);
        Glide.with(context).load(productlist.thumbURL).asBitmap()
                .placeholder(R.drawable.default_image).fitCenter().into(holder.product_img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("ID", productlist.id);
                intent.putExtra("TITLE", title);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return listing.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView product_name, price;
        ImageView product_img;
        // CardView product_detail_card;

        public ViewHolder(View itemView) {
            super(itemView);
            product_name = (TextView) itemView.findViewById(R.id.txt_title);
            price = (TextView) itemView.findViewById(R.id.txt_collection);
            product_img = (ImageView) itemView.findViewById(R.id.img_product);
            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.txt_title), Config.MEDIUM);

            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.txt_collection), Config.BOLD);

            // product_detail_card    =   (CardView) itemView.findViewById(R.id.product_detail_card);


        }
    }

}
