package com.demo1.smsapp.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;
import com.demo1.smsapp.R;
import com.demo1.smsapp.adapter.NewAdapter;
import com.demo1.smsapp.adapter.ViewPagerAdapter;
import com.demo1.smsapp.api.NewsAPI;
import com.demo1.smsapp.api.utils.APIUtils;
import com.demo1.smsapp.databinding.ActivityHomeBinding;
import com.demo1.smsapp.dto.ResponseModel;
import com.demo1.smsapp.fragment.HomeFragment;
import com.demo1.smsapp.fragment.ProfileFragment;
import com.demo1.smsapp.models.Account;
import com.demo1.smsapp.models.News;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding homeBinding;
    String accountJson;
    String profileJson;
    String dataJson;
    String _token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(homeBinding.getRoot());
        SharedPreferences data = getSharedPreferences("informationAccount",MODE_PRIVATE);
        accountJson = data.getString("account",null);
        _token = data.getString("token",null);
        profileJson = data.getString("profile",null);
        dataJson = data.getString("data",null);
        setUpViewPager();
        setupNavigationBottom();
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

    public void setupNavigationBottom(){
        homeBinding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()){
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
    private void setUpViewPager(){
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
                switch (position){
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


}