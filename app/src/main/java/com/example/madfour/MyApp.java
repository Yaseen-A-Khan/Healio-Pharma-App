package com.example.madfour;

import android.app.Application;
//import com.parse.Parse;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Parse
//        Parse.initialize(new Parse.Configuration.Builder(this)
//                .applicationId("YOUR_APP_ID") // Replace with your Back4App App ID
//                .clientKey("YOUR_CLIENT_KEY") // Replace with your Back4App Client Key
//                .server("https://parseapi.back4app.com/")
//                .build()
//        );
    }
}