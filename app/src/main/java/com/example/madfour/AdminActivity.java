package com.example.madfour;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private RecyclerView generalMedicinesRecyclerView, prescriptionMedicinesRecyclerView;
    private Button addGeneralMedicineButton, addPrescriptionMedicineButton, logoutButton;
    private MedicineAdapterAdmin generalMedicineAdapter, prescriptionMedicineAdapter;
    private ParseHelper parseHelper;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        generalMedicinesRecyclerView = findViewById(R.id.general_medicines_recycler_view);
        prescriptionMedicinesRecyclerView = findViewById(R.id.prescription_medicines_recycler_view);
        addGeneralMedicineButton = findViewById(R.id.add_general_medicine_button);
        addPrescriptionMedicineButton = findViewById(R.id.add_prescription_medicine_button);
        logoutButton = findViewById(R.id.logout_button);
        progressBar = findViewById(R.id.progress_bar);

        parseHelper = new ParseHelper();

        generalMedicineAdapter = new MedicineAdapterAdmin(new ArrayList<>(), null, this::deleteMedicine);
        prescriptionMedicineAdapter = new MedicineAdapterAdmin(new ArrayList<>(), null, this::deleteMedicine);

        generalMedicinesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        generalMedicinesRecyclerView.setAdapter(generalMedicineAdapter);

        prescriptionMedicinesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        prescriptionMedicinesRecyclerView.setAdapter(prescriptionMedicineAdapter);

        fetchMedicines();

        addGeneralMedicineButton.setOnClickListener(v -> addMedicine("general"));
        addPrescriptionMedicineButton.setOnClickListener(v -> addMedicine("prescription"));

        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void fetchMedicines() {
        progressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {
            List<MedicineAdmin> generalMedicines = null;
            try {
                generalMedicines = parseHelper.getMedicinesByCategorys("general");
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            List<MedicineAdmin> prescriptionMedicines = null;
            try {
                prescriptionMedicines = parseHelper.getMedicinesByCategorys("prescription");
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            List<MedicineAdmin> finalGeneralMedicines = generalMedicines;
            List<MedicineAdmin> finalPrescriptionMedicines = prescriptionMedicines;
            runOnUiThread(() -> {
                generalMedicineAdapter.updateMedicines(finalGeneralMedicines);
                prescriptionMedicineAdapter.updateMedicines(finalPrescriptionMedicines);
                progressBar.setVisibility(View.GONE);
            });
        }).start();
    }

    private void addMedicine(String category) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Medicine");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_add_medicine, (ViewGroup) findViewById(android.R.id.content), false);
        final EditText nameEditText = viewInflated.findViewById(R.id.medicine_name);
        final EditText descriptionEditText = viewInflated.findViewById(R.id.medicine_description);
        final EditText imageUrlEditText = viewInflated.findViewById(R.id.medicine_image_url);
        final EditText priceEditText = viewInflated.findViewById(R.id.medicine_price);

        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            dialog.dismiss();
            String name = nameEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            String imageUrl = imageUrlEditText.getText().toString();
            String price = priceEditText.getText().toString();
            if (name.isEmpty() || description.isEmpty() || imageUrl.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            parseHelper.addMedicine(name, description, category, imageUrl,price, new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        fetchMedicines();
                    } else {
                        Toast.makeText(AdminActivity.this, "Failed to add medicine: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void deleteMedicine(MedicineAdmin medicine) {
        parseHelper.deleteMedicine(medicine.getId(), new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    fetchMedicines();
                    Toast.makeText(AdminActivity.this, "Medicine deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AdminActivity.this, "Failed to delete medicine: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}