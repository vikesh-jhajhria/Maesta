package com.maesta.maesta.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maesta.maesta.R;
import com.maesta.maesta.utils.Config;
import com.maesta.maesta.utils.Utils;
import com.maesta.maesta.vo.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SubCategoryExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private ArrayList<Product> parentItems, child;
    private ArrayList<Object> childItems;

    private LayoutInflater mLayoutInflater;

    public SubCategoryExpandableListAdapter(Context context, ArrayList<Product> expandableListTitle,
                                            ArrayList<Object> expandableListDetail) {
        mContext = context;
        parentItems = expandableListTitle;
        childItems = expandableListDetail;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        child = (ArrayList<Product>) childItems.get(groupPosition);

        TextView textView = null;

        if (convertView == null) {
            convertView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.list_item, null);
        }

        textView = (TextView) convertView.findViewById(R.id.expandedListItem);
        textView.setText(child.get(childPosition).title);
        Utils.setTypeface(mContext, textView, Config.REGULAR);


        return convertView;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.item_product_subcategary, null);
        }
        /*convertView.findViewById(R.id.line_bottom).setVisibility(View.INVISIBLE);
        if(groupPosition == 0){
            convertView.findViewById(R.id.line_top).setVisibility(View.INVISIBLE);
        }
        if(groupPosition == parentItems.size()-1){
            convertView.findViewById(R.id.line_bottom).setVisibility(View.VISIBLE);
        }*/
        TextView txt = ((TextView) convertView.findViewById(R.id.txt_product_name));
        txt.setText(parentItems.get(groupPosition).title);
        Utils.setTypeface(mContext, txt, Config.BOLD);
        Glide.with(mContext).load(parentItems.get(groupPosition).iconURL).asBitmap()
                .placeholder(R.drawable.banner_1).centerCrop()
                .into((CircleImageView) convertView.findViewById(R.id.img_user_icon));
        final View finalConvertView = convertView;
        if (getChildrenCount(groupPosition) == 0)
            ((ImageView) finalConvertView.findViewById(R.id.img_collapse)).setVisibility(View.INVISIBLE);
        else
            ((ImageView) finalConvertView.findViewById(R.id.img_collapse)).setVisibility(View.VISIBLE);

        /*convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getChildrenCount(groupPosition) > 0){
                    ((ImageView) finalConvertView.findViewById(R.id.img_collapse)).setImageResource(R.drawable.subcatory_down);

                }
            }
        });*/
        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return ((ArrayList<Product>) childItems.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return ((ArrayList<Product>) childItems.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parentItems.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return parentItems.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
