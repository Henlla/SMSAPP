package com.demo1.smsapp.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.demo1.smsapp.activity.teacher.EditAttendanceActivity;
import com.demo1.smsapp.activity.teacher.TeacherAttendanceActivity;
import com.demo1.smsapp.adapter.ListTakeAttendanceAdapter;
import com.demo1.smsapp.api.*;
import com.demo1.smsapp.api.utils.APIUtils;
import com.demo1.smsapp.databinding.FragmentEditAttendanceBinding;
import com.demo1.smsapp.dto.ResponseModel;
import com.demo1.smsapp.dto.TakeAttendanceView;
import com.demo1.smsapp.models.*;
import com.demo1.smsapp.utils.ConvertDayOfWeek;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import retrofit2.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressLint("NewApi")
public class EditAttendanceFragment extends Fragment {
    FragmentEditAttendanceBinding binding;
    String token, teacherJson, date;
    Teacher teacher;
    Gson gson;

    LocalDate toDate, fromDate;
    DateTimeFormatter formatDate;
    DateTimeFormatter formatTime;
    LocalTime currentTime;

    TeacherAttendanceActivity attendanceActivity;
    TakeAttendanceView takeAttendanceView;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditAttendanceBinding.inflate(inflater);

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
        init();
        onGetScheduleDetail();
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

    public void init() {
        attendanceActivity = (TeacherAttendanceActivity) getActivity();
        gson = new Gson();

        listStudentSubject = new ArrayList<>();
        listTakeAttendanceView = new ArrayList<>();
        listClass = new ArrayList<>();
        listStudentClass = new ArrayList<>();

        adapter = new ListTakeAttendanceAdapter();
        formatTime = DateTimeFormatter.ofPattern("HH:mm");
        formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        currentTime = LocalTime.parse(LocalTime.now().format(formatTime));
        token = attendanceActivity.getToken();
        teacherJson = attendanceActivity.getDataJson();
        teacher = gson.fromJson(teacherJson, Teacher.class);
        date = ConvertDayOfWeek.dateFormat(LocalDate.now(), "yyyy-mm-dd");
        onclickItem();
    }

    @SuppressLint("NewApi")
    public void onGetScheduleDetail() {
        try {
            listScheduleDetail = new ArrayList<>();
            toDate = LocalDate.parse(LocalDate.now().format(formatDate));
            fromDate = toDate.minusDays(2);
            Response<ResponseModel> response = scheduleDetailAPI.findScheduleDetailByDateBetweenAndTeacherId(token, fromDate.toString(), toDate.toString(), teacher.getId()).execute();
            if (response.isSuccessful()) {
                String scheduleDetailJson = gson.toJson(response.body().getData());
                Type type = new TypeToken<ArrayList<ScheduleDetail>>() {
                }.getType();
                listScheduleDetail = gson.fromJson(scheduleDetailJson, type);
            }
            if (!listScheduleDetail.isEmpty()) {
                onGetScheduleClass(listScheduleDetail);
            } else {
                Toast.makeText(getContext(), "Don't have schedule for 2 days ago from now", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            Toast.makeText(getContext(), "Don't find", Toast.LENGTH_LONG).show();
        }
    }

    public void onGetScheduleClass(List<ScheduleDetail> listScheduleDetail) {
        try {
            listSchedule = new ArrayList<>();
            for (ScheduleDetail scheduleDetail : listScheduleDetail) {
                Response<ResponseModel> response = scheduleAPI.getScheduleByScheduleDetail(token, scheduleDetail.getScheduleId()).execute();
                if (response.isSuccessful()) {
                    String scheduleJson = gson.toJson(response.body().getData());
                    Type type = new TypeToken<ArrayList<Schedule>>() {
                    }.getType();
                    listSchedule.addAll(gson.fromJson(scheduleJson, type));
                }
            }
            if (!listSchedule.isEmpty()) {
                onGetScheduleDetailBySchedule(listSchedule);
            } else {
                Toast.makeText(getContext(), "Don't have schedule", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            Toast.makeText(getContext(), "Don't find", Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("NewApi")
    public void onGetScheduleDetailBySchedule(List<Schedule> listSchedule) {
        listScheduleDetail = new ArrayList<>();
        for (Schedule schedule : listSchedule) {
            LocalDate startDate = LocalDate.parse(schedule.getStartDate(), formatDate);
            LocalDate endDate = LocalDate.parse(schedule.getEndDate(), formatDate);
            LocalDate currentDate = LocalDate.parse(LocalDate.now().format(formatDate));
            if (currentDate.isAfter(startDate) && currentDate.isBefore(endDate)) {
                try {
                    Response<ResponseModel> response = scheduleDetailAPI.findScheduleDetailsByDateBetweenAndScheduleIdAndTeacherId(token, fromDate.toString(), toDate.toString(), schedule.getId(), teacher.getId()).execute();
                    if (response.isSuccessful()) {
                        String scheduleDetailJson = gson.toJson(response.body().getData());
                        Type type = new TypeToken<ArrayList<ScheduleDetail>>() {
                        }.getType();
                        listScheduleDetail.addAll(gson.fromJson(scheduleDetailJson, type));
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Don't find", Toast.LENGTH_LONG).show();
                }
            }
        }
        if (!listScheduleDetail.isEmpty()) {
            onGetAttendance(listScheduleDetail);
        } else {
            Toast.makeText(getContext(), "Don't have schedule", Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("NewApi")
    public void onGetAttendance(List<ScheduleDetail> listScheduleDetail) {
        listAttendance = new ArrayList<>();
        Schedule schedule;
        Classses classses;
        try {
            for (ScheduleDetail scheduleDetail : listScheduleDetail) {
                listAttendance.clear();
                schedule = new Schedule();
                classses = new Classses();
                // Lấy schedule theo Schedule detail
                Response<ResponseModel> responseSchedule = scheduleAPI.getScheduleById(token, scheduleDetail.getScheduleId()).execute();
                if (responseSchedule.isSuccessful()) {
                    String scheduleJson = gson.toJson(responseSchedule.body().getData());
                    schedule = gson.fromJson(scheduleJson, Schedule.class);
                } else {
                    Toast.makeText(getContext(), "Don't find schedule", Toast.LENGTH_LONG).show();
                }
                // Lấy class theo schedule id
                Response<ResponseModel> responseClass = classAPI.getClassById(token, schedule.getClassId()).execute();
                if (responseClass.isSuccessful()) {
                    String classJson = gson.toJson(responseClass.body().getData());
                    classses = gson.fromJson(classJson, Classses.class);
                }

                Response<ResponseModel> response = attendanceAPI.findAttendanceByDateSlotAndShift(token, scheduleDetail.getDate(), scheduleDetail.getSlot(), classses.getShift()).execute();
                if (response.isSuccessful()) {
                    String attendanceJson = gson.toJson(response.body().getData());
                    Type type = new TypeToken<ArrayList<Attendance>>() {
                    }.getType();
                    listAttendance = gson.fromJson(attendanceJson, type);
                    if (!listAttendance.isEmpty()) {
                        takeAttendanceView = new TakeAttendanceView();
                        switch (classses.getShift().substring(0, 1)) {
                            case "M":
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
                                break;
                            case "A":
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
                                break;
                            case "E":
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
                                break;
                        }
                    }
                }
            }
            if (!listTakeAttendanceView.isEmpty()) {
                OnBindingData(listTakeAttendanceView);
            } else {
                Toast.makeText(getContext(), "Don't have any attendance for 2 days ago from now", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(getContext(), "Don't find", Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("NewApi")
    public void OnBindingData(List<TakeAttendanceView> listTakeAttendanceView) {
        Collections.sort(listTakeAttendanceView, Comparator.comparing(TakeAttendanceView::getDate));
        binding.editRcv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setData(listTakeAttendanceView);
        binding.editRcv.setAdapter(adapter);
    }

    public void onclickItem() {
        adapter.onClick(new ListTakeAttendanceAdapter.OnItemClick() {
            @Override
            public void onClickItem(TakeAttendanceView takeAttendanceView) {
                Intent intent = new Intent(getActivity(), EditAttendanceActivity.class);
                intent.putExtra("classId", takeAttendanceView.getId());
                startActivity(intent);
            }
        });
    }
}
