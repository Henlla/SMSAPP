package com.demo1.smsapp.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.Window;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import com.demo1.smsapp.R;
import com.demo1.smsapp.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding splashBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashBinding= ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(splashBinding.getRoot());
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.bluegra));
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, 2000);
    }
}