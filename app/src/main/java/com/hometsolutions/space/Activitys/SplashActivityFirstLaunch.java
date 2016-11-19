package com.hometsolutions.space.Activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.viewanimator.AnimationBuilder;
import com.github.florent37.viewanimator.ViewAnimator;
import com.hometsolutions.space.R;

public class SplashActivityFirstLaunch extends AppCompatActivity {

    public Context context;
    private static int SPLASH_TIME_OUT = 7000;
    private ImageView logo;
    private ImageView name;
    private TextView love;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_first_launch);
        logo = (ImageView) findViewById(R.id.logoView);
        name  =(ImageView) findViewById(R.id.nameView);
        love = (TextView) findViewById(R.id.loveText);
        name.setVisibility(View.GONE);
        logo.setVisibility(View.GONE);
        love.setVisibility(View.GONE);

        final AnimationBuilder[] builder = new AnimationBuilder[1];
        final AnimationBuilder[] builder1 = new AnimationBuilder[1];
        final AnimationBuilder[] builder2 = new AnimationBuilder[1];

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                logo.setVisibility(View.VISIBLE);
                builder[0] = ViewAnimator.animate(logo).fadeIn().duration(700);
                builder[0].start();
            }
        }, 300);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                name.setVisibility(View.VISIBLE);
                builder1[0] = ViewAnimator.animate(name).fadeIn().duration(700);
                builder1[0].start();
            }
        }, 1500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                love.setVisibility(View.VISIBLE);
                builder2[0] = ViewAnimator.animate(love).fadeIn().duration(700);
                builder2[0].start();
            }
        }, 2500);
        context = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext());
        prefs.edit().putBoolean("first_run", false).apply();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(SplashActivityFirstLaunch.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onBackPressed() {
    }
}
