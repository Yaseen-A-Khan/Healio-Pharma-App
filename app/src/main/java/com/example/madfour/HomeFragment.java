package com.example.madfour;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String ARG_USERNAME = "username";
    private String username;
    private RecyclerView generalMedicinesRecyclerView, prescriptionMedicinesRecyclerView;
    private MedicineAdapter generalMedicineAdapter, prescriptionMedicineAdapter;
    private ParseHelper parseHelper;
    private Button viewCartButton;

    public static HomeFragment newInstance(String username) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        generalMedicinesRecyclerView = view.findViewById(R.id.general_medicines_recycler_view);
        prescriptionMedicinesRecyclerView = view.findViewById(R.id.prescription_medicines_recycler_view);
        viewCartButton = view.findViewById(R.id.view_cart_button);

        parseHelper = new ParseHelper();

        generalMedicineAdapter = new MedicineAdapter(new ArrayList<>(), this::addToCart);
        prescriptionMedicineAdapter = new MedicineAdapter(new ArrayList<>(), this::addToCart);

        generalMedicinesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        generalMedicinesRecyclerView.setAdapter(generalMedicineAdapter);

        prescriptionMedicinesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        prescriptionMedicinesRecyclerView.setAdapter(prescriptionMedicineAdapter);

        fetchMedicines();

        viewCartButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CartActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void fetchMedicines() {
        new Thread(() -> {
            List<Medicine> generalMedicines = parseHelper.getMedicinesByCategory("general");
            List<Medicine> prescriptionMedicines = parseHelper.getMedicinesByCategory("prescription");

            getActivity().runOnUiThread(() -> {
                generalMedicineAdapter.updateMedicines(generalMedicines);
                prescriptionMedicineAdapter.updateMedicines(prescriptionMedicines);
            });
        }).start();
    }

    private void addToCart(Medicine medicine) {
        CartManager.getInstance().addToCart(medicine);
        Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
    }
}