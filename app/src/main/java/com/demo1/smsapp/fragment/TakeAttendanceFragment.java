package com.demo1.smsapp.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.demo1.smsapp.R;
import com.demo1.smsapp.activity.SplashActivity;
import com.demo1.smsapp.activity.teacher.TakeAttendanceActivity;
import com.demo1.smsapp.activity.teacher.TeacherAttendanceActivity;
import com.demo1.smsapp.adapter.ListTakeAttendanceAdapter;
import com.demo1.smsapp.api.*;
import com.demo1.smsapp.api.utils.APIUtils;
import com.demo1.smsapp.databinding.FragmentTakeAttendanceBinding;
import com.demo1.smsapp.dto.ResponseModel;
import com.demo1.smsapp.dto.TakeAttendanceView;
import com.demo1.smsapp.models.*;
import com.demo1.smsapp.utils.ConvertDayOfWeek;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static android.content.Context.MODE_PRIVATE;

public class TakeAttendanceFragment extends Fragment {

    FragmentTakeAttendanceBinding binding;
    String token, teacherJson, date;
    Teacher teacher;
    Gson gson;

    String mSTime = "07:30", mETime = "11:30";
    String aSTime = "12:30", aETime = "17:30";
    String eSTime = "17:30", eETime = "21:30";

//    String mSTime = "00:00", mETime = "23:59";
//    String aSTime = "00:00", aETime = "23:59";
//    String eSTime = "00:00", eETime = "23:59";

    LocalTime startTime;
    LocalTime endTime;
    LocalTime onTime;
    LocalDate toDate;
    LocalDate fromDate;
    DateTimeFormatter formatTime;
    DateTimeFormatter formatDate;
    String currentTime;

    MaterialDialog materialDialog;

    LocalDate currentDate;
    LocalDate detailDate;
    LocalDate previousDate;

    TeacherAttendanceActivity attendanceActivity;
    TakeAttendanceView takeAttendanceView;
    Schedule schedule;
    Classses classses;

    ScheduleDetailAPI scheduleDetailAPI;
    ScheduleAPI scheduleAPI;
    ClassAPI classAPI;
    StudentClassAPI studentClassAPI;
    StudentSubjectAPI studentSubjectAPI;
    AttendanceAPI attendanceAPI;
    ListTakeAttendanceAdapter adapter;

    List<ScheduleDetail> listScheduleDetail;
    List<Schedule> listSchedule;
    List<Classses> listClass;
    List<StudentClass> listStudentClass;
    List<StudentSubject> listStudentSubject;
    List<TakeAttendanceView> listTakeAttendanceView;
    List<Attendance> listAttendance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTakeAttendanceBinding.inflate(inflater);

//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork()   // or .detectAll() for all detectable problems
//                .penaltyLog().build());
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        createApi();
        init();
        return binding.getRoot();
    }

    public void createApi() {
        scheduleDetailAPI = APIUtils.getScheduleDetail();
        scheduleAPI = APIUtils.getScheduleAPI();
        classAPI = APIUtils.getClasses();
        studentClassAPI = APIUtils.getStudentClass();
        studentSubjectAPI = APIUtils.getStudentSubject();
        attendanceAPI = APIUtils.getAttendance();
    }

    @SuppressLint("NewApi")
    public void init() {
        attendanceActivity = (TeacherAttendanceActivity) getActivity();
        gson = new Gson();

        listStudentSubject = new ArrayList<>();
        listTakeAttendanceView = new ArrayList<>();
        listClass = new ArrayList<>();
        listStudentClass = new ArrayList<>();

        schedule = new Schedule();
        classses = new Classses();

        adapter = new ListTakeAttendanceAdapter();
        formatTime = DateTimeFormatter.ofPattern("HH:mm");
        formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        currentTime = LocalTime.parse(LocalTime.now().format(formatTime)).toString();
        token = attendanceActivity.getToken();
        teacherJson = attendanceActivity.getDataJson();
        teacher = gson.fromJson(teacherJson, Teacher.class);
        date = ConvertDayOfWeek.dateFormat(LocalDate.now(), "yyyy-mm-dd");
        onclickItem();
        onGetScheduleDetail();
    }

    @SuppressLint("NewApi")
    public void onGetScheduleDetail() {
//        try {
        toDate = LocalDate.parse(LocalDate.now().format(formatDate));
        fromDate = toDate.minusDays(2);
        scheduleDetailAPI.findScheduleDetailByDateBetweenAndTeacherId(token, fromDate.toString(), toDate.toString(), teacher.getId()).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()) {
                    listScheduleDetail = new ArrayList<>();
                    String scheduleDetailJson = gson.toJson(response.body().getData());
                    Type type = new TypeToken<ArrayList<ScheduleDetail>>() {
                    }.getType();
                    listScheduleDetail = gson.fromJson(scheduleDetailJson, type);
                    if (!listScheduleDetail.isEmpty()) {
                        onGetAttendance(listScheduleDetail);
                    } else {
                        Toast.makeText(getContext(), "Don't have attendance for today", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

//    public void onGetScheduleClass(List<ScheduleDetail> listScheduleDetail) {
//        try {
//            listSchedule = new ArrayList<>();
//            for (ScheduleDetail scheduleDetail : listScheduleDetail) {
//                Response<ResponseModel> response = scheduleAPI.getScheduleByScheduleDetail(token, scheduleDetail.getScheduleId()).execute();
//                if (response.isSuccessful()) {
//                    String scheduleJson = gson.toJson(response.body().getData());
//                    Type type = new TypeToken<ArrayList<Schedule>>() {
//                    }.getType();
//                    listSchedule.addAll(gson.fromJson(scheduleJson, type));
//                }
//            }
//            if (!listSchedule.isEmpty()) {
//                onGetScheduleDetailBySchedule(listSchedule);
//            } else {
//                Toast.makeText(getContext(), "Don't have schedule", Toast.LENGTH_LONG).show();
//            }
//        } catch (IOException e) {
//            Toast.makeText(getContext(), "Don't find", Toast.LENGTH_LONG).show();
//        }
//    }
//    @SuppressLint("NewApi")
//    public void onGetScheduleDetailBySchedule(List<Schedule> listSchedule) {
//        listScheduleDetail = new ArrayList<>();
//        for (Schedule schedule : listSchedule) {
//            LocalDate startDate = LocalDate.parse(schedule.getStartDate(), formatDate);
//            LocalDate endDate = LocalDate.parse(schedule.getEndDate(), formatDate);
//            LocalDate currentDate = LocalDate.parse(LocalDate.now().format(formatDate));
//            if (currentDate.isAfter(startDate) && currentDate.isBefore(endDate)) {
//                try {
//                    Response<ResponseModel> response = scheduleDetailAPI.findScheduleDetailsByDateBetweenAndScheduleIdAndTeacherId(token, fromDate.toString(),toDate.toString(), schedule.getId(), teacher.getId()).execute();
//                    if (response.isSuccessful()) {
//                        String scheduleDetailJson = gson.toJson(response.body().getData());
//                        Type type = new TypeToken<ArrayList<ScheduleDetail>>() {
//                        }.getType();
//                        listScheduleDetail.addAll(gson.fromJson(scheduleDetailJson, type));
//                    }
//                } catch (Exception e) {
//                    Toast.makeText(getContext(), "Don't find", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//        listScheduleDetail = listScheduleDetail.stream().distinct().collect(Collectors.toList());
//        if (!listScheduleDetail.isEmpty()) {
//            onGetAttendance(listScheduleDetail);
//        } else {
//            Toast.makeText(getContext(), "Don't have schedule detail", Toast.LENGTH_LONG).show();
//        }
//    }

    @SuppressLint("NewApi")
    public void onGetAttendance(List<ScheduleDetail> listScheduleDetail) {
        listAttendance = new ArrayList<>();
//        try {
        boolean isOnTime = true;
        for (ScheduleDetail scheduleDetail : listScheduleDetail) {
            listAttendance.clear();
            schedule = new Schedule();
            classses = new Classses();
            //Lấy schedule
            scheduleAPI.getScheduleById(token, scheduleDetail.getScheduleId()).enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    if (response.isSuccessful()) {
                        String scheduleJson = gson.toJson(response.body().getData());
                        schedule = gson.fromJson(scheduleJson, Schedule.class);
                        if (schedule != null) {
                            classAPI.getClassById(token, schedule.getClassId()).enqueue(new Callback<ResponseModel>() {
                                @Override
                                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                    if (response.isSuccessful()) {
                                        String classJson = gson.toJson(response.body().getData());
                                        classses = gson.fromJson(classJson, Classses.class);
                                        if (classses != null) {
                                            attendanceAPI.findAttendanceByDateSlotAndShift(token, scheduleDetail.getDate(), scheduleDetail.getSlot(), classses.getShift()).enqueue(new Callback<ResponseModel>() {
                                                @Override
                                                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                                    boolean isOnTime = true;
                                                    if (response.isSuccessful()) {
                                                        String attendanceJson = gson.toJson(response.body().getData());
                                                        Type type = new TypeToken<ArrayList<Attendance>>() {
                                                        }.getType();
                                                        listAttendance = gson.fromJson(attendanceJson, type);
                                                        if (listAttendance.isEmpty()) {
                                                            takeAttendanceView = new TakeAttendanceView();
                                                            currentDate = LocalDate.parse(LocalDate.now().format(formatDate));
                                                            previousDate = currentDate.minusDays(1);
                                                            detailDate = LocalDate.parse(LocalDate.parse(scheduleDetail.getDate()).format(formatDate));
                                                            switch (classses.getShift().substring(0, 1)) {
                                                                case "M":
                                                                    startTime = LocalTime.parse(mSTime, formatTime);
                                                                    endTime = LocalTime.parse(mETime, formatTime);
                                                                    onTime = LocalTime.parse(currentTime, formatTime);
                                                                    if (detailDate.isEqual(previousDate)) {
                                                                        takeAttendanceView.setClass_code(classses.getClassCode());
                                                                        takeAttendanceView.setSlot(scheduleDetail.getSlot());
                                                                        takeAttendanceView.setSubject_code(scheduleDetail.getSubjectBySubjectId().getSubjectCode());
                                                                        takeAttendanceView.setSubject_name(scheduleDetail.getSubjectBySubjectId().getSubjectName());
                                                                        takeAttendanceView.setDate(scheduleDetail.getDate());
                                                                        takeAttendanceView.setId(classses.getId() + "-" + scheduleDetail.getSlot() + "-" + scheduleDetail.getDate());
                                                                        if (scheduleDetail.getSlot() == 1) {
                                                                            takeAttendanceView.setStartTime("07:30");
                                                                            takeAttendanceView.setEndTime("09:30");
                                                                        } else {
                                                                            takeAttendanceView.setStartTime("09:30");
                                                                            takeAttendanceView.setEndTime("11:30");
                                                                        }
                                                                        listTakeAttendanceView.add(takeAttendanceView);
                                                                    } else if (detailDate.isEqual(currentDate)) {
                                                                        takeAttendanceView.setClass_code(classses.getClassCode());
                                                                        takeAttendanceView.setSlot(scheduleDetail.getSlot());
                                                                        takeAttendanceView.setSubject_code(scheduleDetail.getSubjectBySubjectId().getSubjectCode());
                                                                        takeAttendanceView.setSubject_name(scheduleDetail.getSubjectBySubjectId().getSubjectName());
                                                                        takeAttendanceView.setDate(scheduleDetail.getDate());
                                                                        takeAttendanceView.setId(classses.getId() + "-" + scheduleDetail.getSlot() + "-" + scheduleDetail.getDate());
                                                                        if (scheduleDetail.getSlot() == 1) {
                                                                            takeAttendanceView.setStartTime("07:30");
                                                                            takeAttendanceView.setEndTime("09:30");
                                                                        } else {
                                                                            takeAttendanceView.setStartTime("09:30");
                                                                            takeAttendanceView.setEndTime("11:30");
                                                                        }
                                                                        listTakeAttendanceView.add(takeAttendanceView);
                                                                    } else {
                                                                        if (detailDate.plusDays(1).isEqual(previousDate)) {
                                                                            takeAttendanceView.setClass_code(classses.getClassCode());
                                                                            takeAttendanceView.setSlot(scheduleDetail.getSlot());
                                                                            takeAttendanceView.setSubject_code(scheduleDetail.getSubjectBySubjectId().getSubjectCode());
                                                                            takeAttendanceView.setSubject_name(scheduleDetail.getSubjectBySubjectId().getSubjectName());
                                                                            takeAttendanceView.setDate(scheduleDetail.getDate());
                                                                            takeAttendanceView.setId(classses.getId() + "-" + scheduleDetail.getSlot() + "-" + scheduleDetail.getDate());
                                                                            if (scheduleDetail.getSlot() == 1) {
                                                                                takeAttendanceView.setStartTime("07:30");
                                                                                takeAttendanceView.setEndTime("09:30");
                                                                            } else {
                                                                                takeAttendanceView.setStartTime("09:30");
                                                                                takeAttendanceView.setEndTime("11:30");
                                                                            }
                                                                            listTakeAttendanceView.add(takeAttendanceView);
                                                                        } else {
                                                                            if (onTime.isAfter(startTime) && onTime.isBefore(endTime)) {
                                                                                takeAttendanceView.setClass_code(classses.getClassCode());
                                                                                takeAttendanceView.setSlot(scheduleDetail.getSlot());
                                                                                takeAttendanceView.setSubject_code(scheduleDetail.getSubjectBySubjectId().getSubjectCode());
                                                                                takeAttendanceView.setSubject_name(scheduleDetail.getSubjectBySubjectId().getSubjectName());
                                                                                takeAttendanceView.setDate(scheduleDetail.getDate());
                                                                                takeAttendanceView.setId(classses.getId() + "-" + scheduleDetail.getSlot() + "-" + scheduleDetail.getDate());
                                                                                if (scheduleDetail.getSlot() == 1) {
                                                                                    takeAttendanceView.setStartTime("07:30");
                                                                                    takeAttendanceView.setEndTime("09:30");
                                                                                } else {
                                                                                    takeAttendanceView.setStartTime("09:30");
                                                                                    takeAttendanceView.setEndTime("11:30");
                                                                                }
                                                                                listTakeAttendanceView.add(takeAttendanceView);
                                                                            } else {
                                                                                isOnTime = false;
                                                                            }
                                                                        }
                                                                    }
                                                                    break;
                                                                case "A":
                                                                    startTime = LocalTime.parse(aSTime, formatTime);
                                                                    endTime = LocalTime.parse(aETime, formatTime);
                                                                    onTime = LocalTime.parse(currentTime, formatTime);
                                                                    if (detailDate.isEqual(previousDate)) {
                                                                        takeAttendanceView.setClass_code(classses.getClassCode());
                                                                        takeAttendanceView.setSlot(scheduleDetail.getSlot());
                                                                        takeAttendanceView.setSubject_code(scheduleDetail.getSubjectBySubjectId().getSubjectCode());
                                                                        takeAttendanceView.setSubject_name(scheduleDetail.getSubjectBySubjectId().getSubjectName());
                                                                        takeAttendanceView.setDate(scheduleDetail.getDate());
                                                                        takeAttendanceView.setId(classses.getId() + "-" + scheduleDetail.getSlot() + "-" + scheduleDetail.getDate());
                                                                        if (scheduleDetail.getSlot() == 1) {
                                                                            takeAttendanceView.setStartTime("12:30");
                                                                            takeAttendanceView.setEndTime("15:30");
                                                                        } else {
                                                                            takeAttendanceView.setStartTime("15:30");
                                                                            takeAttendanceView.setEndTime("17:30");
                                                                        }
                                                                        listTakeAttendanceView.add(takeAttendanceView);
                                                                    } else if (detailDate.isEqual(currentDate)) {
                                                                        takeAttendanceView.setClass_code(classses.getClassCode());
                                                                        takeAttendanceView.setSlot(scheduleDetail.getSlot());
                                                                        takeAttendanceView.setSubject_code(scheduleDetail.getSubjectBySubjectId().getSubjectCode());
                                                                        takeAttendanceView.setSubject_name(scheduleDetail.getSubjectBySubjectId().getSubjectName());
                                                                        takeAttendanceView.setDate(scheduleDetail.getDate());
                                                                        takeAttendanceView.setId(classses.getId() + "-" + scheduleDetail.getSlot() + "-" + scheduleDetail.getDate());
                                                                        if (scheduleDetail.getSlot() == 1) {
                                                                            takeAttendanceView.setStartTime("12:30");
                                                                            takeAttendanceView.setEndTime("15:30");
                                                                        } else {
                                                                            takeAttendanceView.setStartTime("15:30");
                                                                            takeAttendanceView.setEndTime("17:30");
                                                                        }
                                                                        listTakeAttendanceView.add(takeAttendanceView);
                                                                    } else {
                                                                        if (detailDate.plusDays(1).isEqual(previousDate)) {
                                                                            takeAttendanceView.setClass_code(classses.getClassCode());
                                                                            takeAttendanceView.setSlot(scheduleDetail.getSlot());
                                                                            takeAttendanceView.setSubject_code(scheduleDetail.getSubjectBySubjectId().getSubjectCode());
                                                                            takeAttendanceView.setSubject_name(scheduleDetail.getSubjectBySubjectId().getSubjectName());
                                                                            takeAttendanceView.setDate(scheduleDetail.getDate());
                                                                            takeAttendanceView.setId(classses.getId() + "-" + scheduleDetail.getSlot() + "-" + scheduleDetail.getDate());
                                                                            if (scheduleDetail.getSlot() == 1) {
                                                                                takeAttendanceView.setStartTime("12:30");
                                                                                takeAttendanceView.setEndTime("15:30");
                                                                            } else {
                                                                                takeAttendanceView.setStartTime("15:30");
                                                                                takeAttendanceView.setEndTime("17:30");
                                                                            }
                                                                            listTakeAttendanceView.add(takeAttendanceView);
                                                                        } else {
                                                                            if (onTime.isAfter(startTime) && onTime.isBefore(endTime)) {
                                                                                takeAttendanceView.setClass_code(classses.getClassCode());
                                                                                takeAttendanceView.setSlot(scheduleDetail.getSlot());
                                                                                takeAttendanceView.setSubject_code(scheduleDetail.getSubjectBySubjectId().getSubjectCode());
                                                                                takeAttendanceView.setSubject_name(scheduleDetail.getSubjectBySubjectId().getSubjectName());
                                                                                takeAttendanceView.setDate(scheduleDetail.getDate());
                                                                                takeAttendanceView.setId(classses.getId() + "-" + scheduleDetail.getSlot() + "-" + scheduleDetail.getDate());
                                                                                if (scheduleDetail.getSlot() == 1) {
                                                                                    takeAttendanceView.setStartTime("12:30");
                                                                                    takeAttendanceView.setEndTime("15:30");
                                                                                } else {
                                                                                    takeAttendanceView.setStartTime("15:30");
                                                                                    takeAttendanceView.setEndTime("17:30");
                                                                                }
                                                                                listTakeAttendanceView.add(takeAttendanceView);
                                                                            } else {
                                                                                isOnTime = false;
                                                                            }
                                                                        }
                                                                    }
                                                                    break;
                                                                case "E":
                                                                    startTime = LocalTime.parse(eSTime, formatTime);
                                                                    endTime = LocalTime.parse(eETime, formatTime);
                                                                    onTime = LocalTime.parse(currentTime, formatTime);
                                                                    if (detailDate.isEqual(previousDate)) {
                                                                        takeAttendanceView.setClass_code(classses.getClassCode());
                                                                        takeAttendanceView.setSlot(scheduleDetail.getSlot());
                                                                        takeAttendanceView.setSubject_code(scheduleDetail.getSubjectBySubjectId().getSubjectCode());
                                                                        takeAttendanceView.setSubject_name(scheduleDetail.getSubjectBySubjectId().getSubjectName());
                                                                        takeAttendanceView.setDate(scheduleDetail.getDate());
                                                                        takeAttendanceView.setId(classses.getId() + "-" + scheduleDetail.getSlot() + "-" + scheduleDetail.getDate());
                                                                        if (scheduleDetail.getSlot() == 1) {
                                                                            takeAttendanceView.setStartTime("17:30");
                                                                            takeAttendanceView.setEndTime("19:30");
                                                                        } else {
                                                                            takeAttendanceView.setStartTime("19:30");
                                                                            takeAttendanceView.setEndTime("21:30");
                                                                        }
                                                                        listTakeAttendanceView.add(takeAttendanceView);
                                                                    } else if (detailDate.isEqual(currentDate)) {
                                                                        takeAttendanceView.setClass_code(classses.getClassCode());
                                                                        takeAttendanceView.setSlot(scheduleDetail.getSlot());
                                                                        takeAttendanceView.setSubject_code(scheduleDetail.getSubjectBySubjectId().getSubjectCode());
                                                                        takeAttendanceView.setSubject_name(scheduleDetail.getSubjectBySubjectId().getSubjectName());
                                                                        takeAttendanceView.setDate(scheduleDetail.getDate());
                                                                        takeAttendanceView.setId(classses.getId() + "-" + scheduleDetail.getSlot() + "-" + scheduleDetail.getDate());
                                                                        if (scheduleDetail.getSlot() == 1) {
                                                                            takeAttendanceView.setStartTime("17:30");
                                                                            takeAttendanceView.setEndTime("19:30");
                                                                        } else {
                                                                            takeAttendanceView.setStartTime("19:30");
                                                                            takeAttendanceView.setEndTime("21:30");
                                                                        }
                                                                        listTakeAttendanceView.add(takeAttendanceView);
                                                                    } else {
                                                                        if (detailDate.plusDays(1).isEqual(previousDate)) {
                                                                            takeAttendanceView.setClass_code(classses.getClassCode());
                                                                            takeAttendanceView.setSlot(scheduleDetail.getSlot());
                                                                            takeAttendanceView.setSubject_code(scheduleDetail.getSubjectBySubjectId().getSubjectCode());
                                                                            takeAttendanceView.setSubject_name(scheduleDetail.getSubjectBySubjectId().getSubjectName());
                                                                            takeAttendanceView.setDate(scheduleDetail.getDate());
                                                                            takeAttendanceView.setId(classses.getId() + "-" + scheduleDetail.getSlot() + "-" + scheduleDetail.getDate());
                                                                            if (scheduleDetail.getSlot() == 1) {
                                                                                takeAttendanceView.setStartTime("17:30");
                                                                                takeAttendanceView.setEndTime("19:30");
                                                                            } else {
                                                                                takeAttendanceView.setStartTime("19:30");
                                                                                takeAttendanceView.setEndTime("21:30");
                                                                            }
                                                                            listTakeAttendanceView.add(takeAttendanceView);
                                                                        } else {
                                                                            if (onTime.isAfter(startTime) && onTime.isBefore(endTime)) {
                                                                                takeAttendanceView.setClass_code(classses.getClassCode());
                                                                                takeAttendanceView.setSlot(scheduleDetail.getSlot());
                                                                                takeAttendanceView.setSubject_code(scheduleDetail.getSubjectBySubjectId().getSubjectCode());
                                                                                takeAttendanceView.setSubject_name(scheduleDetail.getSubjectBySubjectId().getSubjectName());
                                                                                takeAttendanceView.setDate(scheduleDetail.getDate());
                                                                                takeAttendanceView.setId(classses.getId() + "-" + scheduleDetail.getSlot() + "-" + scheduleDetail.getDate());
                                                                                if (scheduleDetail.getSlot() == 1) {
                                                                                    takeAttendanceView.setStartTime("17:30");
                                                                                    takeAttendanceView.setEndTime("19:30");
                                                                                } else {
                                                                                    takeAttendanceView.setStartTime("19:30");
                                                                                    takeAttendanceView.setEndTime("21:30");
                                                                                }
                                                                                listTakeAttendanceView.add(takeAttendanceView);
                                                                            } else {
                                                                                isOnTime = false;
                                                                            }
                                                                        }
                                                                    }
                                                                    break;
                                                            }
                                                        }
                                                        if (isOnTime) {
                                                            if (!listTakeAttendanceView.isEmpty()) {
                                                                OnBindingData(listTakeAttendanceView);
                                                            }
                                                        }
                                                    } else if (response.code() == 403) {
                                                        materialDialog = new MaterialDialog.Builder(getActivity())
                                                                .setMessage("End of login session ! Please login again")
                                                                .setCancelable(false)
                                                                .setPositiveButton("", R.drawable.done, new MaterialDialog.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialogInterface, int which) {
                                                                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("informationAccount", MODE_PRIVATE);
                                                                        sharedPreferences.edit().clear().apply();
                                                                        dialogInterface.dismiss();
                                                                        startActivity(new Intent(getContext(), SplashActivity.class));
                                                                    }
                                                                }).build();
                                                        materialDialog.show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseModel> call, Throwable t) {
                                                    Toast.makeText(getContext(), "Don't find attendance for today", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    }else if (response.code() == 403) {
                                        materialDialog = new MaterialDialog.Builder(getActivity())
                                                .setMessage("End of login session ! Please login again")
                                                .setCancelable(false)
                                                .setPositiveButton("", R.drawable.done, new MaterialDialog.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int which) {
                                                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("informationAccount", MODE_PRIVATE);
                                                        sharedPreferences.edit().clear().apply();
                                                        dialogInterface.dismiss();
                                                        startActivity(new Intent(getContext(), SplashActivity.class));
                                                    }
                                                }).build();
                                        materialDialog.show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseModel> call, Throwable t) {
                                    Toast.makeText(getContext(), "Don't find class for today", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }else if (response.code() == 403) {
                        materialDialog = new MaterialDialog.Builder(getActivity())
                                .setMessage("End of login session ! Please login again")
                                .setCancelable(false)
                                .setPositiveButton("", R.drawable.done, new MaterialDialog.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {
                                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("informationAccount", MODE_PRIVATE);
                                        sharedPreferences.edit().clear().apply();
                                        dialogInterface.dismiss();
                                        startActivity(new Intent(getContext(), SplashActivity.class));
                                    }
                                }).build();
                        materialDialog.show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(getContext(), "Don't find schedule for today", Toast.LENGTH_LONG).show();
                }
            });
//                Response<ResponseModel> responseSchedule = scheduleAPI.getScheduleById(token, scheduleDetail.getScheduleId()).execute();
//                if (responseSchedule.isSuccessful()) {
//                    String scheduleJson = gson.toJson(responseSchedule.body().getData());
//                    schedule = gson.fromJson(scheduleJson, Schedule.class);
//                    // Lấy class
//                    Response<ResponseModel> responseClass = classAPI.getClassById(token, schedule.getClassId()).execute();
//                    if (responseClass.isSuccessful()) {
//                        String classJson = gson.toJson(responseClass.body().getData());
//                        classses = gson.fromJson(classJson, Classses.class);
//                        Response<ResponseModel> responseAttendance = attendanceAPI.findAttendanceByDateSlotAndShift(token, scheduleDetail.getDate(), scheduleDetail.getSlot(), classses.getShift()).execute();
//                        if (responseAttendance.isSuccessful()) {
//                            String attendanceJson = gson.toJson(responseAttendance.body().getData());
//                            Type type = new TypeToken<ArrayList<Attendance>>() {
//                            }.getType();
//                            listAttendance = gson.fromJson(attendanceJson, type);
//                            if (listAttendance.isEmpty()) {
//                                takeAttendanceView = new TakeAttendanceView();
//                                currentDate = LocalDate.parse(LocalDate.now().format(formatDate));
//                                previousDate = currentDate.minusDays(1);
//                                detailDate = LocalDate.parse(LocalDate.parse(scheduleDetail.getDate()).format(formatDate));
//                                switch (classses.getShift().substring(0, 1)) {
//                                    case "M":
//                                        startTime = LocalTime.parse(mSTime, formatTime);
//                                        endTime = LocalTime.parse(mETime, formatTime);
//                                        onTime = LocalTime.parse(currentTime, formatTime);
//                                        if (detailDate.isEqual(previousDate)) {
//                                            takeAttendanceView.setClass_code(classses.getClassCode());
//                                            takeAttendanceView.setSlot(scheduleDetail.getSlot());
//                                            takeAttendanceView.setSubject_code(scheduleDetail.getSubjectBySubjectId().getSubjectCode());
//                                            takeAttendanceView.setSubject_name(scheduleDetail.getSubjectBySubjectId().getSubjectName());
//                                            takeAttendanceView.setDate(scheduleDetail.getDate());
//                                            takeAttendanceView.setId(classses.getId() + "-" + scheduleDetail.getSlot() + "-" + scheduleDetail.getDate());
//                                            if (scheduleDetail.getSlot() == 1) {
//                                                takeAttendanceView.setStartTime("07:30");
//                                                takeAttendanceView.setEndTime("09:30");
//                                            } else {
//                                                takeAttendanceView.setStartTime("09:30");
//                                                takeAttendanceView.setEndTime("11:30");
//                                            }
//                                            listTakeAttendanceView.add(takeAttendanceView);
//                                        } else if (detailDate.isEqual(currentDate)) {
//                                            takeAttendanceView.setClass_code(classses.getClassCode());
//                                            takeAttendanceView.setSlot(scheduleDetail.getSlot());
//                                            takeAttendanceView.setSubject_code(scheduleDetail.getSubjectBySubjectId().getSubjectCode());
//                                            takeAttendanceView.setSubject_name(scheduleDetail.getSubjectBySubjectId().getSubjectName());
//                                            takeAttendanceView.setDate(scheduleDetail.getDate());
//                                            takeAttendanceView.setId(classses.getId() + "-" + scheduleDetail.getSlot() + "-" + scheduleDetail.getDate());
//                                            if (scheduleDetail.getSlot() == 1) {
//                                                takeAttendanceView.setStartTime("07:30");
//                                                takeAttendanceView.setEndTime("09:30");
//                                            } else {
//                                                takeAttendanceView.setStartTime("09:30");
//                                                takeAttendanceView.setEndTime("11:30");
//                                            }
//                                            listTakeAttendanceView.add(takeAttendanceView);
//                                        } else {
//                                            if (detailDate.plusDays(1).isEqual(previousDate)) {
//                                                takeAttendanceView.setClass_code(classses.getClassCode());
//                                                takeAttendanceView.setSlot(scheduleDetail.getSlot());
//                                                takeAttendanceView.setSubject_code(scheduleDetail.getSubjectBySubjectId().getSubjectCode());
//                                                takeAttendanceView.setSubject_name(scheduleDetail.getSubjectBySubjectId().getSubjectName());
//                                                takeAttendanceView.setDate(scheduleDetail.getDate());
//                                                takeAttendanceView.setId(classses.getId() + "-" + scheduleDetail.getSlot() + "-" + scheduleDetail.getDate());
//                                                if (scheduleDetail.getSlot() == 1) {
//                                                    takeAttendanceView.setStartTime("07:30");
//                                                    takeAttendanceView.setEndTime("09:30");
//                                                } else {
//                                                    takeAttendanceView.setStartTime("09:30");
//                                                    takeAttendanceView.setEndTime("11:30");
//                                                }
//                                                listTakeAttendanceView.add(takeAttendanceView);
//                                            } else {
//                                                if (onTime.isAfter(startTime) && onTime.isBefore(endTime)) {
//                                                    takeAttendanceView.setClass_code(classses.getClassCode());
//                                                    takeAttendanceView.setSlot(scheduleDetail.getSlot());
//                                                    takeAttendanceView.setSubject_code(scheduleDetail.getSubjectBySubjectId().getSubjectCode());
//                                                    takeAttendanceView.setSubject_name(scheduleDetail.getSubjectBySubjectId().getSubjectName());
//                                                    takeAttendanceView.setDate(scheduleDetail.getDate());
//                                                    takeAttendanceView.setId(classses.getId() + "-" + scheduleDetail.getSlot() + "-" + scheduleDetail.getDate());
//                                                    if (scheduleDetail.getSlot() == 1) {
//                                                        takeAttendanceView.setStartTime("07:30");
//                                                        takeAttendanceView.setEndTime("09:30");
//                                                    } else {
//                                                        takeAttendanceView.setStartTime("09:30");
//                                                        takeAttendanceView.setEndTime("11:30");
//                                                    }
//                                                    listTakeAttendanceView.add(takeAttendanceView);
//                                                } else {
//                                                    isOnTime = false;
//                                                }
//                                            }
//                                        }
//                                        break;
//                                    case "A":
//                                        startTime = LocalTime.parse(aSTime, formatTime);
//                                        endTime = LocalTime.parse(aETime, formatTime);
//                                        onTime = LocalTime.parse(currentTime, formatTime);
//                                        if (detailDate.isEqual(previousDate)) {
//                                            takeAttendanceView.setClass_code(classses.getClassCode());
//                                            takeAttendanceView.setSlot(scheduleDetail.getSlot());
//                                            takeAttendanceView.setSubject_code(scheduleDetail.getSubjectBySubjectId().getSubjectCode());
//                                            takeAttendanceView.setSubject_name(scheduleDetail.getSubjectBySubjectId().getSubjectName());
//                                            takeAttendanceView.setDate(scheduleDetail.getDate());
//                                            takeAttendanceView.setId(classses.getId() + "-" + scheduleDetail.getSlot() + "-" + scheduleDetail.getDate());
//                                            if (scheduleDetail.getSlot() == 1) {
//                                                takeAttendanceView.setStartTime("12:30");
//                                                takeAttendanceView.setEndTime("15:30");
//                                            } else {
//                                                takeAttendanceView.setStartTime("15:30");
//                                                takeAttendanceView.setEndTime("17:30");
//                                            }
//                                            listTakeAttendanceView.add(takeAttendanceView);
//                                        } else if (detailDate.isEqual(currentDate)) {
//                                            takeAttendanceView.setClass_code(classses.getClassCode());
//                                            takeAttendanceView.setSlot(scheduleDetail.getSlot());
//                                            takeAttendanceView.setSubject_code(scheduleDetail.getSubjectBySubjectId().getSubjectCode());
//                                            takeAttendanceView.setSubject_name(scheduleDetail.getSubjectBySubjectId().getSubjectName());
//                                            takeAttendanceView.setDate(scheduleDetail.getDate());
//                                            takeAttendanceView.setId(classses.getId() + "-" + scheduleDetail.getSlot() + "-" + scheduleDetail.getDate());
//                                            if (scheduleDetail.getSlot() == 1) {
//                                                takeAttendanceView.setStartTime("12:30");
//                                                takeAttendanceView.setEndTime("15:30");
//                                            } else {
//                                                takeAttendanceView.setStartTime("15:30");
//                                                takeAttendanceView.setEndTime("17:30");
//                                            }
//                                            listTakeAttendanceView.add(takeAttendanceView);
//                                        } else {
//                                            if (detailDate.plusDays(1).isEqual(previousDate)) {
//                                                takeAttendanceView.setClass_code(classses.getClassCode());
//                                                takeAttendanceView.setSlot(scheduleDetail.getSlot());
//                                                takeAttendanceView.setSubject_code(scheduleDetail.getSubjectBySubjectId().getSubjectCode());
//                                                takeAttendanceView.setSubject_name(scheduleDetail.getSubjectBySubjectId().getSubjectName());
//                                                takeAttendanceView.setDate(scheduleDetail.getDate());
//                                                takeAttendanceView.setId(classses.getId() + "-" + scheduleDetail.getSlot() + "-" + scheduleDetail.getDate());
//                                                if (scheduleDetail.getSlot() == 1) {
//                                                    takeAttendanceView.setStartTime("12:30");
//                                                    takeAttendanceView.setEndTime("15:30");
//                                                } else {
//                                                    takeAttendanceView.setStartTime("15:30");
//                                                    takeAttendanceView.setEndTime("17:30");
//                                                }
//                                                listTakeAttendanceView.add(takeAttendanceView);
//                                            } else {
//                                                if (onTime.isAfter(startTime) && onTime.isBefore(endTime)) {
//                                                    takeAttendanceView.setClass_code(classses.getClassCode());
//                                                    takeAttendanceView.setSlot(scheduleDetail.getSlot());
//                                                    takeAttendanceView.setSubject_code(scheduleDetail.getSubjectBySubjectId().getSubjectCode());
//                                                    takeAttendanceView.setSubject_name(scheduleDetail.getSubjectBySubjectId().getSubjectName());
//                                                    takeAttendanceView.setDate(scheduleDetail.getDate());
//                                                    takeAttendanceView.setId(classses.getId() + "-" + scheduleDetail.getSlot() + "-" + scheduleDetail.getDate());
//                                                    if (scheduleDetail.getSlot() == 1) {
//                                                        takeAttendanceView.setStartTime("12:30");
//                                                        takeAttendanceView.setEndTime("15:30");
//                                                    } else {
//                                                        takeAttendanceView.setStartTime("15:30");
//                                                        takeAttendanceView.setEndTime("17:30");
//                                                    }
//                                                    listTakeAttendanceView.add(takeAttendanceView);
//                                                } else {
//                                                    isOnTime = false;
//                                                }
//                                            }
//                                        }
//                                        break;
//                                    case "E":
//                                        startTime = LocalTime.parse(eSTime, formatTime);
//                                        endTime = LocalTime.parse(eETime, formatTime);
//                                        onTime = LocalTime.parse(currentTime, formatTime);
//                                        if (detailDate.isEqual(previousDate)) {
//                                            takeAttendanceView.setClass_code(classses.getClassCode());
//                                            takeAttendanceView.setSlot(scheduleDetail.getSlot());
//                                            takeAttendanceView.setSubject_code(scheduleDetail.getSubjectBySubjectId().getSubjectCode());
//                                            takeAttendanceView.setSubject_name(scheduleDetail.getSubjectBySubjectId().getSubjectName());
//                                            takeAttendanceView.setDate(scheduleDetail.getDate());
//                                            takeAttendanceView.setId(classses.getId() + "-" + scheduleDetail.getSlot() + "-" + scheduleDetail.getDate());
//                                            if (scheduleDetail.getSlot() == 1) {
//                                                takeAttendanceView.setStartTime("17:30");
//                                                takeAttendanceView.setEndTime("19:30");
//                                            } else {
//                                                takeAttendanceView.setStartTime("19:30");
//                                                takeAttendanceView.setEndTime("21:30");
//                                            }
//                                            listTakeAttendanceView.add(takeAttendanceView);
//                                        } else if (detailDate.isEqual(currentDate)) {
//                                            takeAttendanceView.setClass_code(classses.getClassCode());
//                                            takeAttendanceView.setSlot(scheduleDetail.getSlot());
//                                            takeAttendanceView.setSubject_code(scheduleDetail.getSubjectBySubjectId().getSubjectCode());
//                                            takeAttendanceView.setSubject_name(scheduleDetail.getSubjectBySubjectId().getSubjectName());
//                                            takeAttendanceView.setDate(scheduleDetail.getDate());
//                                            takeAttendanceView.setId(classses.getId() + "-" + scheduleDetail.getSlot() + "-" + scheduleDetail.getDate());
//                                            if (scheduleDetail.getSlot() == 1) {
//                                                takeAttendanceView.setStartTime("17:30");
//                                                takeAttendanceView.setEndTime("19:30");
//                                            } else {
//                                                takeAttendanceView.setStartTime("19:30");
//                                                takeAttendanceView.setEndTime("21:30");
//                                            }
//                                            listTakeAttendanceView.add(takeAttendanceView);
//                                        } else {
//                                            if (detailDate.plusDays(1).isEqual(previousDate)) {
//                                                takeAttendanceView.setClass_code(classses.getClassCode());
//                                                takeAttendanceView.setSlot(scheduleDetail.getSlot());
//                                                takeAttendanceView.setSubject_code(scheduleDetail.getSubjectBySubjectId().getSubjectCode());
//                                                takeAttendanceView.setSubject_name(scheduleDetail.getSubjectBySubjectId().getSubjectName());
//                                                takeAttendanceView.setDate(scheduleDetail.getDate());
//                                                takeAttendanceView.setId(classses.getId() + "-" + scheduleDetail.getSlot() + "-" + scheduleDetail.getDate());
//                                                if (scheduleDetail.getSlot() == 1) {
//                                                    takeAttendanceView.setStartTime("17:30");
//                                                    takeAttendanceView.setEndTime("19:30");
//                                                } else {
//                                                    takeAttendanceView.setStartTime("19:30");
//                                                    takeAttendanceView.setEndTime("21:30");
//                                                }
//                                                listTakeAttendanceView.add(takeAttendanceView);
//                                            } else {
//                                                if (onTime.isAfter(startTime) && onTime.isBefore(endTime)) {
//                                                    takeAttendanceView.setClass_code(classses.getClassCode());
//                                                    takeAttendanceView.setSlot(scheduleDetail.getSlot());
//                                                    takeAttendanceView.setSubject_code(scheduleDetail.getSubjectBySubjectId().getSubjectCode());
//                                                    takeAttendanceView.setSubject_name(scheduleDetail.getSubjectBySubjectId().getSubjectName());
//                                                    takeAttendanceView.setDate(scheduleDetail.getDate());
//                                                    takeAttendanceView.setId(classses.getId() + "-" + scheduleDetail.getSlot() + "-" + scheduleDetail.getDate());
//                                                    if (scheduleDetail.getSlot() == 1) {
//                                                        takeAttendanceView.setStartTime("17:30");
//                                                        takeAttendanceView.setEndTime("19:30");
//                                                    } else {
//                                                        takeAttendanceView.setStartTime("19:30");
//                                                        takeAttendanceView.setEndTime("21:30");
//                                                    }
//                                                    listTakeAttendanceView.add(takeAttendanceView);
//                                                } else {
//                                                    isOnTime = false;
//                                                }
//                                            }
//                                        }
//                                        break;
//                                }
//                            }
//                        } else {
//                            Toast.makeText(getContext(), "Don't have attendance for today", Toast.LENGTH_LONG).show();
//                        }
//                    } else {
//                        Toast.makeText(getContext(), "Don't have attendance for today", Toast.LENGTH_LONG).show();
//                    }
//                } else {
//                    Toast.makeText(getContext(), "Don't have attendance for today", Toast.LENGTH_LONG).show();
//                }
        }
//        } catch (Exception e) {
//            Toast.makeText(getContext(), "Don't find", Toast.LENGTH_LONG).show();
//        }
    }

    @SuppressLint("NewApi")
    public void OnBindingData(List<TakeAttendanceView> listTakeAttendanceView) {
        binding.takeRcv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setData(listTakeAttendanceView);
        binding.takeRcv.setAdapter(adapter);
    }

    public void onclickItem() {
        adapter.onClick(new ListTakeAttendanceAdapter.OnItemClick() {
            @Override
            public void onClickItem(TakeAttendanceView takeAttendanceView) {
                Intent intent = new Intent(getActivity(), TakeAttendanceActivity.class);
                intent.putExtra("classId", takeAttendanceView.getId());
                startActivity(intent);
            }
        });
    }

}
