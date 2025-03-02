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

public class MedicineAdapterAdmin extends RecyclerView.Adapter<MedicineAdapterAdmin.MedicineViewHolder> {
    private List<MedicineAdmin> medicines;
    //private OnAddToCartListener onAddToCartListener;
    private OnDeleteMedicineListener onDeleteMedicineListener;

    public interface OnAddToCartListener {
        void onAddToCart(MedicineAdmin medicine);
    }

    public interface OnDeleteMedicineListener {
        void onDeleteMedicine(MedicineAdmin medicine);
    }

    public MedicineAdapterAdmin(List<MedicineAdmin> medicines, OnAddToCartListener onAddToCartListener, OnDeleteMedicineListener onDeleteMedicineListener) {
        this.medicines = medicines;
        //this.onAddToCartListener = onAddToCartListener;
        this.onDeleteMedicineListener = onDeleteMedicineListener;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medicine_admin, parent, false);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        MedicineAdmin medicine = medicines.get(position);
        holder.nameTextView.setText(medicine.getName());
        holder.descriptionTextView.setText(medicine.getDescription());
        holder.priceTextView.setText("Rs."+medicine.getPrice());
        Glide.with(holder.itemView.getContext())
                .load(medicine.getImageUrl())
                .into(holder.imageView);
        //holder.addToCartButton.setOnClickListener(v -> onAddToCartListener.onAddToCart(medicine));
        holder.deleteItemButton.setOnClickListener(v -> onDeleteMedicineListener.onDeleteMedicine(medicine));
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    public void updateMedicines(List<MedicineAdmin> newMedicines) {
        medicines.clear();
        medicines.addAll(newMedicines);
        notifyDataSetChanged();
    }

    static class MedicineViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, descriptionTextView,priceTextView;
        ImageView imageView;
        Button addToCartButton, deleteItemButton;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.medicine_name);
            descriptionTextView = itemView.findViewById(R.id.medicine_description);
            imageView = itemView.findViewById(R.id.medicine_image);
            priceTextView=itemView.findViewById(R.id.medicine_price);
            //addToCartButton = itemView.findViewById(R.id.add_to_cart_button);
            deleteItemButton = itemView.findViewById(R.id.delete_item_button);
        }
    }
}