package com.maesta.maesta.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maesta.maesta.ListingActivity;
import com.maesta.maesta.ProductDetailActivity;
import com.maesta.maesta.R;

import com.maesta.maesta.utils.Config;
import com.maesta.maesta.utils.Utils;
import com.maesta.maesta.vo.SubCategoryVO;

import java.util.List;


public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {
    Context context;

    List<SubCategoryVO> subcategory_list;
    public SubCategoryAdapter(List<SubCategoryVO>   subcat, Context context){
        this.context=context;
        this.subcategory_list=subcat;
    }
    @Override
    public  ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_product_subcategary,parent,false);
    return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SubCategoryVO subcategory = subcategory_list.get(position);
        holder.productName.setText(subcategory.productName);
holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context, ListingActivity.class);
        context.startActivity(intent);
    }
});


    }
    @Override
    public int getItemCount() {
        return  subcategory_list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder  {
        TextView   productName;
RelativeLayout relativeLayout;
       // CardView product_detail_card;

        public ViewHolder(View itemView) {
            super(itemView);
            productName    =   (TextView) itemView.findViewById(R.id.txt_product_name);
            relativeLayout=(RelativeLayout)itemView.findViewById(R.id.rl1);
            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.txt_product_name), Config.MEDIUM);


            // product_detail_card    =   (CardView) itemView.findViewById(R.id.product_detail_card);



        }
}

}
