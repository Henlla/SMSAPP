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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EditAttendanceFragment extends Fragment {
    FragmentEditAttendanceBinding binding;
    String token, teacherJson, date;
    Teacher teacher;
    Gson gson;

//    String mSTime = "07:30", mETime = "11:30";
//    String aSTime = "12:30", aETime = "17:30";
//    String eSTime = "17:30", eETime = "21:30";

//    String mSTime = "00:00", mETime = "23:59";
//    String aSTime = "00:00", aETime = "23:59";
//    String eSTime = "00:00", eETime = "23:59";

    String toDate, fromDate;

    LocalTime startTime;
    LocalTime endTime;
    LocalTime onTime;
    DateTimeFormatter format;
    String currentTime;

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

    @SuppressLint("NewApi")
    public void init() {
        attendanceActivity = (TeacherAttendanceActivity) getActivity();
        gson = new Gson();

        listStudentSubject = new ArrayList<>();
        listTakeAttendanceView = new ArrayList<>();
        listClass = new ArrayList<>();
        listStudentClass = new ArrayList<>();

        adapter = new ListTakeAttendanceAdapter();
        format = DateTimeFormatter.ofPattern("HH:mm");
        int hour = LocalDateTime.now().getHour();
        int minute = LocalDateTime.now().getMinute();
        currentTime = (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute);
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
            toDate = ConvertDayOfWeek.dateFormat(LocalDate.now(), "yyyy-mm-dd");
            String[] date = toDate.split("-");
            fromDate = ConvertDayOfWeek.dateFormat(LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]) - 2), "yyyy-mm-dd");

            Response<ResponseModel> response = scheduleDetailAPI.findScheduleDetailByDateBetween(token, fromDate, toDate).execute();
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
                onGetClass(listSchedule);
            } else {
                Toast.makeText(getContext(), "Don't have schedule", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            Toast.makeText(getContext(), "Don't find", Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("NewApi")
    public void onGetClass(List<Schedule> scheduleList) {
        List<Integer> listClassId = scheduleList.stream().map(Schedule::getClassId).distinct().collect(Collectors.toList());
        listClass = new ArrayList<>();
        try {
            for (Integer classId : listClassId) {
                Response<ResponseModel> response = classAPI.findClassByTeacherAndSchedule(token, teacher.getId(), classId).execute();
                if (response.isSuccessful()) {
                    String classJson = gson.toJson(response.body().getData());
                    Classses classses = gson.fromJson(classJson, Classses.class);
                    if (classses != null) {
                        listClass.add(classses);
                    }
                }
            }
            if (!listClass.isEmpty()) {
                onGetSchedule(listClass);
            } else {
                Toast.makeText(getContext(), "Don't have class", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            Toast.makeText(getContext(), "Don't find", Toast.LENGTH_LONG).show();
        }
    }

    public void onGetSchedule(List<Classses> listClass) {
        try {
            listSchedule = new ArrayList<>();
            for (Classses classses : listClass) {
                Response<ResponseModel> response = scheduleAPI.getScheduleByClass(token, classses.getId()).execute();
                if (response.isSuccessful()) {
                    String scheduleJson = gson.toJson(response.body().getData());
                    Type type = new TypeToken<ArrayList<Schedule>>() {
                    }.getType();
                    listSchedule.addAll(gson.fromJson(scheduleJson, type));
                } else {
                    Toast.makeText(getContext(), "Don't find schedule", Toast.LENGTH_LONG).show();
                }
            }
            if (!listSchedule.isEmpty()) {
                onGetScheduleDetailBySchedule(listSchedule, listClass);
            } else {
                Toast.makeText(getContext(), "Don't have schedule", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Don't find", Toast.LENGTH_LONG).show();
        }

    }

    @SuppressLint("NewApi")
    public void onGetScheduleDetailBySchedule(List<Schedule> listSchedule, List<Classses> listClass) {
        listScheduleDetail = new ArrayList<>();
        for (Schedule schedule : listSchedule) {
            String day = LocalDate.now().getDayOfMonth() < 10 ? "0" + LocalDate.now().getDayOfMonth() : String.valueOf(LocalDate.now().getDayOfMonth());
            String month = LocalDate.now().getMonthValue() < 10 ? "0" + LocalDate.now().getMonthValue() : String.valueOf(LocalDate.now().getMonthValue());
            String year = String.valueOf(LocalDate.now().getYear());
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(schedule.getStartDate(), format);
            LocalDate endDate = LocalDate.parse(schedule.getEndDate(), format);
            LocalDate currentDate = LocalDate.parse(year + "-" + month + "-" + day, format);
            if (currentDate.isAfter(startDate) && currentDate.isBefore(endDate)) {
                try {
                    Response<ResponseModel> response = scheduleDetailAPI.findScheduleByDateBetweenAndScheduleId(token, fromDate, toDate, schedule.getId()).execute();
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
            onGetAttendance(listScheduleDetail, listClass);
        } else {
            Toast.makeText(getContext(), "Don't have schedule", Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("NewApi")
    public void onGetAttendance(List<ScheduleDetail> listScheduleDetail, List<Classses> listClass) {
        listAttendance = new ArrayList<>();
        try {
            for (ScheduleDetail scheduleDetail : listScheduleDetail) {
                listAttendance.clear();
                for (Classses classses : listClass) {
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
//                                    startTime = LocalTime.parse(mSTime, format);
//                                    endTime = LocalTime.parse(mETime, format);
//                                    onTime = LocalTime.parse(currentTime, format);
//                                    if (onTime.isAfter(startTime) && onTime.isBefore(endTime)) {
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
//                                    } else {
//                                        isOnTime = false;
//                                    }
                                    break;
                                case "A":
//                                    startTime = LocalTime.parse(aSTime, format);
//                                    endTime = LocalTime.parse(aETime, format);
//                                    onTime = LocalTime.parse(currentTime, format);
//                                    if (onTime.isAfter(startTime) && onTime.isBefore(endTime)) {
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
//                                    } else {
//                                        isOnTime = false;
//                                    }
                                    break;
                                case "E":
//                                    startTime = LocalTime.parse(eSTime, format);
//                                    endTime = LocalTime.parse(eETime, format);
//                                    onTime = LocalTime.parse(currentTime, format);
//                                    if (onTime.isAfter(startTime) && onTime.isBefore(endTime)) {
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
//                                    } else {
//                                        isOnTime = false;
//                                    }
                                    break;
                            }
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
