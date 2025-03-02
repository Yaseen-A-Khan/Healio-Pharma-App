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
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {
    private List<Medicine> medicines;
    private OnAddToCartListener onAddToCartListener;

    public interface OnAddToCartListener {
        void onAddToCart(Medicine medicine);
    }

    public MedicineAdapter(List<Medicine> medicines, OnAddToCartListener onAddToCartListener) {
        this.medicines = medicines;
        this.onAddToCartListener = onAddToCartListener;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medicine, parent, false);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine medicine = medicines.get(position);
        holder.nameTextView.setText(medicine.getName());
        holder.priceTextView.setText("Rs."+medicine.getPrice());
        holder.descriptionTextView.setText(medicine.getDescription());
        Glide.with(holder.itemView.getContext())
                .load(medicine.getImageUrl())
                .into(holder.imageView);
        holder.addToCartButton.setOnClickListener(v -> onAddToCartListener.onAddToCart(medicine));
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    public void updateMedicines(List<Medicine> newMedicines) {
        medicines.clear();
        medicines.addAll(newMedicines);
        notifyDataSetChanged();
    }

    static class MedicineViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, descriptionTextView, priceTextView;
        ImageView imageView;
        Button addToCartButton;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.medicine_name);
            descriptionTextView = itemView.findViewById(R.id.medicine_description);
            imageView = itemView.findViewById(R.id.medicine_image);
            priceTextView=itemView.findViewById(R.id.medicine_price);
            addToCartButton = itemView.findViewById(R.id.delete_item_button);
        }
    }
}