package com.example.madfour;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseUser;

import java.util.List;

public class OrdersFragment extends Fragment {

    private static final String ARG_USERNAME = "username";
    private static final String TAG = "OrdersFragment";
    private String username;
    private RecyclerView ordersRecyclerView;
    private OrdersAdapter ordersAdapter;
    private ParseHelper parseHelper;

    public static OrdersFragment newInstance(String username) {
        OrdersFragment fragment = new OrdersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        ordersRecyclerView = view.findViewById(R.id.orders_recycler_view);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        parseHelper = new ParseHelper();

        fetchOrders();

        return view;
    }

    private void fetchOrders() {
        new Thread(() -> {
            try {
                ParseUser user = ParseUser.getCurrentUser();

                List<Order> orders = parseHelper.getOrdersByUsername(user.getUsername());
                Log.d(TAG, "Fetched orders: " + orders.size()); // Debugging log
                getActivity().runOnUiThread(() -> {
                    if (orders != null && !orders.isEmpty()) {
                        ordersAdapter = new OrdersAdapter(orders, null);
                        ordersRecyclerView.setAdapter(ordersAdapter);
                        Log.d(TAG, "Adapter set with orders"); // Debugging log
                    } else {
                        Log.d(TAG, "No orders found");
                        Toast.makeText(getContext(), "No orders found", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Failed to fetch orders!", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
}