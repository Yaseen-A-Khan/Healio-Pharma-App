package com.example.madfour;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.parse.ParseUser;

public class ProfileFragment extends Fragment {

    private static final String ARG_USERNAME = "username";
    private String username;
    private TextView usernameTextView, emailTextView, phoneTextView, addressTextView;
    private Button logoutButton;
    private ParseHelper parseHelper;

    public static ProfileFragment newInstance(String username) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        usernameTextView = view.findViewById(R.id.profile_username);
        emailTextView = view.findViewById(R.id.profile_email);
        phoneTextView = view.findViewById(R.id.profile_phone);
        addressTextView = view.findViewById(R.id.profile_address);
        logoutButton = view.findViewById(R.id.logout_button);

        parseHelper = new ParseHelper();

        ParseUser user = ParseUser.getCurrentUser();
        if (user != null) {
            usernameTextView.setText("Username : "+user.getUsername());
            emailTextView.setText("Email : "+user.getEmail());
            phoneTextView.setText("Phone No. : "+user.getString("phone"));
            addressTextView.setText("Address : " +user.getString("address"));
        }

        logoutButton.setOnClickListener(v -> {
            ParseUser.logOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return view;
    }
}