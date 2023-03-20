package com.demo1.smsapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.demo1.smsapp.R;
import com.demo1.smsapp.adapter.ViewPagerAdapter;
import com.demo1.smsapp.databinding.ActivityHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding homeBinding;
    String accountJson;
    String profileJson;
    String dataJson;
    String _token;
    String role;
    MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(homeBinding.getRoot());
        SharedPreferences data = getSharedPreferences("informationAccount", MODE_PRIVATE);
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.red));
        accountJson = data.getString("account", null);
        _token = data.getString("token", null);
        profileJson = data.getString("profile", null);
        dataJson = data.getString("data", null);
        role = data.getString("role", null);
        setUpViewPager();
        setupNavigationBottom();
    }

    public String getRole() {
        return role;
    }

    private void checkToken() {
        String newToken = _token.substring(7, _token.length());
        DecodedJWT jwt = JWT.decode(newToken);
        if (jwt.getExpiresAt().before(new Date())) {
            materialDialog = new MaterialDialog.Builder(HomeActivity.this)
                    .setMessage("Hết phiên đăng nhập ! Vui lòng đăng nhập lại")
                    .setCancelable(false)
                    .setPositiveButton("", R.drawable.done, new MaterialDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            SharedPreferences sharedPreferences = getApplication().getSharedPreferences("informationAccount", MODE_PRIVATE);
                            sharedPreferences.edit().clear().apply();
                            dialogInterface.dismiss();
                            startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                        }
                    }).build();
            materialDialog.show();
        }
    }

    public String getAccountJson() {
        return accountJson;
    }

    public String getProfileJson() {
        return profileJson;
    }

    public String getDataJson() {
        return dataJson;
    }

    public String get_token() {
        return _token;
    }

    public void setupNavigationBottom() {
        homeBinding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        homeBinding.viewPager.setCurrentItem(0);
                        break;
                    case R.id.profile:
                        homeBinding.viewPager.setCurrentItem(1);
                        break;
                }
                return false;
            }
        });
    }

    private void setUpViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        homeBinding.viewPager.setAdapter(viewPagerAdapter);
        homeBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //Event vuốt trái phải
            @SuppressLint("ResourceAsColor")
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        homeBinding.bottomNav.getMenu().findItem(R.id.home).setChecked(true);
                        break;
                    case 1:
                        homeBinding.bottomNav.getMenu().findItem(R.id.profile).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkToken();
    }
}
