package com.example.madfour;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.parse.ParseObject;

public class MainActivity extends AppCompatActivity {
       @Override
       protected void onCreate(Bundle savedInstanceState) {
                  super.onCreate(savedInstanceState);
                  setContentView(R.layout.activity_main);
                  ParseObject firstObject = new  ParseObject("FirstClass");
                  firstObject.put("message","Hey ! First message from android. Parse is now connected");
                  firstObject.saveInBackground(e -> {
                         if (e != null){
                               Log.e("MainActivity", e.getLocalizedMessage());
                               }else{
                                  Log.d("MainActivity","Object saved.");
                               }
                        });
                 }
      }
