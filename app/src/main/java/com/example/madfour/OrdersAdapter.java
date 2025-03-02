package com.example.madfour;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {
    private List<Order> orders;
    private OnUpdateOrderStatusListener onUpdateOrderStatusListener;

    public interface OnUpdateOrderStatusListener {
        void onUpdateOrderStatus(Order order, String newStatus);
    }

    public OrdersAdapter(List<Order> orders, OnUpdateOrderStatusListener onUpdateOrderStatusListener) {
        this.orders = orders;
        this.onUpdateOrderStatusListener = onUpdateOrderStatusListener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_customer, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.usernameTextView.setText(order.getUsername());
        holder.statusTextView.setText("Status : "+order.getStatus());
        holder.totalAmountTextView.setText("Total: Rs." + order.getTotalAmount());

        StringBuilder itemsStringBuilder = new StringBuilder();
        for (String item : order.getItems()) {
            itemsStringBuilder.append(item).append("\n");
        }
        holder.itemsTextView.setText(itemsStringBuilder.toString().trim());

        if (onUpdateOrderStatusListener != null) {
            holder.updateStatusButton.setVisibility(View.VISIBLE);
            holder.updateStatusButton.setOnClickListener(v -> {
                // Simulate new status selection
                String newStatus = "Delivered"; // This can be replaced with a dialog or other UI element to select status
                onUpdateOrderStatusListener.onUpdateOrderStatus(order, newStatus);

                // Only update this item instead of refreshing all
                order.setStatus(newStatus);
                notifyItemChanged(position);
            });
        } else {
            holder.updateStatusButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView, statusTextView, itemsTextView,totalAmountTextView;
        Button updateStatusButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.order_username);
            statusTextView = itemView.findViewById(R.id.order_status);
            itemsTextView = itemView.findViewById(R.id.order_items);
            totalAmountTextView = itemView.findViewById(R.id.order_total_amount);
            updateStatusButton = itemView.findViewById(R.id.update_status_button);
        }
    }
}