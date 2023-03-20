package com.demo1.smsapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Window;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.demo1.smsapp.R;
import com.demo1.smsapp.adapter.ListAttendanceAdapter;
import com.demo1.smsapp.api.*;
import com.demo1.smsapp.api.utils.APIUtils;
import com.demo1.smsapp.databinding.ActivityAttendanceBinding;
import com.demo1.smsapp.dto.AttendanceView;
import com.demo1.smsapp.dto.ResponseModel;
import com.demo1.smsapp.models.*;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AttendanceActivity extends AppCompatActivity {
    ActivityAttendanceBinding binding;
    MaterialDialog materialDialog;

    SharedPreferences data;
    String token;
    int subjectId, studentId;
    Gson gson;

    List<AttendanceView> listAttendanceView;
    List<ScheduleDetail> listScheduleDetail;
    ListAttendanceAdapter adapter;

    Classses classses;

    StudentClassAPI studentClassAPI;
    ClassAPI classAPI;
    TeacherAPI teacherAPI;
    ScheduleAPI scheduleAPI;
    ScheduleDetailAPI scheduleDetailAPI;
    StudentSubjectAPI studentSubjectAPI;
    AttendanceAPI attendanceAPI;

    StudentSubject studentSubject;
    Teacher teacher;
    Schedule schedule;
    AttendanceView attendanceView;
    Attendance attendance;

    List<StudentClass> listStudentClass;
    List<Classses> listClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAttendanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.red));
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
        createApi();
        onGetStudentClass();
    }

    public void createApi() {
        studentClassAPI = APIUtils.getStudentClass();
        classAPI = APIUtils.getClasses();
        teacherAPI = APIUtils.getTeacher();
        scheduleAPI = APIUtils.getScheduleAPI();
        scheduleDetailAPI = APIUtils.getScheduleDetail();
        studentSubjectAPI = APIUtils.getStudentSubject();
        attendanceAPI = APIUtils.getAttendance();
        init();
    }

    public void init() {
        gson = new Gson();
        data = getSharedPreferences("informationAccount", MODE_PRIVATE);
        token = data.getString("token", null);
        adapter = new ListAttendanceAdapter();
        listAttendanceView = new ArrayList<>();
        Intent intent = getIntent();
        subjectId = Integer.parseInt(intent.getStringExtra("subjectId"));
        studentId = Integer.parseInt(intent.getStringExtra("studentId"));
        OnBack();
    }

    public void onGetStudentClass() {
        try {
            Response<ResponseModel> response = studentClassAPI.getClassIdByStudentId(token, studentId).execute();
            if (response.isSuccessful()) {
                String studentClassJson = gson.toJson(response.body().getData());
                listStudentClass = gson.fromJson(studentClassJson, new TypeToken<ArrayList<StudentClass>>() {
                }.getType());
                onGetClass();
            } else if (response.code() == 403) {
                materialDialog = new MaterialDialog.Builder(AttendanceActivity.this)
                        .setMessage("End of session login ! Please login again")
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
            } else {
                Toast.makeText(getApplicationContext(), "Can't get student class", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Can't get student class", Toast.LENGTH_LONG).show();
        }
    }

    public void onGetClass() {
        try {
            Response<ResponseModel> response = classAPI.getClassById(token, listStudentClass.get(0).getClassId()).execute();
            if (response.isSuccessful()) {
                String classJson = gson.toJson(response.body().getData());
                classses = gson.fromJson(classJson, Classses.class);
            } else if (response.code() == 403) {
                materialDialog = new MaterialDialog.Builder(AttendanceActivity.this)
                        .setMessage("End of session login ! Please login again")
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
            } else {
                Toast.makeText(getApplicationContext(), "Can't get schedule", Toast.LENGTH_LONG).show();
            }
            getScheduleDetail();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Can't get schedule", Toast.LENGTH_LONG).show();
        }
    }

    public void getScheduleDetail() {
        try {
            listScheduleDetail = new ArrayList<>();
            for (Schedule schedule : classses.getSchedulesById()) {
                for (ScheduleDetail scheduleDetail : schedule.getScheduleDetailsById()) {
                    if (scheduleDetail.getSubjectId().equals(subjectId)) {
                        listScheduleDetail.add(scheduleDetail);
                    }
                }
            }
            OnGetAttendance();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Can't get schedule", Toast.LENGTH_LONG).show();
        }
    }

    public void OnGetAttendance() {

        try {
            listAttendanceView = new ArrayList<>();
            Response<ResponseModel> responseStudentSubject = studentSubjectAPI.findStudentSubjectBySubjectIdAndStudentId(token, subjectId, studentId).execute();
            String studentSubjectJson = gson.toJson(responseStudentSubject.body().getData());
            StudentSubject studentSubject = gson.fromJson(studentSubjectJson, StudentSubject.class);
            for (ScheduleDetail scheduleDetail : listScheduleDetail) {
                attendanceView = new AttendanceView();
                Response<ResponseModel> response = attendanceAPI.findAttendanceByDateAndSlotAndStudentSubjectAndShift(token, scheduleDetail.getDate(), scheduleDetail.getSlot(), studentSubject.getId(), classses.getShift()).execute();
                String attendanceJson = gson.toJson(response.body().getData());
                Attendance attendance = gson.fromJson(attendanceJson, Attendance.class);
                if (attendance != null) {
                    attendanceView.setDate(scheduleDetail.getDate());
                    attendanceView.setSlot(scheduleDetail.getSlot());
                    attendanceView.setClass_code(classses.getClassCode());
                    attendanceView.setTeacher_name(classses.getTeacher().getProfileByProfileId().getFirstName() + " " + classses.getTeacher().getProfileByProfileId().getLastName());
                    attendanceView.setStatus(String.valueOf(attendance.getStatus()));
                    listAttendanceView.add(attendanceView);
                } else {
                    attendanceView.setDate(scheduleDetail.getDate());
                    attendanceView.setSlot(scheduleDetail.getSlot());
                    attendanceView.setClass_code(classses.getClassCode());
                    attendanceView.setTeacher_name(classses.getTeacher().getProfileByProfileId().getFirstName() + " " + classses.getTeacher().getProfileByProfileId().getLastName());
                    attendanceView.setStatus("FUTURE");
                    listAttendanceView.add(attendanceView);
                }
            }
            OnGetAttendanceView(listAttendanceView);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Can't get schedule", Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("NewApi")
    public void OnGetAttendanceView(List<AttendanceView> listAttendanceView) {
        if (listAttendanceView.size() == listScheduleDetail.size()) {
            Collections.sort(listAttendanceView, Comparator.comparing(AttendanceView::getDate)
                    .thenComparing(AttendanceView::getSlot));
            OnBindingView(listAttendanceView);
        }
    }

    public void OnBindingView(List<AttendanceView> listAttendanceView) {
        binding.attendanceRcv.setLayoutManager(new LinearLayoutManager(AttendanceActivity.this));
        adapter.setData(listAttendanceView);
        binding.attendanceRcv.setAdapter(adapter);
    }

    private void OnBack() {
        binding.icBack.setOnClickListener(view -> {
            finish();
        });
    }
}
