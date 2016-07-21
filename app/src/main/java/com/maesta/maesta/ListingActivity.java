package com.maesta.maesta;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.GridView;
import android.widget.TextView;

import com.maesta.maesta.adapter.ListingAdapter;
import com.maesta.maesta.adapter.MyCollectionAdapter;
import com.maesta.maesta.utils.Config;
import com.maesta.maesta.utils.Utils;
import com.maesta.maesta.vo.CollectionVO;
import com.maesta.maesta.vo.ListingVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saloni.bhansali on 7/20/2016.
 */
public class ListingActivity extends BaseActivity
{
    private RecyclerView recyclerView;
    private List<ListingVO> productList;
    private ListingAdapter listingAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);
        setToolbar();
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        productList = new ArrayList<>();
        ListingVO productLists = new ListingVO();
        productLists.textTitile="Rounded sass'n Class series of Maseta italia's Eye Wear Section";
        productLists.textcollection="Rs.20,000";
        productList.add(productLists);
        productList.add(productLists);
        productList.add(productLists);
        productList.add(productLists);
        productList.add(productLists);
        productList.add(productLists);
        listingAdapter = new ListingAdapter(productList, this);
        final GridLayoutManager layoutManager = new GridLayoutManager(this,2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(listingAdapter);
    }
    private void setToolbar() {
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


}
