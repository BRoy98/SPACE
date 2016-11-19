package com.hometsolutions.space;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.hometsolutions.space.Activitys.SplashActivity;
import com.hometsolutions.space.Activitys.SplashActivityFirstLaunch;

public class LaunchActivity extends AppCompatActivity {

    Context context;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext());

        if (prefs.getBoolean("first_run", true)) {
            Intent intent = new Intent(this, SplashActivityFirstLaunch.class);
            startActivity(intent);
            this.finish();
        } else {
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
            this.finish();
        }
    }
}
