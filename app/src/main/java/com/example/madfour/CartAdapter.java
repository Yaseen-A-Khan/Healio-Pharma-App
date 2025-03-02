package com.example.madfour;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<Medicine> cartItems;
    private Map<Medicine, Integer> cartQuantities;

    public CartAdapter(Map<Medicine, Integer> cartItems) {
        this.cartItems = new ArrayList<>(cartItems.keySet());
        this.cartQuantities = cartItems;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Medicine medicine = cartItems.get(position);
        holder.nameTextView.setText(medicine.getName());
        holder.descriptionTextView.setText(medicine.getDescription());
        holder.quantityTextView.setText(String.valueOf(cartQuantities.get(medicine)));
        Glide.with(holder.itemView.getContext())
                .load(medicine.getImageUrl())
                .into(holder.imageView);

        holder.incrementButton.setOnClickListener(v -> {
            CartManager.getInstance().addToCart(medicine);
            updateCartData();
            notifyItemChanged(position);
        });

        holder.decrementButton.setOnClickListener(v -> {
            CartManager.getInstance().removeFromCart(medicine);
            if (CartManager.getInstance().getCartItems().containsKey(medicine)) {
                updateCartData();
                notifyItemChanged(position);
            } else {
                cartItems.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, cartItems.size());
            }
        });
    }

    private void updateCartData() {
        cartQuantities.clear();
        cartQuantities.putAll(CartManager.getInstance().getCartItems());
        cartItems.clear();
        cartItems.addAll(cartQuantities.keySet());
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, descriptionTextView, quantityTextView;
        ImageView imageView;
        Button incrementButton, decrementButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.cart_item_name);
            descriptionTextView = itemView.findViewById(R.id.cart_item_description);
            quantityTextView = itemView.findViewById(R.id.cart_item_quantity);
            imageView = itemView.findViewById(R.id.cart_item_image);
            incrementButton = itemView.findViewById(R.id.increment_button);
            decrementButton = itemView.findViewById(R.id.decrement_button);
        }
    }
}