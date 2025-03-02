package com.example.madfour;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;
    private Spinner roleSpinner;
    private Button loginButton, registerButton;
    private ParseHelper parseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        roleSpinner = findViewById(R.id.role_spinner);
        loginButton = findViewById(R.id.login_button);
        registerButton = findViewById(R.id.register_button);
        parseHelper = new ParseHelper();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        loginButton.setOnClickListener(v -> login());
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void login() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String role = roleSpinner.getSelectedItem().toString();

        if (parseHelper.authenticateUser(username, password, role)) {
            switch (role) {
                case "Customer":
                    startActivity(new Intent(LoginActivity.this, CustomerActivity.class));
                    break;
                case "Admin":
                    startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                    break;
                case "Delivery Agent":
                    startActivity(new Intent(LoginActivity.this, DeliveryAgentActivity.class));
                    break;
            }
        } else {
            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    }
}