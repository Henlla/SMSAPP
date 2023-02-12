package com.demo1.smsapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.demo1.smsapp.R;
import com.demo1.smsapp.api.AccountAPI;
import com.demo1.smsapp.api.ProfileAPI;
import com.demo1.smsapp.api.StudentAPI;
import com.demo1.smsapp.api.TeacherAPI;
import com.demo1.smsapp.api.utils.APIUtils;
import com.demo1.smsapp.databinding.ActivityLoginBinding;
import com.demo1.smsapp.dto.LoginResponse;
import com.demo1.smsapp.models.Account;
import com.demo1.smsapp.models.Profile;
import com.demo1.smsapp.models.Student;
import com.demo1.smsapp.models.Teacher;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding loginBinding;
    boolean isShow = false;
    AccountAPI mAccountAPI;
    StudentAPI studentAPI;
    ProfileAPI profileAPI;
    TeacherAPI teacherAPI;

    Integer profileId;

    String jsonProfile;
    String data;
    Profile profile;
    Student student;
    Teacher teacher;
    String _token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());
        mAccountAPI = APIUtils.getAccountAPI();
        studentAPI =APIUtils.getStudent();
        profileAPI = APIUtils.getProfile();
        teacherAPI = APIUtils.getTeacher();
        hideShowPassword();
        setFocusButton();
        login();
    }
    @SuppressLint("NewApi")
    private void hideShowPassword() {
        loginBinding.hideShowPass.setOnClickListener(view ->{
            if(isShow){
                isShow = false;
                loginBinding.lgPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                loginBinding.lgPassword.setTypeface(getResources().getFont(R.font.inria_serif_regular));
                loginBinding.hideShowPass.setImageResource(R.drawable.visibility_off);

            }else{
                isShow = true;
                loginBinding.lgPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                loginBinding.lgPassword.setTypeface(getResources().getFont(R.font.inria_serif_regular));
                loginBinding.hideShowPass.setImageResource(R.drawable.visibility);

            }
        });
    }

    private void setFocusButton() {
        loginBinding.lgUser.setOnFocusChangeListener((view, b) -> {
            if(b){
                loginBinding.layoutMsgLogin.setVisibility(View.GONE);
            }
        });
        loginBinding.lgPassword.setOnFocusChangeListener((view, b) -> {
            if(b){
                loginBinding.layoutMsgLogin.setVisibility(View.GONE);
            }
        });
    }

    private void login() {
        loginBinding.btnLogin.setOnClickListener(view ->{
            String username = loginBinding.lgUser.getText().toString().trim();
            String password = loginBinding.lgPassword.getText().toString().trim();
            if(username.isEmpty() && password.isEmpty()){
                loginBinding.layoutMsgLogin.setVisibility(View.VISIBLE);
                loginBinding.msgLogin.setText(R.string.msg_error_login);
            }else if(username.isEmpty()){
                loginBinding.layoutMsgLogin.setVisibility(View.VISIBLE);
                loginBinding.msgLogin.setText(R.string.msg_error_username);
            } else if (password.isEmpty()) {
                loginBinding.layoutMsgLogin.setVisibility(View.VISIBLE);
                loginBinding.msgLogin.setText(R.string.msg_error_password);
            }else{
                mAccountAPI.login(username,password).enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        Log.d("responseBody","response to API "+response.body().toString());
                        Log.d("responseCode","response to API "+response.code());
                        Gson gson = new Gson();
                        String jsonAccount = gson.toJson(response.body().getData());
                        _token ="Bearer "+response.body().getToken();
                        Account accountResponse = gson.fromJson(jsonAccount,Account.class);
                        Integer accountId = accountResponse.getId();
                        profileAPI.getProfileByAccountId(_token,accountId).enqueue(new Callback<Profile>() {
                            @Override
                            public void onResponse(Call<Profile> call, Response<Profile> response) {
                                profile = response.body();
                                profileId= profile.getId();
                                jsonProfile = gson.toJson(profile);
                                if(accountResponse.getRoleByRoleId().getRoleName().equals("STUDENT")){
                                    studentAPI.getStudentByProfileId(_token,profileId).enqueue(new Callback<Student>() {
                                        @Override
                                        public void onResponse(Call<Student> call, Response<Student> response) {
                                            student = response.body();
                                            data = gson.toJson(student);
                                            String accountJson = gson.toJson(accountResponse);
                                            SharedPreferences sharedPreferences = getSharedPreferences("informationAccount",MODE_PRIVATE);
                                            sharedPreferences.edit()
                                                    .putString("account",accountJson)
                                                    .putString("token",_token)
                                                    .putString("profile",jsonProfile)
                                                    .putString("data",data)
                                                    .apply();
                                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                        }
                                        @Override
                                        public void onFailure(Call<Student> call, Throwable t) {
                                            Log.e("msg",t.getMessage());
                                        }
                                    });
                                }else{
                                    teacherAPI.getTeacherByProfileId(_token,profileId).enqueue(new Callback<Teacher>() {
                                        @Override
                                        public void onResponse(Call<Teacher> call, Response<Teacher> response) {
                                            teacher = response.body();
                                            data = gson.toJson(teacher);
                                            String accountJson = gson.toJson(accountResponse);
                                            SharedPreferences sharedPreferences = getSharedPreferences("informationAccount",MODE_PRIVATE);
                                            sharedPreferences.edit()
                                                    .putString("account",accountJson)
                                                    .putString("token",_token)
                                                    .putString("profile",jsonProfile)
                                                    .putString("data",data)
                                                    .apply();
                                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                        }
                                        @Override
                                        public void onFailure(Call<Teacher> call, Throwable t) {
                                            Log.e("msg",t.getMessage());
                                        }
                                    });
                                }
                            }
                            @Override
                            public void onFailure(Call<Profile> call, Throwable t) {
                                Log.e("msg",t.getMessage());
                            }
                        });


                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("msg",t.getMessage());
                    }
                });
            }
        });
    }
}