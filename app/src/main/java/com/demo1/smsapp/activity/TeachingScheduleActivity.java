package com.demo1.smsapp.activity;

import android.content.SharedPreferences;
import android.os.Build;
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
import com.demo1.smsapp.api.utils.APIUtils;
import com.demo1.smsapp.databinding.ActivityTeachingScheduleBinding;
import com.demo1.smsapp.databinding.ListTeachingScheduleBinding;
import com.demo1.smsapp.dto.ResponseModel;
import com.demo1.smsapp.dto.ScheduleDetailModel;
import com.demo1.smsapp.dto.ScheduleGroupDTO;
import com.demo1.smsapp.models.*;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    List<ScheduleGroupDTO> groupDTOList;
    List<Classses> listClass;
    ListTeachingScheduleAdapter listTeachingScheduleAdapter;
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
        teacherJson =sharedPreferences.getString("data", null);
        teacher= gson.fromJson(teacherJson,Teacher.class);
        classAPI= APIUtils.getClasses();
        listTeachingScheduleAdapter= new ListTeachingScheduleAdapter();
        processBtnBack();
        getListSchedule();
        eventSelectDate();
    }


    private void processBtnBack() {
        binding.icBack.setOnClickListener(view -> {
            finish();
        });
    }

    private void getListSchedule() {
        listClass = new ArrayList<>();
        classAPI.findClassByTeacher(_token, teacher.getId()).enqueue(new Callback<ResponseModel>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
               if(response.isSuccessful()){
                   groupDTOList = new ArrayList<>();
                   String json = gson.toJson(response.body().getData());
                   Type listType = new TypeToken<ArrayList<Classses>>() {
                   }.getType();
                   listClass = gson.fromJson(json,listType);
                   LocalDate now = LocalDate.now();
                   Integer week = now.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
                   List<ScheduleDetail> newList = new ArrayList<>();
                   for(Classses classses:listClass){
                       HashMap<String,List<ScheduleDetail>> hashMap = new HashMap<>();
                       for (Schedule schedule : classses.getSchedulesById()) {

                           for (ScheduleDetail scheduleDetails : schedule.getScheduleDetailsById()) {
                               if (LocalDate.parse(scheduleDetails.getDate())
                                       .get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()) == week) {
                                   String key = scheduleDetails.getDate();
                                   if(hashMap.containsKey(key)){
                                       List<ScheduleDetail> list = hashMap.get(key);
                                       list.add(scheduleDetails);

                                   }else{
                                       List<ScheduleDetail> list = new ArrayList<ScheduleDetail>();
                                       list.add(scheduleDetails);
                                       hashMap.put(key, list);
                                   }
                                   newList.add(scheduleDetails);
                               }
                           }
                       }
                       SortedSet<String> keys = new TreeSet<>(hashMap.keySet());
                       String subShift = classses.getShift().substring(0,1);
                       for(String key : keys){
                           List<ScheduleDetail> scheduleDetailList1 = hashMap.get(key);
                           List<ScheduleDetailModel> listTemp= new ArrayList<>();
                           for(ScheduleDetail scheduleDetail : scheduleDetailList1){
                               ScheduleDetailModel scheduleDetailModel = new ScheduleDetailModel();
                               scheduleDetailModel.setDate(scheduleDetail.getDate());
                               scheduleDetailModel.setId(scheduleDetail.getId());
                               scheduleDetailModel.setScheduleId(scheduleDetail.getSubjectBySubjectId().getId());
                               scheduleDetailModel.setScheduleId(scheduleDetail.getScheduleId());
                               scheduleDetailModel.setSubjectBySubjectId(scheduleDetail.getSubjectBySubjectId());
                               scheduleDetailModel.setScheduleByScheduleId(scheduleDetail.getScheduleByScheduleId());
                               scheduleDetailModel.setClassName(classses.getClassCode());
                               scheduleDetailModel.setSlot(scheduleDetail.getSlot());
                               if(subShift.equals("M")){
                                   if(scheduleDetail.getSlot().equals(1)){
                                       scheduleDetailModel.setTimeStart("7:30");
                                       scheduleDetailModel.setTimeEnd("9:30");
                                   }else{
                                       scheduleDetailModel.setTimeStart("9:30");
                                       scheduleDetailModel.setTimeEnd("11:30");
                                   }
                               } else if (subShift.equals("A")) {
                                   if(scheduleDetail.getSlot().equals(1)){
                                       scheduleDetailModel.setTimeStart("12:30");
                                       scheduleDetailModel.setTimeEnd("15:30");
                                   }else{
                                       scheduleDetailModel.setTimeStart("15:30");
                                       scheduleDetailModel.setTimeEnd("17:30");
                                   }
                               }else{
                                   if(scheduleDetail.getSlot().equals(1)){
                                       scheduleDetailModel.setTimeStart("17:30");
                                       scheduleDetailModel.setTimeEnd("19:30");
                                   }else{
                                       scheduleDetailModel.setTimeStart("19:30");
                                       scheduleDetailModel.setTimeEnd("21:30");
                                   }
                               }
                               scheduleDetailModel.setDayOfWeek(scheduleDetail.getDayOfWeek());
                               scheduleDetailModel.setTeacherName(classses.getTeacher().getProfileByProfileId().getLastName());
                               listTemp.add(scheduleDetailModel);
                           }
                           ScheduleGroupDTO scheduleGroupDTO = new ScheduleGroupDTO();
                           scheduleGroupDTO.setDate(LocalDate.parse(key));
                           scheduleGroupDTO.setList(listTemp);
                           groupDTOList.add(scheduleGroupDTO);
                       }
                   }

                   HashMap<String,List<ScheduleGroupDTO>> hashMap = new HashMap<>();
                   for (ScheduleGroupDTO scheduleGroupDTO : groupDTOList){
                       String key = scheduleGroupDTO.getDate().toString();
                       if(hashMap.containsKey(key)){
                           List<ScheduleGroupDTO> scheduleGroupDTOS = hashMap.get(key);
                           scheduleGroupDTOS.add(scheduleGroupDTO);
                       }else{
                           List<ScheduleGroupDTO> list = new ArrayList<ScheduleGroupDTO>();
                           list.add(scheduleGroupDTO);
                           hashMap.put(key, list);
                       }
                   }

                   SortedSet<String> keys = new TreeSet<>(hashMap.keySet());
                   List<ScheduleGroupDTO> groupDTOS = new ArrayList<>();
                   List<ScheduleGroupDTO> list = new ArrayList<>();
                   for(String key : keys){
                       list = hashMap.get(key);
                       List<ScheduleDetailModel> listTemp= new ArrayList<>();
                       ScheduleGroupDTO sc = new ScheduleGroupDTO();
                       sc.setDate(LocalDate.parse(key));
                       for (ScheduleGroupDTO scheduleGroupDTO:list){
                           for (ScheduleDetailModel model:scheduleGroupDTO.getList()){
                               listTemp.add(model);
                           }
                       }
                       sc.setList(listTemp);
                       groupDTOS.add(sc);
                   }


                   listTeachingScheduleAdapter.setData(groupDTOS);
                   binding.rcvTeachingSchedule.setAdapter(listTeachingScheduleAdapter);
                   binding.rcvTeachingSchedule.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

               }
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e("error",t.getMessage());
            }
        });

    }

    private void eventSelectDate() {
        binding.datePicker.setOnSelectionChanged(new Function1<Date, Unit>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public Unit invoke(Date date) {
                groupDTOList = new ArrayList<>();
                Integer week  = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
                List<ScheduleDetail> newList = new ArrayList<>();
                for(Classses classses:listClass){
                    HashMap<String,List<ScheduleDetail>> hashMap = new HashMap<>();
                    for (Schedule schedule : classses.getSchedulesById()) {
                        for (ScheduleDetail scheduleDetails : schedule.getScheduleDetailsById()) {
                            if (LocalDate.parse(scheduleDetails.getDate())
                                    .get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()) == week) {
                                String key = scheduleDetails.getDate();
                                if(hashMap.containsKey(key)){
                                    List<ScheduleDetail> list = hashMap.get(key);
                                    list.add(scheduleDetails);

                                }else{
                                    List<ScheduleDetail> list = new ArrayList<ScheduleDetail>();
                                    list.add(scheduleDetails);
                                    hashMap.put(key, list);
                                }
                                newList.add(scheduleDetails);
                            }
                        }
                    }
                    SortedSet<String> keys = new TreeSet<>(hashMap.keySet());
                    String subShift = classses.getShift().substring(0,1);
                    for(String key : keys){
                        List<ScheduleDetail> scheduleDetailList1 = hashMap.get(key);
                        List<ScheduleDetailModel> listTemp= new ArrayList<>();
                        for(ScheduleDetail scheduleDetail : scheduleDetailList1){
                            ScheduleDetailModel scheduleDetailModel = new ScheduleDetailModel();
                            scheduleDetailModel.setDate(scheduleDetail.getDate());
                            scheduleDetailModel.setId(scheduleDetail.getId());
                            scheduleDetailModel.setScheduleId(scheduleDetail.getSubjectBySubjectId().getId());
                            scheduleDetailModel.setScheduleId(scheduleDetail.getScheduleId());
                            scheduleDetailModel.setSubjectBySubjectId(scheduleDetail.getSubjectBySubjectId());
                            scheduleDetailModel.setScheduleByScheduleId(scheduleDetail.getScheduleByScheduleId());
                            scheduleDetailModel.setClassName(classses.getClassCode());
                            scheduleDetailModel.setSlot(scheduleDetail.getSlot());
                            if(subShift.equals("M")){
                                if(scheduleDetail.getSlot().equals(1)){
                                    scheduleDetailModel.setTimeStart("7:30");
                                    scheduleDetailModel.setTimeEnd("9:30");
                                }else{
                                    scheduleDetailModel.setTimeStart("9:30");
                                    scheduleDetailModel.setTimeEnd("11:30");
                                }
                            } else if (subShift.equals("A")) {
                                if(scheduleDetail.getSlot().equals(1)){
                                    scheduleDetailModel.setTimeStart("12:30");
                                    scheduleDetailModel.setTimeEnd("15:30");
                                }else{
                                    scheduleDetailModel.setTimeStart("15:30");
                                    scheduleDetailModel.setTimeEnd("17:30");
                                }
                            }else{
                                if(scheduleDetail.getSlot().equals(1)){
                                    scheduleDetailModel.setTimeStart("17:30");
                                    scheduleDetailModel.setTimeEnd("19:30");
                                }else{
                                    scheduleDetailModel.setTimeStart("19:30");
                                    scheduleDetailModel.setTimeEnd("21:30");
                                }
                            }
                            scheduleDetailModel.setDayOfWeek(scheduleDetail.getDayOfWeek());
                            scheduleDetailModel.setTeacherName(classses.getTeacher().getProfileByProfileId().getLastName());
                            listTemp.add(scheduleDetailModel);
                        }
                        ScheduleGroupDTO scheduleGroupDTO = new ScheduleGroupDTO();
                        scheduleGroupDTO.setDate(LocalDate.parse(key));
                        scheduleGroupDTO.setList(listTemp);
                        groupDTOList.add(scheduleGroupDTO);
                    }
                }

                HashMap<String,List<ScheduleGroupDTO>> hashMap = new HashMap<>();
                for (ScheduleGroupDTO scheduleGroupDTO : groupDTOList){
                    String key = scheduleGroupDTO.getDate().toString();
                    if(hashMap.containsKey(key)){
                        List<ScheduleGroupDTO> scheduleGroupDTOS = hashMap.get(key);
                        scheduleGroupDTOS.add(scheduleGroupDTO);
                    }else{
                        List<ScheduleGroupDTO> list = new ArrayList<ScheduleGroupDTO>();
                        list.add(scheduleGroupDTO);
                        hashMap.put(key, list);
                    }
                }

                SortedSet<String> keys = new TreeSet<>(hashMap.keySet());
                List<ScheduleGroupDTO> groupDTOS = new ArrayList<>();
                List<ScheduleGroupDTO> list = new ArrayList<>();
                for(String key : keys){
                    list = hashMap.get(key);
                    List<ScheduleDetailModel> listTemp= new ArrayList<>();
                    ScheduleGroupDTO sc = new ScheduleGroupDTO();
                    sc.setDate(LocalDate.parse(key));
                    for (ScheduleGroupDTO scheduleGroupDTO:list){
                        for (ScheduleDetailModel model:scheduleGroupDTO.getList()){
                            listTemp.add(model);
                        }
                    }
                    sc.setList(listTemp);
                    groupDTOS.add(sc);
                }

                listTeachingScheduleAdapter.setData(groupDTOS);
                binding.rcvTeachingSchedule.setAdapter(listTeachingScheduleAdapter);
                binding.rcvTeachingSchedule.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                return null;
            }
        });
    }
}