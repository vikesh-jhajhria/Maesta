package com.maesta.maesta;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.maesta.maesta.adapter.MyCollectionAdapter;
import com.maesta.maesta.utils.Config;
import com.maesta.maesta.utils.Utils;
import com.maesta.maesta.vo.CollectionVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saloni on 7/19/2016.
 */
public class MyCollectionActivity extends BaseActivity {
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
            applyFont();
            collectionAdapter = new MyCollectionAdapter(collection, this);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(collectionAdapter);
            findViewById(R.id.btn_place_order).setOnClickListener(this);

        }
    }

    private void setToolbar() {
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        getSupportActionBar().setTitle("My Collection");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void applyFont() {
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txtview_total), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txtview_total_price), Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (Button) findViewById(R.id.btn_place_order), Config.BOLD);


    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_place_order:
                startActivity(new Intent(getApplicationContext(),OrderHistoryActivity.class));
                break;
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }

        if(item.getItemId() == R.id.search){

            return true;
        }
        return false;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_profile, menu);
        return true;
    }


}
