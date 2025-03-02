package com.example.madfour;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class OrdersAdapterDelivery extends RecyclerView.Adapter<OrdersAdapterDelivery.OrderViewHolder> {
    private List<Order> orders;
    private OnUpdateOrderStatusListener onUpdateOrderStatusListener;

    public interface OnUpdateOrderStatusListener {
        void onUpdateOrderStatus(Order order, String newStatus);
    }

    public OrdersAdapterDelivery(List<Order> orders, OnUpdateOrderStatusListener onUpdateOrderStatusListener) {
        this.orders = orders;
        this.onUpdateOrderStatusListener = onUpdateOrderStatusListener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_delivery, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.usernameTextView.setText(order.getUsername());
        holder.statusTextView.setText("Status : "+order.getStatus());
        holder.totalAmountTextView.setText("Collect Amount: Rs." + order.getTotalAmount());

        StringBuilder itemsStringBuilder = new StringBuilder();
        for (String item : order.getItems()) {
            itemsStringBuilder.append(item).append("\n");
        }
        holder.itemsTextView.setText(itemsStringBuilder.toString().trim());

        boolean isDelivered = "Delivered".equals(order.getStatus());
        if (isDelivered) {
            holder.statusSpinner.setVisibility(View.GONE);
            holder.updateStatusButton.setVisibility(View.GONE);
        } else {
            holder.statusSpinner.setVisibility(View.VISIBLE);
            holder.updateStatusButton.setVisibility(View.VISIBLE);

            // Populate spinner with status options
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    holder.itemView.getContext(),
                    R.array.order_status_array, // Status options from strings.xml
                    android.R.layout.simple_spinner_item
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.statusSpinner.setAdapter(adapter);

            holder.updateStatusButton.setOnClickListener(v -> {
                String newStatus = holder.statusSpinner.getSelectedItem().toString();
                onUpdateOrderStatusListener.onUpdateOrderStatus(order, newStatus);

                // Update the status locally and refresh the item
                order.setStatus(newStatus);
                notifyItemChanged(position);
            });
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView, statusTextView, itemsTextView,totalAmountTextView;
        Spinner statusSpinner;
        Button updateStatusButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.order_username);
            statusTextView = itemView.findViewById(R.id.order_status);
            itemsTextView = itemView.findViewById(R.id.order_items);
            statusSpinner = itemView.findViewById(R.id.status_spinner);
            totalAmountTextView = itemView.findViewById(R.id.order_total_amount);
            updateStatusButton = itemView.findViewById(R.id.update_status_button);
        }
    }
}
