package com.serosoft.academiassu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;
import com.serosoft.academiassu.Modules.Login.LoginActivity;
import com.serosoft.academiassu.Manager.SharedPrefrenceManager;
import com.serosoft.academiassu.Helpers.Consts;

public class SplashActivity extends AppCompatActivity
{
    Context mContext;
    SharedPrefrenceManager sharedPrefrenceManager;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        mContext = SplashActivity.this;

        sharedPrefrenceManager = new SharedPrefrenceManager(SplashActivity.this);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        handler.postDelayed(runnable, Consts.SPLASH_TIME_OUT);
    }

    Runnable runnable = new Runnable()
    {
        @Override
        public void run()
        {
            if (sharedPrefrenceManager.getUserLoginStatusFromKey())
            {
                if(sharedPrefrenceManager.getIsFacultyStatusFromKey()){
                    Intent intent = new Intent(mContext, FacultyMainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }

            } else {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        handler.removeCallbacks(runnable);
        SplashActivity.this.finish();
    }
}