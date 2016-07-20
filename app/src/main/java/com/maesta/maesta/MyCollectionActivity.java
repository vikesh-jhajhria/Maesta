package com.maesta.maesta;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.maesta.maesta.adapter.MyCollectionAdapter;
import com.maesta.maesta.vo.CollectionVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saloni on 7/19/2016.
 */
public class MyCollectionActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<CollectionVO> collection;
    private MyCollectionAdapter collectionAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_layout_recycleview);
        {
            recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
            collection = new ArrayList<>();
            CollectionVO collections = new CollectionVO();
            collections.product_name="Rounded sass'n Class series of Maseta italia's Eye Wear Section";
            collections.price="Rs.20,000";
            collections.quantity="Quantity";
            collections.quantity_number="20";

            collection.add(collections);
            collection.add(collections);
            collection.add(collections);
            collection.add(collections);
            collection.add(collections);
            collection.add(collections);
            setToolbar();
            collectionAdapter = new MyCollectionAdapter(collection, this);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(collectionAdapter);
        }
    }

    private void setToolbar() {
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        getSupportActionBar().setTitle("My Collection");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    }
