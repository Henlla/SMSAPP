package com.demo1.smsapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.demo1.smsapp.R;
import com.demo1.smsapp.adapter.ListSubjectAdapter;
import com.demo1.smsapp.api.*;
import com.demo1.smsapp.api.utils.APIUtils;
import com.demo1.smsapp.databinding.ActivityViewAttendanceBinding;
import com.demo1.smsapp.dto.ResponseModel;
import com.demo1.smsapp.dto.SubjectView;
import com.demo1.smsapp.models.*;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ViewSubjectActivity extends AppCompatActivity {
    ActivityViewAttendanceBinding binding;
    String token, dataJson;
    Student student;
    Gson gson;
    MaterialDialog materialDialog;
    SharedPreferences data;
    ListSubjectAdapter adapter;

    StudentClassAPI studentClassAPI;
    ScheduleAPI scheduleAPI;
    StudentMajorAPI studentMajorAPI;
    SubjectAPI subjectAPI;
    StudentSubjectAPI studentSubjectAPI;

    List<StudentClass> listStudentClass;
    List<Schedule> listSchedule;
    List<Subject> listSubject;
    List<SubjectView> listSubjectView;

    MajorStudent majorStudent;
    SubjectView subjectView;
    StudentSubject studentSubject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.red));
        binding = ActivityViewAttendanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        OnBack();
        onCreateApi();
        onGetClass();
    }

    public void onCreateApi() {
        studentClassAPI = APIUtils.getStudentClass();
        scheduleAPI = APIUtils.getScheduleAPI();
        studentMajorAPI = APIUtils.getStudentMajor();
        subjectAPI = APIUtils.getSubject();
        studentSubjectAPI = APIUtils.getStudentSubject();
        init();
    }

    public void init() {
        adapter = new ListSubjectAdapter();
        gson = new Gson();
        data = getSharedPreferences("informationAccount", MODE_PRIVATE);
        dataJson = data.getString("data", null);
        token = data.getString("token", null);
        student = gson.fromJson(dataJson, Student.class);
        listSubjectView = new ArrayList<>();
        onItemClick();
    }

    @SuppressLint("NewApi")
    public void OnGetListSubjectView(StudentSubject studentSubject, Subject subject) {
        subjectView = new SubjectView();
        subjectView.setId(subject.getId());
        subjectView.setSemester(subject.getSemesterId());
        subjectView.setSubject_code(subject.getSubjectCode());
        subjectView.setSubject_name(subject.getSubjectName());
        subjectView.setStatus(studentSubject.getStatus());
        listSubjectView.add(subjectView);
        Collections.sort(listSubjectView, Comparator.comparing(SubjectView::getSemester).thenComparing(SubjectView::getId));
        OnBindingView(listSubjectView);
    }

    public void OnBindingView(List<SubjectView> listSubjectView) {
        binding.attendanceRcv.setLayoutManager(new LinearLayoutManager(ViewSubjectActivity.this));
        adapter.setData(listSubjectView);
        binding.attendanceRcv.setAdapter(adapter);
    }

    public void onGetStudentSubject(Integer subjectId, Integer studentId, Subject subject) {
        studentSubjectAPI.findStudentSubjectBySubjectIdAndStudentId(token, subjectId, studentId).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()) {
                    studentSubject = new StudentSubject();
                    String studentSubjectJson = gson.toJson(response.body().getData());
                    studentSubject = gson.fromJson(studentSubjectJson, StudentSubject.class);
                    OnGetListSubjectView(studentSubject, subject);
                } else if (response.code() == 403) {
                    materialDialog = new MaterialDialog.Builder(ViewSubjectActivity.this)
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
                    Toast.makeText(getApplicationContext(), "Get data fail", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e("error", t.getMessage());
                Toast.makeText(getApplicationContext(), "Get student subject fail", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onGetSubject() {
        subjectAPI.findSubjectBySemesterIdAndMajorId(token, 1, listSchedule.size(), majorStudent.getMajorId())
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful()) {
                            listSubject = new ArrayList<>();
                            String subjectJson = gson.toJson(response.body().getData());
                            Type type = new TypeToken<ArrayList<Subject>>() {
                            }.getType();
                            listSubject = gson.fromJson(subjectJson, type);
                            for (Subject subject : listSubject) {
                                onGetStudentSubject(subject.getId(), student.getId(), subject);
                            }
                        } else if (response.code() == 403) {
                            materialDialog = new MaterialDialog.Builder(ViewSubjectActivity.this)
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
                            Toast.makeText(getApplicationContext(), "Get data fail", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {

                    }
                });
    }

    public void onGetMajorStudent() {
        studentMajorAPI.getMajorByStudentId(token, student.getId()).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()) {
                    majorStudent = new MajorStudent();
                    String majorStudentJson = gson.toJson(response.body().getData());
                    majorStudent = gson.fromJson(majorStudentJson, MajorStudent.class);
                    onGetSubject();
                } else if (response.code() == 403) {
                    materialDialog = new MaterialDialog.Builder(ViewSubjectActivity.this)
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
                    Toast.makeText(getApplicationContext(), "Get data fail", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e("error", t.getMessage());
                Toast.makeText(getApplicationContext(), "Get Major Student Fail", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onGetSchedule() {
        scheduleAPI.getScheduleByClass(token, listStudentClass.get(0).getClassId()).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()) {
                    listSchedule = new ArrayList<>();
                    String scheduleJson = gson.toJson(response.body().getData());
                    Type type = new TypeToken<ArrayList<Schedule>>() {
                    }.getType();
                    listSchedule = gson.fromJson(scheduleJson, type);
                    onGetMajorStudent();
                } else if (response.code() == 403) {
                    materialDialog = new MaterialDialog.Builder(ViewSubjectActivity.this)
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
                    Toast.makeText(getApplicationContext(), "Get data fail", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e("error", t.getMessage());
                Toast.makeText(getApplicationContext(), "Get Schedule Fail", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onGetClass() {
        studentClassAPI.getClassByStudentId(token, student.getId()).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        listStudentClass = new ArrayList<>();
                        String studentClassJson = gson.toJson(response.body().getData());
                        Type type = new TypeToken<ArrayList<StudentClass>>() {
                        }.getType();
                        listStudentClass = gson.fromJson(studentClassJson, type);
                        onGetSchedule();
                    } else {
                        Log.i("info", "Null data");
                    }
                } else if (response.code() == 403) {
                    materialDialog = new MaterialDialog.Builder(ViewSubjectActivity.this)
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
                    Toast.makeText(getApplicationContext(), "Get data fail", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e("error", t.getMessage());
                Toast.makeText(getApplicationContext(), "Get Class Fail", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onItemClick() {
        adapter.OnItemClick(new ListSubjectAdapter.ItemClick() {
            @Override
            public void OnClickItem(SubjectView subjectView) {
                Intent intent = new Intent(ViewSubjectActivity.this, AttendanceActivity.class);
                intent.putExtra("subjectId", subjectView.getId().toString());
                intent.putExtra("studentId", student.getId().toString());
                startActivity(intent);
            }
        });
    }

    private void OnBack() {
        binding.icBack.setOnClickListener(view -> {
            finish();
        });
    }
}
