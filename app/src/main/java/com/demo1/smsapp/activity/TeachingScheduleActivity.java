package com.demo1.smsapp.activity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.demo1.smsapp.R;
import com.demo1.smsapp.adapter.ListTeachingScheduleAdapter;
import com.demo1.smsapp.api.ClassAPI;
import com.demo1.smsapp.api.ScheduleAPI;
import com.demo1.smsapp.api.ScheduleDetailAPI;
import com.demo1.smsapp.api.utils.APIUtils;
import com.demo1.smsapp.databinding.ActivityTeachingScheduleBinding;
import com.demo1.smsapp.databinding.ListTeachingScheduleBinding;
import com.demo1.smsapp.dto.*;
import com.demo1.smsapp.models.*;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

public class TeachingScheduleActivity extends AppCompatActivity {
    ActivityTeachingScheduleBinding binding;
    String teacherJson;
    String _token;
    Teacher teacher;
    Gson gson;
    ClassAPI classAPI;
    ScheduleDetailAPI scheduleDetailAPI;
    ScheduleAPI scheduleAPI;
    List<ScheduleDetail> scheduleDetails;
    ListTeachingScheduleAdapter listTeachingScheduleAdapter;
    Classses classses;
    List<TeachingScheduleModel> scheduleModels;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeachingScheduleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.red));
        gson = new Gson();
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("informationAccount", MODE_PRIVATE);
        _token = sharedPreferences.getString("token", null);
        teacherJson = sharedPreferences.getString("data", null);
        teacher = gson.fromJson(teacherJson, Teacher.class);
        classAPI = APIUtils.getClasses();
        scheduleDetailAPI = APIUtils.getScheduleDetail();
        scheduleAPI = APIUtils.getScheduleAPI();
        listTeachingScheduleAdapter = new ListTeachingScheduleAdapter();
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
        processBtnBack();
        getListSchedule();
//        getList();
        eventSelectDate();
    }


    private void processBtnBack() {
        binding.icBack.setOnClickListener(view -> {
            finish();
        });
    }

    private void getListSchedule() {
        scheduleDetails = new ArrayList<>();
        classses = new Classses();
        scheduleModels = new ArrayList<>();
        scheduleDetailAPI.findScheduleByTeacher(_token, teacher.getId()).enqueue(new Callback<ResponseModel>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()) {
                    String json = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<ArrayList<ScheduleDetail>>() {
                    }.getType();
                    scheduleDetails = gson.fromJson(json, listType);
                    LocalDate now = LocalDate.now();
                    Integer week = now.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
                    Integer year = now.getYear();
//                    for (ScheduleDetail scheduleDetail : scheduleDetails.stream().filter(scheduleDetail -> LocalDate.parse(scheduleDetail.getDate())
//                            .get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()) == week).collect(Collectors.toList())) {
//                        scheduleAPI.getScheduleByScheduleDetail(_token, scheduleDetail.getScheduleId()).enqueue(new Callback<ResponseModel>() {
//                            @Override
//                            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
//                                if (response.isSuccessful()) {
//                                    String json = gson.toJson(response.body().getData());
//                                    Type listType = new TypeToken<ArrayList<Schedule>>() {
//                                    }.getType();
//                                    List<Schedule> l = gson.fromJson(json, listType);
//                                    Schedule schedule = l.get(0);
//                                    classAPI.getClassById(_token, schedule.getClassId()).enqueue(new Callback<ResponseModel>() {
//                                        @Override
//                                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
//                                            if (response.isSuccessful()) {
//                                                String json = gson.toJson(response.body().getData());
//                                                classses = gson.fromJson(json, Classses.class);
//                                                TeachingScheduleModel scheduleModel = new TeachingScheduleModel();
//                                                scheduleModel.setDate(scheduleDetail.getDate());
//                                                scheduleModel.setSubject(scheduleDetail.getSubjectBySubjectId());
//                                                scheduleModel.setDayOfWeek(scheduleDetail.getDayOfWeek());
//                                                scheduleModel.setShift(classses.getShift());
//                                                scheduleModel.setSlot(scheduleDetail.getSlot());
//                                                scheduleModel.setRoomCode(classses.getClassRoom().getRoomCode());
//                                                scheduleModel.setClassCode(classses.getClassCode());
//                                                scheduleModels.add(scheduleModel);
//                                            }
//                                        }
//                                        @Override
//                                        public void onFailure(Call<ResponseModel> call, Throwable t) {
//
//                                        }
//                                    });
//                                }
//                            }
//                            @Override
//                            public void onFailure(Call<ResponseModel> call, Throwable t) {
//
//                            }
//                        });
//                    }
                    for (ScheduleDetail scheduleDetail : scheduleDetails.stream().filter(scheduleDetail -> LocalDate.parse(scheduleDetail.getDate())
                            .get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()) == week && LocalDate.parse(scheduleDetail.getDate()).getYear() == year).collect(Collectors.toList())) {
                        try {
                            Response<ResponseModel> responseSchedule = scheduleAPI.getScheduleByScheduleDetail(_token, scheduleDetail.getScheduleId()).execute();
                            if (responseSchedule.isSuccessful()) {
                                String jsonSchedule = gson.toJson(responseSchedule.body().getData());
                                Type collectionType = new TypeToken<ArrayList<Schedule>>() {
                                }.getType();
                                List<Schedule> l = gson.fromJson(jsonSchedule, collectionType);
                                Schedule schedule = l.get(0);
                                Response<ResponseModel> responseClass = classAPI.getClassById(_token, schedule.getClassId()).execute();
                                String jsonClass = gson.toJson(responseClass.body().getData());
                                classses = gson.fromJson(jsonClass, Classses.class);
                                TeachingScheduleModel scheduleModel = new TeachingScheduleModel();
                                scheduleModel.setDate(scheduleDetail.getDate());
                                scheduleModel.setSubject(scheduleDetail.getSubjectBySubjectId());
                                scheduleModel.setDayOfWeek(scheduleDetail.getDayOfWeek());
                                scheduleModel.setShift(classses.getShift());
                                scheduleModel.setDepartmentCode(classses.getDepartmentByDepartmentId().getDepartmentCode());
                                scheduleModel.setSlot(scheduleDetail.getSlot());
                                scheduleModel.setRoomCode(classses.getClassRoom().getRoomCode());
                                scheduleModel.setClassCode(classses.getClassCode());
                                scheduleModels.add(scheduleModel);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    HashMap<String, List<TeachingScheduleModel>> hashMap = new HashMap<>();
                    for (TeachingScheduleModel teachingScheduleModel : scheduleModels) {
                        String key = teachingScheduleModel.getDate();
                        if (hashMap.containsKey(key)) {
                            List<TeachingScheduleModel> list = hashMap.get(key);
                            list.add(teachingScheduleModel);
                        } else {
                            List<TeachingScheduleModel> list = new ArrayList<>();
                            list.add(teachingScheduleModel);
                            hashMap.put(key, list);
                        }

                    }

                    SortedSet<String> keys = new TreeSet<>(hashMap.keySet());
                    List<TeachingScheduleGroupDTO> scheduleGroupDTOList = new ArrayList<>();
                    for (String key : keys) {
                        List<TeachingScheduleModel> list = hashMap.get(key);
                        TeachingScheduleGroupDTO teachingScheduleGroupDTO = new TeachingScheduleGroupDTO();
                        teachingScheduleGroupDTO.setDate(LocalDate.parse(key));
                        teachingScheduleGroupDTO.setTeachingScheduleModels(list);
                        scheduleGroupDTOList.add(teachingScheduleGroupDTO);

                    }

                    listTeachingScheduleAdapter.setData(scheduleGroupDTOList);
                    binding.rcvTeachingSchedule.setAdapter(listTeachingScheduleAdapter);
                    binding.rcvTeachingSchedule.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

            }
        });
    }

//    public void getList(){
//        List<TeachingScheduleModel> list = scheduleModels;
//    }

    private void eventSelectDate() {
        binding.datePicker.setOnSelectionChanged(new Function1<Date, Unit>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public Unit invoke(Date date) {
                Integer week = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
                Integer year = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
                List<TeachingScheduleModel> scheduleModels = new ArrayList<>();
                for (ScheduleDetail scheduleDetail : scheduleDetails.stream().filter(scheduleDetail -> LocalDate.parse(scheduleDetail.getDate())
                        .get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()) == week && LocalDate.parse(scheduleDetail.getDate()).getYear() == year).collect(Collectors.toList())) {
                    try {
                        Response<ResponseModel> responseSchedule = scheduleAPI.getScheduleByScheduleDetail(_token, scheduleDetail.getScheduleId()).execute();
                        if (responseSchedule.isSuccessful()) {
                            String jsonSchedule = gson.toJson(responseSchedule.body().getData());
                            Type collectionType = new TypeToken<ArrayList<Schedule>>() {
                            }.getType();
                            List<Schedule> l = gson.fromJson(jsonSchedule, collectionType);
                            Schedule schedule = l.get(0);
                            Response<ResponseModel> responseClass = classAPI.getClassById(_token, schedule.getClassId()).execute();
                            String jsonClass = gson.toJson(responseClass.body().getData());
                            classses = gson.fromJson(jsonClass, Classses.class);
                            TeachingScheduleModel scheduleModel = new TeachingScheduleModel();
                            scheduleModel.setDate(scheduleDetail.getDate());
                            scheduleModel.setSubject(scheduleDetail.getSubjectBySubjectId());
                            scheduleModel.setDayOfWeek(scheduleDetail.getDayOfWeek());
                            scheduleModel.setShift(classses.getShift());
                            scheduleModel.setDepartmentCode(classses.getDepartmentByDepartmentId().getDepartmentCode());
                            scheduleModel.setSlot(scheduleDetail.getSlot());
                            scheduleModel.setRoomCode(classses.getClassRoom().getRoomCode());
                            scheduleModel.setClassCode(classses.getClassCode());
                            scheduleModels.add(scheduleModel);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                HashMap<String, List<TeachingScheduleModel>> hashMap = new HashMap<>();
                for (TeachingScheduleModel teachingScheduleModel : scheduleModels) {
                    String key = teachingScheduleModel.getDate();
                    if (hashMap.containsKey(key)) {
                        List<TeachingScheduleModel> list = hashMap.get(key);
                        list.add(teachingScheduleModel);
                    } else {
                        List<TeachingScheduleModel> list = new ArrayList<>();
                        list.add(teachingScheduleModel);
                        hashMap.put(key, list);
                    }

                }

                SortedSet<String> keys = new TreeSet<>(hashMap.keySet());
                List<TeachingScheduleGroupDTO> scheduleGroupDTOList = new ArrayList<>();
                for (String key : keys) {
                    List<TeachingScheduleModel> list = hashMap.get(key);
                    TeachingScheduleGroupDTO teachingScheduleGroupDTO = new TeachingScheduleGroupDTO();
                    teachingScheduleGroupDTO.setDate(LocalDate.parse(key));
                    teachingScheduleGroupDTO.setTeachingScheduleModels(list);
                    scheduleGroupDTOList.add(teachingScheduleGroupDTO);

                }

                listTeachingScheduleAdapter.setData(scheduleGroupDTOList);
                binding.rcvTeachingSchedule.setAdapter(listTeachingScheduleAdapter);
                binding.rcvTeachingSchedule.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                return null;
            }
        });
    }
}