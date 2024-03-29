package com.example.customermanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CustomerListActivity extends AppCompatActivity {
    private static final String LOG_TAG = CustomerListActivity.class.getName();

    private FirebaseUser user;
    private FirebaseAuth mAuth;

    private RecyclerView mRecyclerView;
    private ArrayList<CustomerItem> mCustomerList;
    private ManagingCustomerAdapter mAdapter;

    private FrameLayout redCircle;
    private TextView contentTextView;


    private FirebaseFirestore mFirestore;
    private CollectionReference mCustomers;

    private NotificationHandler mNotificationHandler;

    private int gridNumber = 1;
    private int favouritesItems = 0;
    private boolean viewRow = true;
    private int queryLimit = 10;

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

        mFirestore = FirebaseFirestore.getInstance();
        mCustomers = mFirestore.collection("Customers");

        queryData();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        this.registerReceiver(powerReceiver, filter);

        mNotificationHandler = new NotificationHandler(this);
    }

    BroadcastReceiver powerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action == null)
                return;

            switch (action) {
                case Intent.ACTION_POWER_CONNECTED:
                    queryLimit = 10;
                    break;
                case Intent.ACTION_POWER_DISCONNECTED:
                    queryLimit = 3;
                    break;
            }

            queryData();
        }
    };

    private void queryData() {
        mCustomerList.clear();

        //mCustomers.whereEqualTo()...

        mCustomers.orderBy("favouritedCount", Query.Direction.DESCENDING).limit(queryLimit).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                CustomerItem item = document.toObject(CustomerItem.class);
                item.setId(document.getId());
                mCustomerList.add(item);
            }

            if (mCustomerList.size() == 0) {
                initializeData();
                queryData();
            }

            mAdapter.notifyDataSetChanged();
        });

    }

    private void initializeData() {
        String[] customersList = getResources()
                .getStringArray(R.array.customer_name);
        String[] boughtItemName = getResources()
                .getStringArray(R.array.bought_item_name);
        String[] paymentMethod = getResources()
                .getStringArray(R.array.payment_method);
        String[] price = getResources()
                .getStringArray(R.array.price);

        TypedArray customersImageResource = getResources()
                .obtainTypedArray(R.array.customer_image);


        for (int i = 0; i < customersList.length; i++) {
            mCustomers.add(new CustomerItem(
                    customersList[i],
                    boughtItemName[i],
                    paymentMethod[i],
                    price[i],
                    customersImageResource.getResourceId(i, 0), 0));
        }

        customersImageResource.recycle();
    }

    public void deleteCustomer(CustomerItem item) {
        DocumentReference ref = mCustomers.document(item._getId());

        ref.delete().addOnSuccessListener(success -> {
            Log.d(LOG_TAG, "Customer successfully deleted: " + item._getId());
        })
        .addOnFailureListener(failure -> {
            Toast.makeText(this, "customer " + item._getId() + " cannot be deleted", Toast.LENGTH_LONG).show();
        });

        queryData();
        mNotificationHandler.cancel();

        //TODO 8.gyakvidi/40perc
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
            case R.id.favourites:
                Log.d(LOG_TAG, "Favourites clicked");
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
        final MenuItem alertMenuItem = menu.findItem(R.id.favourites);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();

        redCircle = (FrameLayout) rootView.findViewById(R.id.view_alert_red_circle);
        contentTextView = (TextView) rootView.findViewById(R.id.view_alert_count_textview);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(alertMenuItem);
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }

    public void updateAlertIcon(CustomerItem item) {
        favouritesItems = (favouritesItems + 1);
        if (0 < favouritesItems) {
            contentTextView.setText(String.valueOf(favouritesItems));
        } else {
            contentTextView.setText("");
        }

        redCircle.setVisibility((favouritesItems > 0) ? View.VISIBLE : View.GONE);

        mCustomers.document(item._getId()).update("favouritedCount", item.getFavouritedCount() + 1)
                .addOnFailureListener(failure -> {
                        Toast.makeText(this, "Customer " + item._getId() + " cannot be changed", Toast.LENGTH_LONG).show();
                });

        mNotificationHandler.send(item.getName());

        queryData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(powerReceiver);
    }
}