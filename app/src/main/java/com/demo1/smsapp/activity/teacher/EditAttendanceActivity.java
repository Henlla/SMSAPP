package com.demo1.smsapp.activity.teacher;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.demo1.smsapp.R;
import com.demo1.smsapp.activity.HomeActivity;
import com.demo1.smsapp.activity.SplashActivity;
import com.demo1.smsapp.adapter.ListEditAttendanceAdapter;
import com.demo1.smsapp.api.*;
import com.demo1.smsapp.api.utils.APIUtils;
import com.demo1.smsapp.databinding.ActivityEditAttendanceBinding;
import com.demo1.smsapp.dto.ResponseModel;
import com.demo1.smsapp.dto.StudentAttendanceView;
import com.demo1.smsapp.models.*;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EditAttendanceActivity extends AppCompatActivity {
    ActivityEditAttendanceBinding binding;

    SharedPreferences data;

    Gson gson;
    Teacher teacher;
    Classses classses;
    MaterialDialog materialDialog;

    String classId, slot, date, jsonData, token;

    List<StudentAttendanceView> listStudentAttendance;
    ListEditAttendanceAdapter adapter;
    List<Attendance> listAttendance;
    List<StudentSubject> listStudentSubject;

    ClassAPI classAPI;
    SubjectAPI subjectAPI;
    StudentClassAPI studentClassAPI;
    StudentSubjectAPI studentSubjectAPI;
    AttendanceAPI attendanceAPI;
    StudentAPI studentAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditAttendanceBinding.inflate(getLayoutInflater());
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.red));
        setContentView(binding.getRoot());
        onCreateApi();
        init();
        new MyTask().execute();

    }

    public void onCreateApi() {
        classAPI = APIUtils.getClasses();
        attendanceAPI = APIUtils.getAttendance();
        studentClassAPI = APIUtils.getStudentClass();
        studentSubjectAPI = APIUtils.getStudentSubject();
        studentAPI = APIUtils.getStudent();
        subjectAPI = APIUtils.getSubject();
    }

    public void init() {
        OnBack();
        Intent intent = getIntent();
        adapter = new ListEditAttendanceAdapter();
        gson = new Gson();
        data = getSharedPreferences("informationAccount", MODE_PRIVATE);
        jsonData = data.getString("data", null);
        token = data.getString("token", null);
        teacher = gson.fromJson(jsonData, Teacher.class);
        String classData = intent.getStringExtra("classId");
        classId = classData.split("-")[0];
        slot = classData.split("-")[1];
        date = classData.split("-")[2] + "-" + classData.split("-")[3] + "-" + classData.split("-")[4];
    }

    public class MyTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            onGetClass();
            onUpdateAttendance();
            return null;
        }
    }

    public void onGetClass() {
        try {
            Response<ResponseModel> response = classAPI.getClassById(token, Integer.valueOf(classId)).execute();
            if (response.isSuccessful()) {
                String classJson = gson.toJson(response.body().getData());
                classses = gson.fromJson(classJson, Classses.class);
                onGetAttendanceByDate(classses);
            } else if (response.code() == 403) {
                materialDialog = new MaterialDialog.Builder(EditAttendanceActivity.this)
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
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onGetAttendanceByDate(Classses classses) {
        try {
            listAttendance = new ArrayList<>();
            Response<ResponseModel> response = attendanceAPI.findAttendanceByDateSlotAndShift(token, date, Integer.valueOf(slot), classses.getShift()).execute();
            if (response.isSuccessful()) {
                String attendanceJson = gson.toJson(response.body().getData());
                listAttendance = gson.fromJson(attendanceJson, new TypeToken<ArrayList<Attendance>>() {
                }.getType());
                if(!listAttendance.isEmpty()){
                    onGetStudentSubject(listAttendance);
                }
            } else if (response.code() == 403) {
                materialDialog = new MaterialDialog.Builder(EditAttendanceActivity.this)
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
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onGetStudentSubject(List<Attendance> listAttendance) {
        try {
            listStudentAttendance = new ArrayList<>();
            for (Attendance attendance : listAttendance) {
                Response<ResponseModel> responseStudentSubject = studentSubjectAPI.findStudentSubjectByAttendance(token, attendance.getStudentSubjectId()).execute();
                if (responseStudentSubject.isSuccessful()) {
                    String studentSubjectJson = gson.toJson(responseStudentSubject.body().getData());
                    StudentSubject studentSubject = gson.fromJson(studentSubjectJson, StudentSubject.class);
                    Response<ResponseModel> responseStudent = studentAPI.getStudentById(token, studentSubject.getStudentId()).execute();
                    if (responseStudent.isSuccessful()) {
                        String studentJson = gson.toJson(responseStudent.body().getData());
                        Student student = gson.fromJson(studentJson, Student.class);
                        StudentAttendanceView studentAttendanceView = new StudentAttendanceView();
                        studentAttendanceView.setId(attendance.getId());
                        studentAttendanceView.setStudent_subjectId(studentSubject.getId());
                        studentAttendanceView.setClassId(classId);
                        studentAttendanceView.setNote(attendance.getNote());
                        studentAttendanceView.setStudentName(student.getStudentByProfile().getFirstName() + " " + student.getStudentByProfile().getLastName());
                        studentAttendanceView.setPresent(attendance.getStatus());
                        listStudentAttendance.add(studentAttendanceView);
                    }
                }
            }
            if (!listStudentAttendance.isEmpty()) {
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        OnBindingData(listStudentAttendance);
                        binding.editRcv.setVisibility(View.VISIBLE);
                        binding.emptyView.setVisibility(View.GONE);
                    }
                });

            }else{
                binding.editRcv.setVisibility(View.GONE);
                binding.emptyView.setVisibility(View.VISIBLE);
                binding.emptyView.setText("Don't have attendance taken");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void OnBindingData(List<StudentAttendanceView> list) {
        binding.editRcv.setLayoutManager(new LinearLayoutManager(EditAttendanceActivity.this));
        adapter.setData(list);
        binding.editRcv.setAdapter(adapter);
    }

    public void onUpdateAttendance() {
        listAttendance = new ArrayList<>();
        List<StudentAttendanceView> listStudentAttendanceView = adapter.getData();
        binding.updateAttendance.setOnClickListener(v -> {
            for (StudentAttendanceView studentAttendanceView : listStudentAttendanceView) {
                Attendance attendance = new Attendance();
                attendance.setStatus(studentAttendanceView.getIsPresent());
                attendance.setShift(classses.getShift());
                attendance.setSlot(Integer.valueOf(slot));
                attendance.setNote(studentAttendanceView.getNote());
                attendance.setId(studentAttendanceView.getId());
                attendance.setDate(date);
                attendance.setStudentSubjectId(studentAttendanceView.getStudent_subjectId());
                listAttendance.add(attendance);
            }
            if (!listAttendance.isEmpty()) {
                try {
                    Response<ResponseModel> response = attendanceAPI.saveAll(token, gson.toJson(listAttendance)).execute();
                    if (response.isSuccessful()) {
                        listStudentSubject = new ArrayList<>();
                        for (Attendance attendance : listAttendance) {
                            getStudentSubject(attendance);
                        }
                        onSaveStatus();
                    } else if (response.code() == 403) {
                        materialDialog = new MaterialDialog.Builder(EditAttendanceActivity.this)
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
                    }
                } catch (Exception e) {
                    Log.e("error",e.getMessage());
                }
            } else {
                Toast.makeText(getApplicationContext(), "Attendance fail", Toast.LENGTH_LONG).show();
            }
        });
    }

    @SuppressLint("NewApi")
    public void getStudentSubject(Attendance attendance) {
        try {
            // Lấy student subject
            Response<ResponseModel> responseStudentSubject = studentSubjectAPI.findStudentSubjectById(token, attendance.getStudentSubjectId()).execute();
            if (responseStudentSubject.isSuccessful()) {
                String studentSubjectJson = gson.toJson(responseStudentSubject.body().getData());
                StudentSubject studentSubject = gson.fromJson(studentSubjectJson, StudentSubject.class);
                //Lấy subject
                Response<ResponseModel> responseSubject = subjectAPI.findSubjectById(token, studentSubject.getSubjectId()).execute();
                Subject subject = gson.fromJson(gson.toJson(responseSubject.body().getData()), Subject.class);
                Response<ResponseModel> responseAttendance = attendanceAPI.findAttendanceByStudentSubjectId(token, studentSubject.getId()).execute();
                if (responseAttendance.isSuccessful()) {
                    String attendanceJson = gson.toJson(responseAttendance.body().getData());
                    List<Attendance> listAttendance = gson.fromJson(attendanceJson, new com.google.gson.reflect.TypeToken<ArrayList<Attendance>>() {
                    }.getType());
                    double absentSlot = listAttendance.stream().filter(attendance1 -> attendance1.getStatus() == 0).collect(Collectors.toList()).size();
                    double subjectSlot = subject.getSlot();
                    int total = (int) Math.floor((absentSlot / subjectSlot) * 100);
                    studentSubject.setStatus(String.valueOf(total));
                    listStudentSubject.add(studentSubject);
                } else if (responseStudentSubject.code() == 403) {
                    materialDialog = new MaterialDialog.Builder(EditAttendanceActivity.this)
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
                }
            } else if (responseStudentSubject.code() == 403) {
                materialDialog = new MaterialDialog.Builder(EditAttendanceActivity.this)
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
            }
        } catch (Exception e) {
            Log.e("error",e.getMessage());
        }
    }

    public void onSaveStatus() {
        try {
            String studentSubjectJson = gson.toJson(listStudentSubject);
            Response<ResponseModel> response = studentSubjectAPI.updateSubjectStatus(token, studentSubjectJson).execute();
            if (response.isSuccessful()) {
                Toast.makeText(getApplicationContext(), "Update attendance success", Toast.LENGTH_LONG).show();
                startActivity(new Intent(EditAttendanceActivity.this, HomeActivity.class));
            } else if (response.code() == 403) {
                materialDialog = new MaterialDialog.Builder(EditAttendanceActivity.this)
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
            }
        } catch (Exception e) {
            Log.e("error",e.getMessage());
        }

    }

    private void OnBack() {
        binding.icBack.setOnClickListener(view -> {
            finish();
        });
    }
}
