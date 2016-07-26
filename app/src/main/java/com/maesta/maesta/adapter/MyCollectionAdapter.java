package com.maesta.maesta.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maesta.maesta.AboutusActivity;
import com.maesta.maesta.BaseActivity;
import com.maesta.maesta.MyCollectionActivity;
import com.maesta.maesta.R;
import com.maesta.maesta.utils.AppPreferences;
import com.maesta.maesta.utils.Config;
import com.maesta.maesta.utils.HTTPUrlConnection;
import com.maesta.maesta.utils.Utils;
import com.maesta.maesta.vo.Collection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by saloni.bhansali on 4/21/2016.
 */
public class MyCollectionAdapter extends RecyclerView.Adapter<MyCollectionAdapter.ViewHolder> {
    Context context;
    AppPreferences mPrefs;
    List<Collection> collection;
    String quantityNo;

    public MyCollectionAdapter(List<Collection> collectionlist, Context context) {
        this.context = context;
        this.collection = collectionlist;
        mPrefs = AppPreferences.getAppPreferences(this.context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_collection, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Collection collections = collection.get(position);
        holder.product_name.setText(collections.product_name);
        holder.quantityno.setText(collections.quantity_number);
        holder.quantity.setText(collections.quantity);
        holder.price.setText(collections.price);

        holder.quantityno.addTextChangedListener(new updateValidation(holder, position));

        Glide.with(context).load(collections.thumbURL).asBitmap()
                .placeholder(R.drawable.banner_1).centerCrop().into(holder.productimg);
        holder.txtview_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RemoveOrderTask().execute(collections.id + "", position + "");
            }
        });
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UpdateCollectionTask().execute(collections.id + "", position + "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return collection.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView product_name, quantity, price, txtview_remove, update;
        ImageView productimg;
        EditText quantityno;
        CardView product_detail_card;


        public ViewHolder(View itemView) {
            super(itemView);

            productimg = (ImageView) itemView.findViewById(R.id.img_1);
            product_name = (TextView) itemView.findViewById(R.id.txtview_product_name);
            quantity = (TextView) itemView.findViewById(R.id.txtview_quantity);
            price = (TextView) itemView.findViewById(R.id.txt_view_price);
            quantityno = (EditText) itemView.findViewById(R.id.et_quantity_number);
            txtview_remove = (TextView) itemView.findViewById(R.id.remove_txtview);
            update = (TextView) itemView.findViewById(R.id.txtview_update);
            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.txtview_product_name), Config.BOLD);
            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.txtview_quantity), Config.BOLD);
            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.txt_view_price), Config.BOLD);
            Utils.setTypeface(context, (EditText) itemView.findViewById(R.id.et_quantity_number), Config.MEDIUM);
            product_detail_card = (CardView) itemView.findViewById(R.id.product_detail_card);


        }
    }

    /*Remove Order Async Task*/
    class RemoveOrderTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;
        int index;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((BaseActivity) context).showProgessDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String customerId = AppPreferences.getAppPreferences(context)
                    .getStringValue(AppPreferences.USER_ID);


            index = Integer.parseInt(params[1]);
            postDataParams = new HashMap<String, String>();
            String apikey = mPrefs.getStringValue(AppPreferences.API_KEY);
            postDataParams.put("api_key", apikey);
            postDataParams.put("collection_id", params[0]);
            postDataParams.put("customer_id", customerId);

            return HTTPUrlConnection.getInstance().load(Config.REMOVE_COLLECTION, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ((BaseActivity) context).dismissProgressDialog();

            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    ((MyCollectionActivity)context).resetTotal(object.getString("total_amount"));
                    collection.remove(index);
                    notifyDataSetChanged();

                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_LONG).show();

                }else if (object.getString("apistatus").equalsIgnoreCase("API rejection")) {
                    Utils.resetLogin((BaseActivity)context);
                }
                else {
                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }

    class updateValidation implements TextWatcher {

        int position;
        ViewHolder holder;

        public updateValidation(ViewHolder holder, int position) {

            this.position = position;
            this.holder = holder;

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {

            switch (holder.quantityno.getId()) {

                case R.id.et_quantity_number:
                    try {
                        quantityNo = ((EditText) holder.quantityno).getText().toString().trim();
                        String oldQuantity = collection.get(position).quantity_number;
                        if (quantityNo.equalsIgnoreCase(oldQuantity)) {
                            holder.update.setVisibility(View.GONE);
                            break;
                        } else if (quantityNo.equalsIgnoreCase("0")||quantityNo.isEmpty()) {
                            holder.update.setVisibility(View.GONE);
                            break;
                        } else {
                            holder.update.setVisibility(View.VISIBLE);

                            break;
                        }
                    }catch (Exception e){

                    }
            }
        }
    }

    class UpdateCollectionTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;
        int index;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //((BaseActivity) context).showProgessDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String customerId = AppPreferences.getAppPreferences(context)
                    .getStringValue(AppPreferences.USER_ID);

            postDataParams = new HashMap<String, String>();
            String apikey = mPrefs.getStringValue(AppPreferences.API_KEY);
            index = Integer.parseInt(params[1]);
            postDataParams.put("api_key", apikey);
            postDataParams.put("quantity", quantityNo);
            postDataParams.put("collection_id", params[0]);
            postDataParams.put("customer_id", customerId);

            return HTTPUrlConnection.getInstance().load(Config.UPDATE_COLLECTION, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //((BaseActivity) context).dismissProgressDialog();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    ((MyCollectionActivity)context).resetTotal(object.getString("total_amount"));
                    collection.get(index).quantity_number=quantityNo;
                    notifyItemChanged(index);
                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_LONG).show();
                }else if (object.getString("apistatus").equalsIgnoreCase("API rejection")) {
                    Utils.resetLogin((BaseActivity)context);
                }
                else {
                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

}
