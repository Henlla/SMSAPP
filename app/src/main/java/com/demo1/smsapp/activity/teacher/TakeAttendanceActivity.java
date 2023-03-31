package com.demo1.smsapp.activity.teacher;

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
import com.demo1.smsapp.activity.HomeActivity;
import com.demo1.smsapp.activity.SplashActivity;
import com.demo1.smsapp.adapter.ListStudentAttendanceAdapter;
import com.demo1.smsapp.api.*;
import com.demo1.smsapp.api.utils.APIUtils;
import com.demo1.smsapp.databinding.ActivityTakeAttendanceBinding;
import com.demo1.smsapp.dto.ResponseModel;
import com.demo1.smsapp.dto.StudentAttendanceView;
import com.demo1.smsapp.models.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TakeAttendanceActivity extends AppCompatActivity {
    ActivityTakeAttendanceBinding binding;

    StudentClassAPI studentClassAPI;
    ScheduleAPI scheduleAPI;
    ScheduleDetailAPI scheduleDetailAPI;
    AttendanceAPI attendanceAPI;
    ClassAPI classAPI;
    StudentSubjectAPI studentSubjectAPI;
    SubjectAPI subjectAPI;
    AttendanceTrackingApi attendanceTrackingApi;

    MaterialDialog materialDialog;
    SharedPreferences data;
    Gson gson;
    Teacher teacher;

    ScheduleDetail scheduleDetail;
    Classses classses;
    Attendance attendance;

    ListStudentAttendanceAdapter adapter;
    String classId, slot, date, token, dataClass;

    List<StudentAttendanceView> listStudentAttendance;
    List<Schedule> listSchedule;
    List<ScheduleDetail> listScheduleDetail;
    List<Attendance> listAttendance;
    List<StudentSubject> listStudentSubject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.red));
        binding = ActivityTakeAttendanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        onCreateApi();

    }

    public void onCreateApi() {
        studentClassAPI = APIUtils.getStudentClass();
        scheduleAPI = APIUtils.getScheduleAPI();
        scheduleDetailAPI = APIUtils.getScheduleDetail();
        attendanceAPI = APIUtils.getAttendance();
        classAPI = APIUtils.getClasses();
        studentSubjectAPI = APIUtils.getStudentSubject();
        attendanceTrackingApi = APIUtils.getAttendanceTracking();
        subjectAPI = APIUtils.getSubject();
        init();
        onCreateList();
    }


    public void init() {
        adapter = new ListStudentAttendanceAdapter();
        Intent intent = getIntent();
        gson = new Gson();
        dataClass = intent.getStringExtra("classId");
        data = getSharedPreferences("informationAccount", MODE_PRIVATE);
        token = data.getString("token", null);
        teacher = gson.fromJson(data.getString("data", null), Teacher.class);
        classId = dataClass.split("-")[0];
        slot = dataClass.split("-")[1];
        date = dataClass.split("-")[2] + "-" + dataClass.split("-")[3] + "-" + dataClass.split("-")[4];
        OnBack();
        onTakeAttendance();
    }

    public void onCreateList() {
        listSchedule = new ArrayList<>();
        listScheduleDetail = new ArrayList<>();
        onGetStudentInClass();
    }


    @SuppressLint("NewApi")
    public void onGetStudentInClass() {
//        try {
        listStudentAttendance = new ArrayList<>();
            studentClassAPI.getStudentByClassId(token,Integer.valueOf(classId)).enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    if (response.isSuccessful()) {
                        String studentJson = gson.toJson(response.body().getData());
                        List<StudentClass> studentClassList = gson.fromJson(studentJson, new TypeToken<ArrayList<StudentClass>>() {
                        }.getType());
                        for (StudentClass studentClass : studentClassList) {
                            StudentAttendanceView studentAttendanceView = new StudentAttendanceView();
                            studentAttendanceView.setId(studentClass.getStudentId());
                            studentAttendanceView.setClassId(dataClass);
                            studentAttendanceView.setStudentName(studentClass.getClassStudentByStudent().getStudentByProfile().getFirstName() + " " + studentClass.getClassStudentByStudent().getStudentByProfile().getLastName());
                            listStudentAttendance.add(studentAttendanceView);
                        }
                        Collections.sort(listStudentAttendance, Comparator.comparing(StudentAttendanceView::getStudentName));
                        if (!listStudentAttendance.isEmpty()) {
                            OnBindingData(listStudentAttendance);
                        } else {
                            Toast.makeText(getApplicationContext(), "Don't find student", Toast.LENGTH_LONG).show();
                        }
                    } else if (response.code() == 403) {
                        materialDialog = new MaterialDialog.Builder(TakeAttendanceActivity.this)
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
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Log.e("error",t.getMessage());
                }
            });
//            Response<ResponseModel> response = studentClassAPI.getStudentByClassId(token, Integer.valueOf(classId)).execute();
//            if (response.isSuccessful()) {
//                String studentJson = gson.toJson(response.body().getData());
//                List<StudentClass> studentClassList = gson.fromJson(studentJson, new TypeToken<ArrayList<StudentClass>>() {
//                }.getType());
//                for (StudentClass studentClass : studentClassList) {
//                    StudentAttendanceView studentAttendanceView = new StudentAttendanceView();
//                    studentAttendanceView.setId(studentClass.getStudentId());
//                    studentAttendanceView.setClassId(dataClass);
//                    studentAttendanceView.setStudentName(studentClass.getClassStudentByStudent().getStudentByProfile().getFirstName() + " " + studentClass.getClassStudentByStudent().getStudentByProfile().getLastName());
//                    listStudentAttendance.add(studentAttendanceView);
//                }
//                Collections.sort(listStudentAttendance, Comparator.comparing(StudentAttendanceView::getStudentName));
//                if (!listStudentAttendance.isEmpty()) {
//                    OnBindingData(listStudentAttendance);
//                } else {
//                    Toast.makeText(getApplicationContext(), "Don't find student", Toast.LENGTH_LONG).show();
//                }
//            } else if (response.code() == 403) {
//                materialDialog = new MaterialDialog.Builder(TakeAttendanceActivity.this)
//                        .setMessage("End of session login ! Please login again")
//                        .setCancelable(false)
//                        .setPositiveButton("", R.drawable.done, new MaterialDialog.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int which) {
//                                SharedPreferences sharedPreferences = getApplication().getSharedPreferences("informationAccount", MODE_PRIVATE);
//                                sharedPreferences.edit().clear().apply();
//                                dialogInterface.dismiss();
//                                startActivity(new Intent(getApplicationContext(), SplashActivity.class));
//                            }
//                        }).build();
//                materialDialog.show();
//            }
//        } catch (IOException e) {
//           throw new RuntimeException(e);
//        }
    }

    public void OnBindingData(List<StudentAttendanceView> list) {
        binding.takeRcv.setLayoutManager(new LinearLayoutManager(TakeAttendanceActivity.this));
        adapter.setData(list);
        binding.takeRcv.setAdapter(adapter);
    }

    public void onTakeAttendance() {
        binding.submitAttendance.setOnClickListener(v -> {
            List<StudentAttendanceView> listStudent = adapter.getData();
            onGetSchedule();
            onSaveAttendance(listStudent);
        });
    }

    public void onGetSchedule() {
        listSchedule.clear();
        try {
            Response<ResponseModel> response = scheduleAPI.getScheduleByClass(token, Integer.valueOf(classId)).execute();
            if (response.isSuccessful()) {
                String scheduleJson = gson.toJson(response.body().getData());
                listSchedule = gson.fromJson(scheduleJson, new TypeToken<ArrayList<Schedule>>() {
                }.getType());
                if (!listSchedule.isEmpty()) {
                    onGetScheduleDetail(listSchedule);
                } else {
                    Toast.makeText(getApplicationContext(), "Don't find schedule", Toast.LENGTH_LONG).show();
                }
            } else if (response.code() == 403) {
                materialDialog = new MaterialDialog.Builder(TakeAttendanceActivity.this)
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
            Toast.makeText(getApplicationContext(), "Don't find", Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("NewApi")
    public void onGetScheduleDetail(List<Schedule> listSchedule) {
        try {
            listScheduleDetail.clear();
            scheduleDetail = new ScheduleDetail();
            for (Schedule schedule : listSchedule) {
                String day = LocalDate.now().getDayOfMonth() < 10 ? "0" + LocalDate.now().getDayOfMonth() : String.valueOf(LocalDate.now().getDayOfMonth());
                String month = LocalDate.now().getMonthValue() < 10 ? "0" + LocalDate.now().getMonthValue() : String.valueOf(LocalDate.now().getMonthValue());
                String year = String.valueOf(LocalDate.now().getYear());
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate startDate = LocalDate.parse(schedule.getStartDate(), format);
                LocalDate endDate = LocalDate.parse(schedule.getEndDate(), format);
                LocalDate currentDate = LocalDate.parse(year + "-" + month + "-" + day, format);
                if (currentDate.isAfter(startDate) && currentDate.isBefore(endDate)) {
                    Response<ResponseModel> response = scheduleDetailAPI.findScheduleDetailByDateScheduleSlot(token, date, schedule.getId(), Integer.valueOf(slot)).execute();
                    if (response.isSuccessful()) {
                        String scheduleDetailJson = gson.toJson(response.body().getData());
                        scheduleDetail = gson.fromJson(scheduleDetailJson, ScheduleDetail.class);
                        onGetClass();
                    } else if (response.code() == 403) {
                        materialDialog = new MaterialDialog.Builder(TakeAttendanceActivity.this)
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
                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Don't find", Toast.LENGTH_LONG).show();
        }
    }

    public void onGetClass() {
        try {
            Response<ResponseModel> response = classAPI.getClassById(token, Integer.valueOf(classId)).execute();
            if (response.isSuccessful()) {
                classses = new Classses();
                String classJson = gson.toJson(response.body().getData());
                classses = gson.fromJson(classJson, Classses.class);
            } else if (response.code() == 403) {
                materialDialog = new MaterialDialog.Builder(TakeAttendanceActivity.this)
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
            Toast.makeText(getApplicationContext(), "Don't find", Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("NewApi")
    public void onSaveAttendance(List<StudentAttendanceView> listStudent) {
        try {
            listAttendance = new ArrayList<>();
            for (StudentAttendanceView studentView : listStudent) {
                attendance = new Attendance();
                Response<ResponseModel> response = studentSubjectAPI.findStudentSubjectByStudentIdAndSubjectId(token, studentView.getId(), scheduleDetail.getSubjectId()).execute();
                if (response.isSuccessful()) {
                    String studentSubjectJson = gson.toJson(response.body().getData());
                    StudentSubject studentSubject = gson.fromJson(studentSubjectJson, StudentSubject.class);
                    attendance.setDate(date);
                    attendance.setNote(studentView.getNote() == null ? "" : studentView.getNote());
                    attendance.setSlot(Integer.valueOf(slot));
                    attendance.setStatus(studentView.isPresent());
                    attendance.setShift(classses.getShift());
                    attendance.setStudentSubjectId(studentSubject.getId());
                    listAttendance.add(attendance);
                } else if (response.code() == 403) {
                    materialDialog = new MaterialDialog.Builder(TakeAttendanceActivity.this)
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
            }
            if (!listAttendance.isEmpty()) {
                Response<ResponseModel> response = attendanceAPI.saveAll(token, gson.toJson(listAttendance)).execute();
                if (response.isSuccessful()) {
                    listStudentSubject = new ArrayList<>();
                    for (Attendance attendance : listAttendance) {
                        getStudentSubject(attendance);
                    }
                    onSaveStatus();
                } else if (response.code() == 403) {
                    materialDialog = new MaterialDialog.Builder(TakeAttendanceActivity.this)
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
                    Toast.makeText(getApplicationContext(), "Take attendance fail", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Attendance fail", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Don't find", Toast.LENGTH_LONG).show();
        }
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
                if (responseSubject.isSuccessful()) {
                    Subject subject = gson.fromJson(gson.toJson(responseSubject.body().getData()), Subject.class);
                    Response<ResponseModel> responseAttendance = attendanceAPI.findAttendanceByStudentSubjectId(token, studentSubject.getId()).execute();
                    if (responseAttendance.isSuccessful()) {
                        String attendanceJson = gson.toJson(responseAttendance.body().getData());
                        List<Attendance> listAttendance = gson.fromJson(attendanceJson, new TypeToken<ArrayList<Attendance>>() {
                        }.getType());
                        double absentSlot = listAttendance.stream().filter(attendance1 -> attendance1.getStatus() == 0).collect(Collectors.toList()).size();
                        double subjectSlot = subject.getSlot();
                        int total = (int) Math.floor((absentSlot / subjectSlot) * 100);
                        studentSubject.setStatus(String.valueOf(total));
                        listStudentSubject.add(studentSubject);
                    } else if (responseStudentSubject.code() == 403) {
                        materialDialog = new MaterialDialog.Builder(TakeAttendanceActivity.this)
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
                } else if (responseSubject.code() == 403) {
                    materialDialog = new MaterialDialog.Builder(TakeAttendanceActivity.this)
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
                materialDialog = new MaterialDialog.Builder(TakeAttendanceActivity.this)
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
            Toast.makeText(getApplicationContext(), "Don't find", Toast.LENGTH_LONG).show();
        }
    }

    public void onSaveStatus() {
        try {
            String studentSubjectJson = gson.toJson(listStudentSubject);
            Response<ResponseModel> response = studentSubjectAPI.updateSubjectStatus(token, studentSubjectJson).execute();
            if (response.isSuccessful()) {
                AttendanceTracking attendanceTracking = new AttendanceTracking();
                attendanceTracking.setTeacherId(teacher.getId());
                attendanceTracking.setDate(date);
                String attendanceTrackingJson = gson.toJson(attendanceTracking);
                Response<ResponseModel> responseTracking = attendanceTrackingApi.saveTracking(token, attendanceTrackingJson).execute();
                if (responseTracking.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Take attendance success", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(TakeAttendanceActivity.this, HomeActivity.class));
                } else if (response.code() == 403) {
                    materialDialog = new MaterialDialog.Builder(TakeAttendanceActivity.this)
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
            } else if (response.code() == 403) {
                materialDialog = new MaterialDialog.Builder(TakeAttendanceActivity.this)
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
            Toast.makeText(getApplicationContext(), "Don't find", Toast.LENGTH_LONG).show();
        }
    }

    private void OnBack() {
        binding.icBack.setOnClickListener(view -> {
            finish();
        });
    }
}
