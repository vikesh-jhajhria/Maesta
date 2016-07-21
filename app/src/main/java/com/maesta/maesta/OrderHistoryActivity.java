package com.maesta.maesta;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.maesta.maesta.adapter.OrderHistoryAdapter;
import com.maesta.maesta.adapter.OrderHistoryDetailAdapter;
import com.maesta.maesta.utils.Config;
import com.maesta.maesta.utils.Utils;
import com.maesta.maesta.vo.OrderHistoryDetailVO;
import com.maesta.maesta.vo.OrderHistoryVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saloni on 7/19/2016.
 */
public class OrderHistoryActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private List<OrderHistoryVO> orderhistory;
    private OrderHistoryAdapter orderAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recycleview);
        {

            recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
            orderhistory = new ArrayList<>();
            OrderHistoryVO collections = new OrderHistoryVO();
            collections.date="Placed on 2 July,2016";
            collections.price="Rs.20,000.00";
            collections.order="Order ID";
            collections.orderId="+98E6F6";
            collections.  status=" Status:";
            collections. pending="pending";
            collections. total="Total";
            collections.orderId="+98E6F6";
            OrderHistoryVO collection = new OrderHistoryVO();
            collection.date="Placed on 2 July,2016";
            collection.price="Rs.20,000.00";
            collection.order="Order ID";
            collection.orderId="+98E6F6";
            collection.  status=" Status:";
            collection. pending="status";
            collection. total="Total";
            collection.orderId="+98E6F6";

            orderhistory.add(collections);
            orderhistory.add(collection);
            orderhistory.add(collections);
            orderhistory.add(collections);

            orderhistory.add(collection);
            orderhistory.add(collection);
            setToolbar();

            orderAdapter = new OrderHistoryAdapter(orderhistory, this);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(orderAdapter);
        }
    }

    private void setToolbar() {
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        getSupportActionBar().setTitle("Order History");
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
