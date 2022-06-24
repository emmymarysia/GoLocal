package com.example.golocal;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Business.class);
        ParseObject.registerSubclass(Guide.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("E9AG7GyHCR7Avg2gg31nbWPhm2DDlCTJuNOoT46a")
                .clientKey("3Z3sR6qwiAAZeuy3G9KPtuKF2n6Wk4McGhkMKAOK")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}