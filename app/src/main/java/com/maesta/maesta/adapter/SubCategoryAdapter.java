package com.maesta.maesta.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maesta.maesta.R;
import com.maesta.maesta.vo.OrderHistoryVO;
import com.maesta.maesta.vo.SubCategoryVO;

import java.util.List;

/**
 * Created by saloni.bhansali on 4/21/2016.
 */
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



    }
    @Override
    public int getItemCount() {
        return  subcategory_list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder  {
        TextView   productName;

       // CardView product_detail_card;

        public ViewHolder(View itemView) {
            super(itemView);;
            productName    =   (TextView) itemView.findViewById(R.id.txt_product_name);

           // product_detail_card    =   (CardView) itemView.findViewById(R.id.product_detail_card);



        }
}

}
