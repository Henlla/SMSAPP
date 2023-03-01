package com.demo1.smsapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.demo1.smsapp.R;
import com.demo1.smsapp.databinding.ActivityTimetableBinding;

public class TimetableActivity extends AppCompatActivity {
    ActivityTimetableBinding timetableBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timetableBinding= ActivityTimetableBinding.inflate(getLayoutInflater());
        setContentView(timetableBinding.getRoot());
    }
}