package com.demo1.smsapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.demo1.smsapp.R;
import com.demo1.smsapp.adapter.ListApplicationAdapter;
import com.demo1.smsapp.api.ApplicationAPI;
import com.demo1.smsapp.api.utils.APIUtils;
import com.demo1.smsapp.databinding.ActivityViewApplicationBinding;
import com.demo1.smsapp.dto.ResponseModel;
import com.demo1.smsapp.models.Application;
import com.demo1.smsapp.models.Student;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ViewApplicationActivity extends AppCompatActivity {
    ActivityViewApplicationBinding binding;
    ListApplicationAdapter applicationAdapter;
    ApplicationAPI applicationAPI;
    Gson gson;
    MaterialDialog materialDialog;
    String dataJson, token;
    public SharedPreferences data;
    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewApplicationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.red));
        init();
    }

    public void init() {
        gson = new Gson();
        data = getSharedPreferences("informationAccount", MODE_PRIVATE);
        dataJson = data.getString("data", null);
        token = data.getString("token", null);
        student = gson.fromJson(dataJson, Student.class);
        applicationAPI = APIUtils.getApplicationApi();
        gson = new Gson();
        applicationAdapter = new ListApplicationAdapter();
        OnBack();
        OnGetApplication();
    }

    public void OnGetApplication() {
        applicationAPI.getByStudentId(token, student.getId()).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()) {
                    String applicationJson = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<ArrayList<Application>>() {
                    }.getType();
                    List<Application> listApplication = gson.fromJson(applicationJson, listType);
                    if (!listApplication.isEmpty()) {
                        binding.rcvApplication.setLayoutManager(new LinearLayoutManager(ViewApplicationActivity.this));
                        applicationAdapter.setData(listApplication);
                        binding.rcvApplication.setAdapter(applicationAdapter);
                        binding.rcvApplication.setVisibility(View.VISIBLE);
                        binding.emptyView.setVisibility(View.GONE);
                        binding.emptyView.setText("");
                    } else {
                        binding.rcvApplication.setVisibility(View.GONE);
                        binding.emptyView.setVisibility(View.VISIBLE);
                        binding.emptyView.setText("Don't have any application");
                    }
                } else if (response.code() == 403) {
                    materialDialog = new MaterialDialog.Builder(ViewApplicationActivity.this)
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
                Log.e("error", t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void OnBack() {
        binding.icBack.setOnClickListener(view -> {
            finish();
        });
    }

}
