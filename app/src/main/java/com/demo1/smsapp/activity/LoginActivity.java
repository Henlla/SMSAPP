package com.demo1.smsapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.demo1.smsapp.R;
import com.demo1.smsapp.databinding.ActivityLoginBinding;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding loginBinding;
    boolean isShow = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());
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

            }
        });
    }
}