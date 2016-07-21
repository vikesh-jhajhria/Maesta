package com.maesta.maesta;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.maesta.maesta.adapter.OrderHistoryDetailAdapter;
import com.maesta.maesta.vo.OrderHistoryDetailVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saloni on 7/19/2016.
 */
public class OrderHistoryDetailActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private List<OrderHistoryDetailVO> orderdetail;
    private OrderHistoryDetailAdapter orderAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recycleview);
        {
            recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
            orderdetail = new ArrayList<>();
            OrderHistoryDetailVO collections = new OrderHistoryDetailVO();
            collections.product_name="Rounded sass'n Class series of Maseta italia's Eye Wear Section";
            collections.price="Rs.20,000.00";
            collections.quantity="Quantity";
            collections.quantity_number="20";

            orderdetail.add(collections);
            orderdetail.add(collections);
            orderdetail.add(collections);
            orderdetail.add(collections);
            orderdetail.add(collections);
            orderdetail.add(collections);
            setToolbar();
            orderAdapter = new OrderHistoryDetailAdapter(orderdetail, this);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(orderAdapter);
        }
    }

    private void setToolbar() {
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        getSupportActionBar().setTitle("Order History Detail");
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
