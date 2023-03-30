package com.demo1.smsapp.activity.teacher;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.demo1.smsapp.R;
import com.demo1.smsapp.activity.TimetableActivity;
import com.demo1.smsapp.adapter.ListStudentTakeMarkAdapter;
import com.demo1.smsapp.api.ClassAPI;
import com.demo1.smsapp.api.MarkAPI;
import com.demo1.smsapp.api.utils.APIUtils;
import com.demo1.smsapp.databinding.ActivityTakeMarkBinding;
import com.demo1.smsapp.dto.ResponseModel;
import com.demo1.smsapp.dto.SubjectModel;
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
import java.util.List;

public class TakeMarkActivity extends AppCompatActivity {
    ActivityTakeMarkBinding takeMarkBinding;
    Gson gson;
    Teacher teacher;
    List<Classses> listClass;
    String _token;
    String teacherJson;
    ClassAPI classAPI;
    List<String> listClassName;
    MaterialDialog materialDialog;
    List<SubjectModel> listSubjectModel;
    Classses classses;
    List<StudentClass> studentClassList;
    List<Student> listStudent;
    String subjectCode;
    List<StudentTakeMarkModel> studentTakeMarkModels;
    boolean flag = false;
    boolean isHasMark = false;
    List<String> listSubjectName;
    MarkAPI markAPI;
    ListStudentTakeMarkAdapter listStudentTakeMarkAdapter;
    int times;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        takeMarkBinding = ActivityTakeMarkBinding.inflate(getLayoutInflater());
        setContentView(takeMarkBinding.getRoot());
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.red));
        gson = new Gson();
        listStudentTakeMarkAdapter=  new ListStudentTakeMarkAdapter();
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("informationAccount", MODE_PRIVATE);
        _token = sharedPreferences.getString("token", null);
        teacherJson = sharedPreferences.getString("data", null);
        teacher = gson.fromJson(teacherJson, Teacher.class);
        classAPI = APIUtils.getClasses();
        markAPI = APIUtils.getMarkAPI();
        getListClass();
        onSelectClass();
        onSelectSubject();
        onSubmitTakeMark();
        processBtnBack();
    }

    private void processBtnBack() {
        takeMarkBinding.icBack.setOnClickListener(view -> {
            finish();
        });
    }

    private void onSubmitTakeMark() {
        takeMarkBinding.submitTakeMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!flag){
                    materialDialog = new MaterialDialog.Builder(TakeMarkActivity.this)
                            .setMessage("Choose subject to take mark")
                            .setCancelable(false)
                            .setPositiveButton("", R.drawable.done, new MaterialDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    materialDialog.dismiss();
                                }
                            }).build();
                    materialDialog.show();
                }else{
                    materialDialog = new MaterialDialog.Builder(TakeMarkActivity.this)
                            .setMessage("Are you sure take mark?")
                            .setCancelable(true)
                            .setPositiveButton("", R.drawable.done, new MaterialDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    List<StudentTakeMarkModel> markModelList = listStudentTakeMarkAdapter.getList();
                                    List<Mark> markList = new ArrayList<>();
                                    boolean isCheck = markModelList.stream().anyMatch(s -> s.getAsm().equals(0.0) || s.getObj().equals(0.0));
                                    materialDialog.dismiss();
                                    if(isCheck){
                                        materialDialog = new MaterialDialog.Builder(TakeMarkActivity.this)
                                                .setMessage("Incorrect mark")
                                                .setCancelable(false)
                                                .setPositiveButton("", R.drawable.done, new MaterialDialog.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int which) {
                                                        materialDialog.dismiss();
                                                    }
                                                }).build();
                                        materialDialog.show();
                                    }else{
                                        if(!isHasMark){
                                            for (StudentTakeMarkModel studentTakeMarkModel:markModelList){
                                                Mark mark = new Mark();
                                                mark.setAsm(studentTakeMarkModel.getAsm());
                                                mark.setObj(studentTakeMarkModel.getObj());
                                                mark.setUpdateTimes(0);
                                                mark.setStudentSubjectId(studentTakeMarkModel.getStudentSubjectId());
                                                markList.add(mark);
                                            }
                                            String json = gson.toJson(markList);
                                            markAPI.saveAll(_token,json).enqueue(new Callback<ResponseModel>() {
                                                @Override
                                                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                                    if(response.isSuccessful()){
                                                        materialDialog = new MaterialDialog.Builder(TakeMarkActivity.this)
                                                                .setMessage("Take mark success")
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
                                                    Log.e("msg","save All mark "+t.getMessage());
                                                }
                                            });
                                        }else{
                                            if(times < 2){
                                                for (StudentTakeMarkModel studentTakeMarkModel:markModelList){
                                                    Mark mark = new Mark();
                                                    mark.setId(studentTakeMarkModel.getMarkId());
                                                    mark.setAsm(studentTakeMarkModel.getAsm());
                                                    mark.setObj(studentTakeMarkModel.getObj());
                                                    mark.setUpdateTimes(times+1);
                                                    mark.setStudentSubjectId(studentTakeMarkModel.getStudentSubjectId());
                                                    markList.add(mark);
                                                }
                                                String json = gson.toJson(markList);
                                                markAPI.saveAll(_token,json).enqueue(new Callback<ResponseModel>() {
                                                    @Override
                                                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                                        if(response.isSuccessful()){
                                                            materialDialog = new MaterialDialog.Builder(TakeMarkActivity.this)
                                                                    .setMessage("You only have "+(2 - (times + 1))+" times update mark")
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
                                                        Log.e("msg","save All mark "+t.getMessage());
                                                    }
                                                });
                                            }else{
                                                materialDialog = new MaterialDialog.Builder(TakeMarkActivity.this)
                                                        .setMessage("You have 0 times update mark!"+"\n"+"Please contact with admin to update mark")
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
                                }
                            })
                            .setNegativeButton("", R.drawable.close, new MaterialDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    materialDialog.dismiss();
                                }
                            }).build();
                    materialDialog.show();
                }
            }
        });
    }

    private void onSelectSubject() {
        takeMarkBinding.cbxSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subjectCode = listSubjectModel.get(i).getName();
                flag = true;
                times = 0;
                SubjectModel subjectModel = listSubjectModel.stream().filter(s -> s.getName().equals(subjectCode)).findFirst().get();
                for (StudentTakeMarkModel studentTakeMarkModel:studentTakeMarkModels){
                    StudentSubject studentSubject = studentTakeMarkModel.getStudent().getStudentSubjectsById()
                                                .stream().filter(s -> s.getSubjectId().equals(subjectModel.getId()) && s.getStudentId().equals(studentTakeMarkModel.getStudent().getId())).findFirst().get();
                    studentTakeMarkModel.setStudentSubjectId(studentSubject.getId());
                    if(!studentSubject.getMarksById().isEmpty()){
                        times = studentSubject.getMarksById().get(0).getUpdateTimes();
                        studentTakeMarkModels.stream().filter(s->s.getStudent().getId().equals(studentTakeMarkModel.getStudent().getId())).findFirst().get().setTimesUpdate(times);
                        studentTakeMarkModels.stream().filter(s->s.getStudent().getId().equals(studentTakeMarkModel.getStudent().getId())).findFirst().get().setAsm(studentSubject.getMarksById().get(0).getAsm());
                        studentTakeMarkModels.stream().filter(s->s.getStudent().getId().equals(studentTakeMarkModel.getStudent().getId())).findFirst().get().setMarkId(studentSubject.getMarksById().get(0).getId());
                        studentTakeMarkModels.stream().filter(s->s.getStudent().getId().equals(studentTakeMarkModel.getStudent().getId())).findFirst().get().setObj(studentSubject.getMarksById().get(0).getObj());
                        isHasMark = true;

                    }else{
                        studentTakeMarkModels.stream().filter(s->s.getStudent().getId().equals(studentTakeMarkModel.getStudent().getId())).findFirst().get().setAsm(0.0);
                        studentTakeMarkModels.stream().filter(s->s.getStudent().getId().equals(studentTakeMarkModel.getStudent().getId())).findFirst().get().setMarkId(0);
                        studentTakeMarkModels.stream().filter(s->s.getStudent().getId().equals(studentTakeMarkModel.getStudent().getId())).findFirst().get().setObj(0.0);
                        isHasMark = false;
                    }
                }
                if(isHasMark){
                    listStudentTakeMarkAdapter.setData(studentTakeMarkModels);
                    takeMarkBinding.rcvTakeMark.setAdapter(listStudentTakeMarkAdapter);
                    takeMarkBinding.rcvTakeMark.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                }else{
                    listStudentTakeMarkAdapter.setData(studentTakeMarkModels);
                    takeMarkBinding.rcvTakeMark.setAdapter(listStudentTakeMarkAdapter);
                    takeMarkBinding.rcvTakeMark.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void onSelectClass() {

        takeMarkBinding.cbxClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String classCode = listClassName.get(i);
                classAPI.findClassCode(_token,classCode).enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if(response.isSuccessful()){
                            listSubjectModel = new ArrayList<>();
                            studentClassList = new ArrayList<>();
                            listStudent = new ArrayList<>();
                            studentTakeMarkModels = new ArrayList<>();
                            listSubjectName = new ArrayList<>();
                            String json = gson.toJson(response.body().getData());
                            classses = gson.fromJson(json,Classses.class);

                            for (Subject subject:classses.getMajor().getSubjectsById()){
                                listSubjectModel.add(new SubjectModel(subject.getId(),subject.getSubjectCode()));
                                listSubjectName.add(subject.getSubjectCode());
                            }
                            studentClassList = classses.getStudentClassById();
                            for (StudentClass studentClass:studentClassList){
                                listStudent.add(studentClass.getClassStudentByStudent());
                            }

                            for (Student student:listStudent){
                                StudentTakeMarkModel studentTakeMarkModel = new StudentTakeMarkModel();
                                studentTakeMarkModel.setStudent(student);
                                studentTakeMarkModel.setMarkId(0);
                                studentTakeMarkModel.setAsm(0.0);
                                studentTakeMarkModel.setObj(0.0);
                                studentTakeMarkModel.setStudentSubjectId(0);
                                studentTakeMarkModel.setTimesUpdate(0);
                                studentTakeMarkModels.add(studentTakeMarkModel);
                            }
                            takeMarkBinding.cbxSubject.setItem(listSubjectName);
                            takeMarkBinding.cbxSubject.setVisibility(View.VISIBLE);
                            listStudentTakeMarkAdapter.setData(studentTakeMarkModels);
                            takeMarkBinding.rcvTakeMark.setAdapter(listStudentTakeMarkAdapter);
                            takeMarkBinding.rcvTakeMark.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        Log.e("msg",t.getMessage());
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getListClass() {
        listClass = new ArrayList<>();
        listClassName = new ArrayList<>();
        classAPI.findClassByTeacher(_token,teacher.getId()).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if(response.isSuccessful()){
                    String json = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<ArrayList<Classses>>() {
                    }.getType();
                    listClass = gson.fromJson(json,listType);
                    for (Classses classses :listClass){
                        listClassName.add(classses.getClassCode());
                    }
                    takeMarkBinding.cbxClass.setItem(listClassName);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e("msg",t.getMessage());
            }
        });
    }
}