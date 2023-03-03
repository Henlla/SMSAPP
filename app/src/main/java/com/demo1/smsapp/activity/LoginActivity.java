package com.demo1.smsapp.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import com.demo1.smsapp.R;
import com.demo1.smsapp.api.*;
import com.demo1.smsapp.api.utils.APIUtils;
import com.demo1.smsapp.databinding.ActivityLoginBinding;
import com.demo1.smsapp.dto.LoginResponse;
import com.demo1.smsapp.enums.ERole;
import com.demo1.smsapp.models.*;
import com.demo1.smsapp.utils.MyFirebaseMessagingService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

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
    MaterialDialog mDialog;
    ProgressDialog pd;
    MyFirebaseMessagingService service;

    DeviceAPI deviceAPI;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());
        mAccountAPI = APIUtils.getAccountAPI();
        studentAPI = APIUtils.getStudent();
        profileAPI = APIUtils.getProfile();
        teacherAPI = APIUtils.getTeacher();
        pd = new ProgressDialog(this);
        pd.setMessage("Loading ....");
        deviceAPI = APIUtils.getDeviceAPI();
        service = new MyFirebaseMessagingService();
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(LoginActivity.this, R.color.red));
        hideShowPassword();
        setFocusButton();
        login();
        setTokenDevice();
    }

    private void setTokenDevice() {

    }


    @SuppressLint("NewApi")
    private void hideShowPassword() {
        loginBinding.hideShowPass.setOnClickListener(view -> {
            if (isShow) {
                isShow = false;
                loginBinding.lgPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                loginBinding.lgPassword.setTypeface(getResources().getFont(R.font.inria_serif_regular));
                loginBinding.hideShowPass.setImageResource(R.drawable.visibility_off);

            } else {
                isShow = true;
                loginBinding.lgPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                loginBinding.lgPassword.setTypeface(getResources().getFont(R.font.inria_serif_regular));
                loginBinding.hideShowPass.setImageResource(R.drawable.visibility);

            }
        });
    }

    private void setFocusButton() {
        loginBinding.lgUser.setOnFocusChangeListener((view, b) -> {
            if (b) {
                loginBinding.layoutMsgLogin.setVisibility(View.GONE);
            }
        });
        loginBinding.lgPassword.setOnFocusChangeListener((view, b) -> {
            if (b) {
                loginBinding.layoutMsgLogin.setVisibility(View.GONE);
            }
        });
    }

    private void login() {
        loginBinding.btnLogin.setOnClickListener(view -> {
            String username = loginBinding.lgUser.getText().toString().trim();
            String password = loginBinding.lgPassword.getText().toString().trim();
            if (username.isEmpty() && password.isEmpty()) {
                loginBinding.layoutMsgLogin.setVisibility(View.VISIBLE);
                loginBinding.msgLogin.setText(R.string.msg_error_login);
            } else if (username.isEmpty()) {
                loginBinding.layoutMsgLogin.setVisibility(View.VISIBLE);
                loginBinding.msgLogin.setText(R.string.msg_error_username);
            } else if (password.isEmpty()) {
                loginBinding.layoutMsgLogin.setVisibility(View.VISIBLE);
                loginBinding.msgLogin.setText(R.string.msg_error_password);
            } else {
                pd.show();
                mAccountAPI.login(username, password).enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            Gson gson = new Gson();
                            String jsonAccount = gson.toJson(response.body().getData());
                            _token = "Bearer " + response.body().getToken();
                            Account accountResponse = gson.fromJson(jsonAccount, Account.class);
                            Integer accountId = accountResponse.getId();

                            deviceAPI.findByAccountId(accountId).enqueue(new Callback<Device>() {
                                @Override
                                public void onResponse(Call<Device> call, Response<Device> response) {
                                    if (response.isSuccessful()) {
                                        FirebaseMessaging.getInstance().getToken()
                                                .addOnCompleteListener(new OnCompleteListener<String>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<String> task) {
                                                        if (!task.isSuccessful()) {
                                                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                                            return;
                                                        }
                                                        Device device = response.body();
                                                        // Get new FCM registration token
                                                        String tokenDevice = task.getResult();
                                                        device.setDeviceToken(tokenDevice);
                                                        String deviceJson = gson.toJson(device);
                                                        deviceAPI.putDevice(deviceJson).enqueue(new Callback<Device>() {
                                                            @Override
                                                            public void onResponse(Call<Device> call, Response<Device> response) {

                                                            }

                                                            @Override
                                                            public void onFailure(Call<Device> call, Throwable t) {

                                                            }
                                                        });
                                                        // Log and toast
                                                        Log.d("token", tokenDevice);
//                        Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {
                                        FirebaseMessaging.getInstance().getToken()
                                                .addOnCompleteListener(new OnCompleteListener<String>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<String> task) {
                                                        if (!task.isSuccessful()) {
                                                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                                            return;
                                                        }
                                                        Device device = new Device();
                                                        // Get new FCM registration token
                                                        String tokenDevice = task.getResult();
                                                        device.setDeviceToken(tokenDevice);
                                                        device.setAccountId(accountId);
                                                        String deviceJson = gson.toJson(device);
                                                        deviceAPI.saveDevice(deviceJson).enqueue(new Callback<Device>() {
                                                            @Override
                                                            public void onResponse(Call<Device> call, Response<Device> response) {

                                                            }

                                                            @Override
                                                            public void onFailure(Call<Device> call, Throwable t) {

                                                            }
                                                        });
                                                        // Log and toast
                                                        Log.d("token", tokenDevice);
//                        Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT).show();
                                                    }

                                                });
                                    }
                                }

                                @Override
                                public void onFailure(Call<Device> call, Throwable t) {

                                }
                            });
                            profileAPI.getProfileByAccountId(_token, accountId).enqueue(new Callback<Profile>() {
                                @Override
                                public void onResponse(Call<Profile> call, Response<Profile> response) {
                                    profile = response.body();
                                    profileId = profile.getId();
                                    jsonProfile = gson.toJson(profile);
                                    if (accountResponse.getRoleByRoleId().getRoleName().equals(ERole.Student.toString())) {
                                        studentAPI.getStudentByProfileId(_token, profileId).enqueue(new Callback<Student>() {
                                            @Override
                                            public void onResponse(Call<Student> call, Response<Student> response) {
                                                student = response.body();
                                                data = gson.toJson(student);
                                                String accountJson = gson.toJson(accountResponse);
                                                SharedPreferences sharedPreferences = getSharedPreferences("informationAccount", MODE_PRIVATE);
                                                sharedPreferences.edit()
                                                        .putString("account", accountJson)
                                                        .putString("token", _token)
                                                        .putString("profile", jsonProfile)
                                                        .putString("role", accountResponse.getRoleByRoleId().getRoleName())
                                                        .putString("data", data)
                                                        .apply();
                                                pd.dismiss();
                                                setTokenDevice();
                                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                            }

                                            @Override
                                            public void onFailure(Call<Student> call, Throwable t) {
                                                Log.e("msg", t.getMessage());
                                            }
                                        });
                                    } else {
                                        teacherAPI.getTeacherByProfileId(_token, profileId).enqueue(new Callback<Teacher>() {
                                            @Override
                                            public void onResponse(Call<Teacher> call, Response<Teacher> response) {
                                                teacher = response.body();
                                                data = gson.toJson(teacher);
                                                String accountJson = gson.toJson(accountResponse);
                                                SharedPreferences sharedPreferences = getSharedPreferences("informationAccount", MODE_PRIVATE);
                                                sharedPreferences.edit()
                                                        .putString("account", accountJson)
                                                        .putString("token", _token)
                                                        .putString("profile", jsonProfile)
                                                        .putString("role", accountResponse.getRoleByRoleId().getRoleName())
                                                        .putString("data", data)
                                                        .apply();
                                                setTokenDevice();
                                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                            }

                                            @Override
                                            public void onFailure(Call<Teacher> call, Throwable t) {
                                                Log.e("msg", t.getMessage());
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onFailure(Call<Profile> call, Throwable t) {
                                    Log.e("msg", t.getMessage());
                                }
                            });
                        } else {
                            pd.dismiss();
                            mDialog = new MaterialDialog.Builder(LoginActivity.this)
                                    .setMessage("Sai tài khoản hoặc mật khẩu !")
                                    .setCancelable(false)
                                    .setPositiveButton("", R.drawable.done, new MaterialDialog.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int which) {
                                            mDialog.dismiss();
                                        }
                                    })
                                    .build();
                            mDialog.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        pd.dismiss();
                        Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("msg", t.getMessage());
                    }
                });
            }
        });
    }
}