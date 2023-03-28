package com.demo1.smsapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.demo1.smsapp.R;
import com.demo1.smsapp.adapter.ListScheduleAdapter;
import com.demo1.smsapp.api.ClassAPI;
import com.demo1.smsapp.api.ScheduleAPI;
import com.demo1.smsapp.api.StudentClassAPI;
import com.demo1.smsapp.api.SubjectAPI;
import com.demo1.smsapp.api.utils.APIUtils;
import com.demo1.smsapp.databinding.ActivityTimetableBinding;
import com.demo1.smsapp.databinding.CalendarSheetModelBinding;
import com.demo1.smsapp.dto.ResponseModel;
import com.demo1.smsapp.dto.ScheduleDetailModel;
import com.demo1.smsapp.dto.ScheduleGroupDTO;
import com.demo1.smsapp.fragment.CalendarBottomSheetModel;
import com.demo1.smsapp.models.*;
import com.demo1.smsapp.utils.ConvertDayOfWeek;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpStatus;
import com.google.gson.Gson;
import dev.shreyaspatil.MaterialDialog.AbstractDialog;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

public class TimetableActivity extends AppCompatActivity implements CalendarBottomSheetModel.ISetOnclick {
    ActivityTimetableBinding timetableBinding;
    Student student;
    String _token;
    Classses classses;
    String studentJson;
    Gson gson;
    ClassAPI classAPI;
    StudentClassAPI studentClassAPI;
    List<String> listSemester;
    List<String> week;
    ScheduleAPI scheduleAPI;
    ListScheduleAdapter listScheduleAdapter;
    MaterialDialog materialDialog;
    List<ScheduleGroupDTO> groupDTOList;
    List<String> listSubject;
    SubjectAPI subjectAPI;
    List<Subject> list;
    List<String> listClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timetableBinding = ActivityTimetableBinding.inflate(getLayoutInflater());
        setContentView(timetableBinding.getRoot());
        gson = new Gson();
        studentClassAPI = APIUtils.getStudentClass();
        classAPI = APIUtils.getClasses();
        listScheduleAdapter = new ListScheduleAdapter();
        scheduleAPI = APIUtils.getScheduleAPI();
        Window window = this.getWindow();
        subjectAPI = APIUtils.getSubject();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.red));
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("informationAccount", MODE_PRIVATE);
        studentJson = sharedPreferences.getString("data", null);
        _token = sharedPreferences.getString("token", null);
        student = gson.fromJson(studentJson, Student.class);
        CalendarBottomSheetModel calendarBottomSheetModel = new CalendarBottomSheetModel(this);
        getClassId();
        processBtnBack();
        setListSpinClass();
        setListSpinSemester();
        setListSpinWeek();
        setDataSelectClass();
        setDataSelectSemester();
        setDataSelectWeek();
        setDataSelectSubject();
        eventClickCalendar();
    }

    private void eventClickCalendar() {
        timetableBinding.calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarBottomSheetModel calendarBottomSheetModel = new CalendarBottomSheetModel();
                calendarBottomSheetModel.show(getSupportFragmentManager(),calendarBottomSheetModel.getTag());
            }
        });
    }

    private void setDataSelectClass() {
        timetableBinding.cbxClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = listClass.get(i);
                classAPI.findClassCode(_token,value).enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        String jsonClass = gson.toJson(response.body().getData());
                        classses = gson.fromJson(jsonClass,Classses.class);
                        timetableBinding.cbxSemester.setVisibility(View.VISIBLE);
                        timetableBinding.cbxSubject.setVisibility(View.VISIBLE);
                        timetableBinding.cbxWeek.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setListSpinClass() {
        timetableBinding.cbxClass.setItem(listClass);
    }

    private void setDataSelectSubject() {
        timetableBinding.cbxSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    listScheduleAdapter.setList(groupDTOList);
                    timetableBinding.rcvListSchedule.setAdapter(listScheduleAdapter);
                    timetableBinding.rcvListSchedule.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                }else{
                    String subjectName = listSubject.get(i);
                    Integer subjectId = null;
                    for (Subject sb:list.stream().filter(subject1 -> subject1.getSubjectName().equals(subjectName)).collect(Collectors.toList())){
                        subjectId = sb.getId();
                    }
                    Integer finalSubjectId = subjectId;
                    boolean flag;
                    List<ScheduleGroupDTO> dtoList = new ArrayList<>();
                    for (ScheduleGroupDTO scheduleGroupDTO : groupDTOList){
                        ScheduleGroupDTO scheduleDTO = new ScheduleGroupDTO();
                        flag = false;
                        List<ScheduleDetailModel> scheduleDetailModelList = new ArrayList<>();
                        for (ScheduleDetailModel scheduleDetailModel : scheduleGroupDTO.getList()){
                            if(scheduleDetailModel.getSubjectBySubjectId().getId() == finalSubjectId){
                                scheduleDetailModelList.add(scheduleDetailModel);
                                flag =true;
                            }
                        }
                        if(flag){
                            scheduleDTO.setDate(scheduleGroupDTO.getDate());
                            scheduleDTO.setList(scheduleDetailModelList);
                            dtoList.add(scheduleDTO);
                        }
                    }
                    listScheduleAdapter.setList(dtoList);
                    timetableBinding.rcvListSchedule.setAdapter(listScheduleAdapter);
                    timetableBinding.rcvListSchedule.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    DividerItemDecoration itemDecorator = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
                    timetableBinding.rcvListSchedule.addItemDecoration(itemDecorator);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setDataSelectWeek() {
        timetableBinding.cbxWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                List<ScheduleGroupDTO> listRangeDate = new ArrayList<>();
                if (i == 0) {
                    int weekOfYear = LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
                    int year = LocalDate.now().getYear();
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.DAY_OF_MONTH, Calendar.MONDAY);
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.WEEK_OF_YEAR, weekOfYear);
                    Date date1 = cal.getTime();
                    cal.add(Calendar.DATE, 6);
                    Date date2 = cal.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    LocalDate startDate = LocalDate.parse(sdf.format(date1));
                    LocalDate endDate = LocalDate.parse(sdf.format(date2));
                    listRangeDate = groupDTOList.stream().filter(scheduleGroupDTO -> scheduleGroupDTO.getDate().isAfter(startDate)
                            && scheduleGroupDTO.getDate().isBefore(endDate))
                            .collect(Collectors.toList());
                    if (listRangeDate.isEmpty()) {
                        materialDialog = new MaterialDialog.Builder(TimetableActivity.this)
                                .setMessage("Không có dữ liệu")
                                .setCancelable(false)
                                .setPositiveButton("", R.drawable.done, new MaterialDialog.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {
                                        materialDialog.dismiss();
                                    }
                                }).build();
                        materialDialog.show();
                    } else {
                        listScheduleAdapter.setList(listRangeDate);
                        timetableBinding.rcvListSchedule.setAdapter(listScheduleAdapter);
                        timetableBinding.rcvListSchedule.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    }
                } else if (i == 1) {
                    int weekOfYear = LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()) + 1;
                    int year = LocalDate.now().getYear();
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.DAY_OF_MONTH, Calendar.MONDAY);
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.WEEK_OF_YEAR, weekOfYear);
                    Date date1 = cal.getTime();
                    cal.add(Calendar.DATE, 6);
                    Date date2 = cal.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    LocalDate startDate = LocalDate.parse(sdf.format(date1));
                    LocalDate endDate = LocalDate.parse(sdf.format(date2));
                    listRangeDate = groupDTOList.stream().filter(scheduleGroupDTO -> scheduleGroupDTO.getDate().isAfter(startDate)
                            && scheduleGroupDTO.getDate().isBefore(endDate))
                            .collect(Collectors.toList());
                    if (listRangeDate.isEmpty()) {
                        materialDialog = new MaterialDialog.Builder(TimetableActivity.this)
                                .setMessage("Không có dữ liệu")
                                .setCancelable(false)
                                .setPositiveButton("", R.drawable.done, new MaterialDialog.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {
                                        materialDialog.dismiss();
                                    }
                                }).build();
                        materialDialog.show();
                    } else {
                        listScheduleAdapter.setList(listRangeDate);
                        timetableBinding.rcvListSchedule.setAdapter(listScheduleAdapter);
                        timetableBinding.rcvListSchedule.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    }
                }else if(i == 2){
                    int monthOfYear = LocalDate.now().getMonthValue();
                    listRangeDate = groupDTOList.stream().filter(scheduleGroupDTO -> scheduleGroupDTO.getDate().getMonthValue() == monthOfYear)
                            .collect(Collectors.toList());
                    if (listRangeDate.isEmpty()) {
                        materialDialog = new MaterialDialog.Builder(TimetableActivity.this)
                                .setMessage("Không có dữ liệu")
                                .setCancelable(false)
                                .setPositiveButton("", R.drawable.done, new MaterialDialog.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {
                                        materialDialog.dismiss();
                                    }
                                }).build();
                        materialDialog.show();
                    } else {
                        listScheduleAdapter.setList(listRangeDate);
                        timetableBinding.rcvListSchedule.setAdapter(listScheduleAdapter);
                        timetableBinding.rcvListSchedule.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    }
                }else if(i == 3){
                    int monthOfYear = LocalDate.now().getMonthValue() +1;
                    listRangeDate = groupDTOList.stream().filter(scheduleGroupDTO -> scheduleGroupDTO.getDate().getMonthValue() == monthOfYear)
                            .collect(Collectors.toList());
                    if (listRangeDate.isEmpty()) {
                        materialDialog = new MaterialDialog.Builder(TimetableActivity.this)
                                .setMessage("Không có dữ liệu")
                                .setCancelable(false)
                                .setPositiveButton("", R.drawable.done, new MaterialDialog.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {
                                        materialDialog.dismiss();
                                    }
                                }).build();
                        materialDialog.show();
                    } else {
                        listScheduleAdapter.setList(listRangeDate);
                        timetableBinding.rcvListSchedule.setAdapter(listScheduleAdapter);
                        timetableBinding.rcvListSchedule.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setDataSelectSemester() {
        timetableBinding.cbxSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                scheduleAPI.getScheduleByClassAndSemester(_token, classses.getId(), i + 1).enqueue(new Callback<ResponseModel>() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        groupDTOList = new ArrayList<>();
                        if (response.isSuccessful()) {
                            timetableBinding.cbxSubject.setVisibility(View.VISIBLE);
                            timetableBinding.cbxWeek.setVisibility(View.VISIBLE);
                            timetableBinding.calendar.setVisibility(View.VISIBLE);
                            String json = gson.toJson(response.body().getData());
                            Schedule schedule = gson.fromJson(json, Schedule.class);
                            if(schedule != null){
                                List<ScheduleDetail> scheduleDetailList = schedule.getScheduleDetailsById().stream().sorted((a, b) -> LocalDate.parse(a.getDate()).compareTo(LocalDate.parse(b.getDate()))).collect(Collectors.toList());
                                HashMap<String,List<ScheduleDetail>> hashMap = new HashMap<>();

                                for (ScheduleDetail scheduleDetail : scheduleDetailList){
                                    String key = scheduleDetail.getDate();
                                    if(hashMap.containsKey(key)){
                                        List<ScheduleDetail> list = hashMap.get(key);
                                        list.add(scheduleDetail);

                                    }else{
                                        List<ScheduleDetail> list = new ArrayList<ScheduleDetail>();
                                        list.add(scheduleDetail);
                                        hashMap.put(key, list);
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
                                        scheduleDetailModel.setDepartmentCode(classses.getDepartmentByDepartmentId().getDepartmentCode());
                                        scheduleDetailModel.setSubjectId(scheduleDetail.getSubjectBySubjectId().getId());
                                        scheduleDetailModel.setScheduleId(scheduleDetail.getScheduleId());
                                        scheduleDetailModel.setSubjectBySubjectId(scheduleDetail.getSubjectBySubjectId());
                                        scheduleDetailModel.setScheduleByScheduleId(scheduleDetail.getScheduleByScheduleId());
                                        scheduleDetailModel.setRoomCode(classses.getClassRoom().getRoomCode());
                                        scheduleDetailModel.setClassName(classses.getClassCode());
                                        scheduleDetailModel.setSlot(scheduleDetail.getSlot());
                                        if(subShift.equals("M")){
                                            if(scheduleDetail.getSlot().equals(1)){
                                                scheduleDetailModel.setTimeStart("8:00");
                                                scheduleDetailModel.setTimeEnd("10:00");
                                            }else{
                                                scheduleDetailModel.setTimeStart("10:00");
                                                scheduleDetailModel.setTimeEnd("12:00");
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
                                        scheduleDetailModel.setTeacherName(scheduleDetail.getTeacherByScheduleDetail().getProfileByProfileId().getLastName());
                                        listTemp.add(scheduleDetailModel);
                                    }
                                    ScheduleGroupDTO scheduleGroupDTO = new ScheduleGroupDTO();
                                    scheduleGroupDTO.setDate(LocalDate.parse(key));
                                    scheduleGroupDTO.setList(listTemp);
                                    groupDTOList.add(scheduleGroupDTO);
                                }

                                groupDTOList = groupDTOList.stream().sorted((a, b) -> a.getDate().compareTo(b.getDate())).collect(Collectors.toList());
                                listScheduleAdapter.setList(groupDTOList);
                                timetableBinding.rcvListSchedule.setAdapter(listScheduleAdapter);
                                timetableBinding.rcvListSchedule.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                                DividerItemDecoration itemDecorator = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
//                                timetableBinding.rcvListSchedule.addItemDecoration(itemDecorator);
                                subjectAPI.findSubjectByMajorIdSemester(_token,classses.getMajorId(),i+1).enqueue(new Callback<ResponseModel>() {
                                    @Override
                                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                        if(response.isSuccessful()){
                                            listSubject = new ArrayList<>();
                                            String json = gson.toJson(response.body().getData());
                                            Type listType = new TypeToken<ArrayList<Subject>>(){}.getType();
                                            list = gson.fromJson(json,listType);
                                            listSubject.add("Tất cả");
                                            for (Subject subject:list){
                                                listSubject.add(subject.getSubjectName());
                                            }
                                            timetableBinding.cbxSubject.setItem(listSubject);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                                        materialDialog = new MaterialDialog.Builder(TimetableActivity.this)
                                                .setMessage(t.getMessage())
                                                .setCancelable(false)
                                                .setPositiveButton("", R.drawable.done, new MaterialDialog.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int which) {
                                                        materialDialog.dismiss();
                                                    }
                                                }).build();
                                        materialDialog.show();
                                    }
                                });
                            }else{
                                materialDialog = new MaterialDialog.Builder(TimetableActivity.this)
                                        .setMessage("Chưa có thời khóa biểu kì " + (i + 1))
                                        .setCancelable(false)
                                        .setPositiveButton("", R.drawable.done, new MaterialDialog.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int which) {
                                                materialDialog.dismiss();
                                            }
                                        }).build();
                                materialDialog.show();
                            }

                        }else if(response.code() == 403){
                            materialDialog = new MaterialDialog.Builder(TimetableActivity.this)
                                    .setMessage("Hết phiên đăng nhập ! Vui lòng đăng nhập lại")
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
                        else {
                            materialDialog = new MaterialDialog.Builder(TimetableActivity.this)
                                    .setMessage("Chưa có thời khóa biểu kì " + (i + 1))
                                    .setCancelable(false)
                                    .setPositiveButton("", R.drawable.done, new MaterialDialog.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int which) {
                                            materialDialog.dismiss();
                                        }
                                    }).build();
                            materialDialog.show();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        materialDialog = new MaterialDialog.Builder(TimetableActivity.this)
                                .setMessage(t.getMessage())
                                .setCancelable(false)
                                .setPositiveButton("", R.drawable.done, new MaterialDialog.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {
                                        materialDialog.dismiss();
                                    }
                                }).build();
                        materialDialog.show();
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setListSpinSemester() {
        listSemester = new ArrayList<>();
        listSemester.add("1");
        listSemester.add("2");
        listSemester.add("3");
        listSemester.add("4");
        timetableBinding.cbxSemester.setItem(listSemester);
    }

    private void setListSpinWeek() {
        week = new ArrayList<>();
        week.add("Tuần này");
        week.add("Tuần sau");
        week.add("Tháng này");
        week.add("Tháng sau");
        timetableBinding.cbxWeek.setItem(week);
    }

    private void processBtnBack() {
        timetableBinding.icBack.setOnClickListener(view -> {
            finish();
        });
    }

    private void getClassId() {
        listClass = new ArrayList<>();
        studentClassAPI.getClassIdByStudentId(_token, student.getId()).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                String json = gson.toJson(response.body().getData());
                Type studentClassType = new TypeToken<ArrayList<StudentClass>>() {
                }.getType();
                List<StudentClass> studentClasses = gson.fromJson(json, studentClassType);
                for (StudentClass studentClass : studentClasses) {
                    classAPI.getClassById(_token, studentClass.getClassId()).enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            String jsonClass = gson.toJson(response.body().getData());
//                                Type classType = new TypeToken<ResponseModel>(){}.getType();
                           Classses classses = gson.fromJson(jsonClass, Classses.class);
                            listClass.add(classses.getClassCode());
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            Log.e("msg", t.getMessage());
                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

            }
        });
    }
    private void checkToken() {
        String newToken = _token.substring(7, _token.length());
        DecodedJWT jwt = JWT.decode(newToken);
        if (jwt.getExpiresAt().before(new Date())) {
            materialDialog = new MaterialDialog.Builder(TimetableActivity.this)
                    .setMessage("Hết phiên đăng nhập ! Vui lòng đăng nhập lại")
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
    protected void onResume() {
        super.onResume();
        checkToken();
    }


    @SuppressLint("NewApi")
    @Override
    public void SelectDate(int month, int date) {
        List<ScheduleGroupDTO> listFilterDate = new ArrayList<>();
        for(ScheduleGroupDTO scheduleGroupDTO:groupDTOList){
            int monthOfSchedule = scheduleGroupDTO.getDate().getMonthValue();
            int dateOfSchedule = scheduleGroupDTO.getDate().getDayOfMonth();
            if(monthOfSchedule - 1 == month && dateOfSchedule == date){
                listFilterDate.add(scheduleGroupDTO);
                listScheduleAdapter.setList(listFilterDate);
                timetableBinding.rcvListSchedule.setAdapter(listScheduleAdapter);
                timetableBinding.rcvListSchedule.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }
        }
        if(listFilterDate.isEmpty()){
            materialDialog = new MaterialDialog.Builder(TimetableActivity.this)
                    .setMessage("Không có dữ liệu")
                    .setCancelable(false)
                    .setPositiveButton("", R.drawable.done, new MaterialDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            materialDialog.dismiss();
                        }
                    }).build();
            materialDialog.show();
        }
    }

}