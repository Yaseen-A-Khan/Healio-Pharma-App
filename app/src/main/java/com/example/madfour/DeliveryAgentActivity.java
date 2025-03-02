package com.example.madfour;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import java.util.List;

public class DeliveryAgentActivity extends AppCompatActivity {

    private static final String TAG = "DeliveryAgentActivity";
    private RecyclerView ordersRecyclerView;
    private OrdersAdapterDelivery ordersAdapter;
    private ParseHelper parseHelper;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_agent);

        ordersRecyclerView = findViewById(R.id.orders_recycler_view);
        logoutButton = findViewById(R.id.logout_button);
        parseHelper = new ParseHelper();

        fetchOrders();

        logoutButton.setOnClickListener(v -> {
            ParseUser.logOut();
            Intent intent = new Intent(DeliveryAgentActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void fetchOrders() {
        new Thread(() -> {
            try {
                List<Order> orders = parseHelper.getNonDeliveredOrders();
                Log.d(TAG, "Fetched orders: " + orders.size());
                runOnUiThread(() -> {
                    ordersAdapter = new OrdersAdapterDelivery(orders, (order, newStatus) -> {
                        parseHelper.updateOrderStatus(order.getId(), newStatus, new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    order.setStatus(newStatus);
                                    ordersAdapter.notifyDataSetChanged();
                                } else {
                                    e.printStackTrace();
                                }
                            }
                        });
                    });
                    ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                    ordersRecyclerView.setAdapter(ordersAdapter);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
