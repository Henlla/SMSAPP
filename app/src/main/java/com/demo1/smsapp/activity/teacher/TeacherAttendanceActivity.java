package com.demo1.smsapp.activity.teacher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.demo1.smsapp.R;
import com.demo1.smsapp.activity.SplashActivity;
import com.demo1.smsapp.databinding.ActivityTeacherAttendanceBinding;
import com.demo1.smsapp.fragment.EditAttendanceFragment;
import com.demo1.smsapp.fragment.TakeAttendanceFragment;
import com.google.android.material.tabs.TabLayout;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

import java.util.Date;

public class TeacherAttendanceActivity extends AppCompatActivity {
    ActivityTeacherAttendanceBinding binding;

    SharedPreferences data;
    String _token, dataJson;
    MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeacherAttendanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.red));
        init();
    }

    public void init() {
        data = getSharedPreferences("informationAccount", MODE_PRIVATE);
        dataJson = data.getString("data", null);
        _token = data.getString("token", null);
        callFragment();
        OnBack();
    }

    private void OnBack() {
        binding.icBack.setOnClickListener(view -> {
            finish();
        });
    }

    private void callFragment() {
        setFragment(new TakeAttendanceFragment());
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        setFragment(new TakeAttendanceFragment());
                        break;
                    case 1:
                        setFragment(new EditAttendanceFragment());
                        break;
                    default:
                        System.exit(0);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(binding.frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    public String getDataJson() {
        return dataJson;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public String getToken() {
        return _token;
    }
}
