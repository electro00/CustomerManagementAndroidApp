package com.example.customermanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class CustomerListActivity extends AppCompatActivity {
    private static final String LOG_TAG = CustomerListActivity.class.getName();

    private FirebaseUser user;
    private FirebaseAuth mAuth;

    private RecyclerView mRecyclerView;
    private ArrayList<CustomerItem> mCustomerList;
    private ManagingCustomerAdapter mAdapter;

    private int gridNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mCustomerList = new ArrayList<>();

        mAdapter = new ManagingCustomerAdapter(this, mCustomerList);
        mRecyclerView.setAdapter(mAdapter);

        initializeData();
    }

    private void initializeData() {
        String[] customersList = getResources().getStringArray(R.array.customer_name);
        String[] boughtItemName = getResources().getStringArray(R.array.bought_item_name);
        String[] price = getResources().getStringArray(R.array.price);

        TypedArray customersImageResource = getResources().obtainTypedArray(R.array.customer_image);

        mCustomerList.clear();

        for (int i = 0; i < customersList.length; i++) {
            mCustomerList.add(new CustomerItem(customersList[i], boughtItemName[i], price[i], customersImageResource.getResourceId(i, 0)));
        }

        customersImageResource.recycle();

        mAdapter.notifyDataSetChanged();
    }
}