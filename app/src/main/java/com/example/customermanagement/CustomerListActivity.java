package com.example.customermanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

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
    private boolean viewRow = true;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.customer_list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(LOG_TAG, newText);
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_button:
                Log.d(LOG_TAG, "Logout clicked");
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            case R.id.setting_button:
                Log.d(LOG_TAG, "Setting clicked");
                return true;
            case R.id.view_selector:
                Log.d(LOG_TAG, "ViewSelector clicked");
                if (viewRow) {
                    changeSpanCount(item, R.drawable.ic_grid_view, 1);
                } else {
                    changeSpanCount(item, R.drawable.ic_row_view, 2);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void changeSpanCount(MenuItem item, int drawableID, int spanCount) {
        viewRow = !viewRow;
        item.setIcon(drawableID);
        GridLayoutManager layoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
        layoutManager.setSpanCount(spanCount);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
}