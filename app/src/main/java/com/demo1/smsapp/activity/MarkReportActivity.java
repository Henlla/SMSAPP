package com.demo1.smsapp.activity;

import android.content.SharedPreferences;
import android.os.StrictMode;
import android.view.Window;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.demo1.smsapp.R;
import com.demo1.smsapp.adapter.ListMarkAdapter;
import com.demo1.smsapp.api.StudentAPI;
import com.demo1.smsapp.api.utils.APIUtils;
import com.demo1.smsapp.databinding.ActivityMarkReportBinding;
import com.demo1.smsapp.dto.MarkGroupModel;
import com.demo1.smsapp.dto.MarkModel;
import com.demo1.smsapp.dto.ResponseModel;
import com.demo1.smsapp.models.*;
import com.google.gson.Gson;
import retrofit2.Response;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class MarkReportActivity extends AppCompatActivity {

    ActivityMarkReportBinding markReportBinding;
    String _token;
    String studentJson;
    Student student;
    Gson gson;
    List<MarkGroupModel> markGroupModels;
    StudentAPI studentAPI;

    List<Subject> subjectList;
    List<MarkModel> markModels;
    ListMarkAdapter listMarkAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
        markReportBinding = ActivityMarkReportBinding.inflate(getLayoutInflater());
        setContentView(markReportBinding.getRoot());
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("informationAccount", MODE_PRIVATE);
        gson = new Gson();
        studentAPI = APIUtils.getStudent();
        listMarkAdapter = new ListMarkAdapter();
        studentJson = sharedPreferences.getString("data", null);
        _token = sharedPreferences.getString("token", null);
        student = gson.fromJson(studentJson, Student.class);

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(MarkReportActivity.this, R.color.red));
        getListSubject();
        getListMark();
        setData();
        OnBack();
    }

    private void OnBack() {
        markReportBinding.icBack.setOnClickListener(view -> {
            finish();
        });
    }

    private void setData() {
        markGroupModels = new ArrayList<>();
        HashMap<Integer, List<MarkModel>> hashMap = new HashMap<>();
        for (MarkModel model : markModels) {
            Integer key = model.getSemester();
            if (hashMap.containsKey(key)) {
                List<MarkModel> list = hashMap.get(key);
                list.add(model);

            } else {
                List<MarkModel> list = new ArrayList<MarkModel>();
                list.add(model);
                hashMap.put(key, list);
            }
        }


        SortedSet<Integer> keys = new TreeSet<>(hashMap.keySet());
        for (Integer key : keys) {
            MarkGroupModel model = new MarkGroupModel();
            model.setSemester(key);
            List<MarkModel> list = hashMap.get(key);
            model.setList(list);
            markGroupModels.add(model);
        }

        listMarkAdapter.setData(markGroupModels);
        markReportBinding.rcvMark.setAdapter(listMarkAdapter);
        markReportBinding.rcvMark.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getListMark() {
        try {
            Response<ResponseModel> studentResponse = studentAPI.getStudentById(_token, student.getId()).execute();
            String studentJson = gson.toJson(studentResponse.body().getData());
            Student studentNew = gson.fromJson(studentJson, Student.class);
            markModels = new ArrayList<>();
            for (Subject subject : subjectList) {
                for (StudentSubject studentSubject : studentNew.getStudentSubjectsById().stream().filter(studentSubject -> studentSubject.getSubjectId().equals(subject.getId())).collect(Collectors.toList())) {
                    if (!studentSubject.getMarksById().isEmpty()) {
                        MarkModel model = new MarkModel();
                        model.setAsm(studentSubject.getMarksById().get(0).getAsm());
                        model.setObj(studentSubject.getMarksById().get(0).getObj());
                        model.setSubject(subject);
                        model.setSemester(subject.getSemesterId());
                        markModels.add(model);
                    } else {
                        MarkModel model = new MarkModel();
                        model.setAsm(0.0);
                        model.setObj(0.0);
                        model.setSubject(subject);
                        model.setSemester(subject.getSemesterId());
                        markModels.add(model);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void getListSubject() {
        subjectList = new ArrayList<>();
        for (MajorStudent majorStudent : student.getMajorStudentsById()) {
            subjectList.addAll(majorStudent.getMajorByMajorId().getSubjectsById());
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setData();
    }
}