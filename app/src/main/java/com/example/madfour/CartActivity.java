package com.example.madfour;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private Button checkoutButton, confirmOrderButton;
    private TextView emptyCartTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        cartRecyclerView = findViewById(R.id.cart_recycler_view);
        checkoutButton = findViewById(R.id.checkout_button);
        confirmOrderButton = findViewById(R.id.confirm_order_button);
        emptyCartTextView = findViewById(R.id.empty_cart_text_view);

        checkoutButton.setVisibility(View.INVISIBLE);
        Map<Medicine, Integer> cartItems = CartManager.getInstance().getCartItems();

        if (cartItems.isEmpty()) {
            emptyCartTextView.setVisibility(View.VISIBLE);
            cartRecyclerView.setVisibility(View.GONE);
            checkoutButton.setVisibility(View.GONE);
            confirmOrderButton.setVisibility(View.GONE);
        } else {
            emptyCartTextView.setVisibility(View.GONE);
            cartRecyclerView.setVisibility(View.VISIBLE);
            checkoutButton.setVisibility(View.INVISIBLE);
            confirmOrderButton.setVisibility(View.VISIBLE);

            cartAdapter = new CartAdapter(cartItems);
            cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            cartRecyclerView.setAdapter(cartAdapter);
        }

        checkoutButton.setOnClickListener(v -> {
            // Implement checkout logic here
        });

        confirmOrderButton.setOnClickListener(v -> confirmOrder());
    }

    private void confirmOrder() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Please log in to confirm your order", Toast.LENGTH_SHORT).show();
            return;
        }

        String username = currentUser.getUsername();
        Map<Medicine, Integer> cartItems = CartManager.getInstance().getCartItems();

        ParseHelper parseHelper = new ParseHelper();
        parseHelper.storeOrder(username, cartItems, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(CartActivity.this, "Order confirmed", Toast.LENGTH_SHORT).show();
                    CartManager.getInstance().clearCart();
                    Intent intent = new Intent(CartActivity.this, CustomerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(CartActivity.this, "Failed to confirm order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}