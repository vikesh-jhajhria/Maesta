package com.maesta.maesta;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.maesta.maesta.adapter.SubCategoryAdapter;
import com.maesta.maesta.vo.SubCategoryVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saloni on 7/19/2016.
 */
public class SubcatgoryActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private List<SubCategoryVO> subcat;
    private SubCategoryAdapter subcategoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_subcatogary);
        {
            recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
            subcat = new ArrayList<>();
            SubCategoryVO collections = new SubCategoryVO ();
            collections.productName ="Rounded sass'n ";


            subcat.add(collections);
            subcat.add(collections);
            subcat.add(collections);
            subcat.add(collections);
            subcat.add(collections);
            subcat.add(collections);
            subcat.add(collections);
            subcat.add(collections);
            subcat.add(collections);
            subcat.add(collections);
            subcat.add(collections);
            subcat.add(collections);

            setToolbar();
            subcategoryAdapter = new SubCategoryAdapter(subcat, this);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(subcategoryAdapter);
        }
    }

    private void setToolbar() {
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }

        return false;
    }
    }
