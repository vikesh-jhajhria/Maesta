package com.maesta.maesta.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.maesta.maesta.R;
import com.maesta.maesta.vo.CollectionVO;

import java.util.List;

/**
 * Created by saloni.bhansali on 4/21/2016.
 */
public class MyCollectionAdapter extends RecyclerView.Adapter<MyCollectionAdapter.ViewHolder> {
    Context context;

    List<CollectionVO> collection;
    public MyCollectionAdapter(List<CollectionVO>  collection, Context context){
        this.context=context;
        this.collection=collection;
    }
    @Override
    public  ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_my_collection,parent,false);
    return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CollectionVO election = collection .get(position);
        holder.product_name.setText(election.product_name );
        holder.quantityno.setText(election. quantity_number );
        holder.quantity.setText(election.quantity );
        holder.price.setText(election.price );

    }
    @Override
    public int getItemCount() {
        return  collection.size();
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
