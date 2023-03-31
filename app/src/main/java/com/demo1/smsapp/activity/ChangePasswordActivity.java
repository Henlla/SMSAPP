package com.demo1.smsapp.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import com.demo1.smsapp.R;
import com.demo1.smsapp.api.AccountAPI;
import com.demo1.smsapp.api.utils.APIUtils;
import com.demo1.smsapp.databinding.ActivityChangePasswordBinding;
import com.demo1.smsapp.dto.ResponseModel;
import com.demo1.smsapp.models.Account;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpStatus;
import com.google.gson.Gson;
import dev.shreyaspatil.MaterialDialog.AbstractDialog;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import org.aviran.cookiebar2.CookieBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    ActivityChangePasswordBinding changePasswordBinding;
    Account account;
    String _token;
    String accountJson;
    AccountAPI accountAPI;
    Gson gson;
    boolean isShowOld = false;
    boolean isShowNew = false;
    MaterialDialog materialDialog;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changePasswordBinding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("informationAccount",MODE_PRIVATE);
        gson = new Gson();
        accountAPI = APIUtils.getAccountAPI();
        _token = sharedPreferences.getString("token",null);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading ....");
        accountJson = sharedPreferences.getString("account",null);
        account = gson.fromJson(accountJson, Account.class);
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.red));
        hideShowOldPassword();
        hideShowNewPassword();
        setFocusButton();
        processChangePassword();
        processBackBtn();
        setContentView(changePasswordBinding.getRoot());
    }

    private void processBackBtn() {
        changePasswordBinding.icBack.setOnClickListener(view->{
            finish();
        });
    }

    private void setFocusButton() {
        changePasswordBinding.tvOldPass.setOnFocusChangeListener((view, b) -> {
            if (b) {
                changePasswordBinding.layoutMsgLogin.setVisibility(View.GONE);
            }
        });
        changePasswordBinding.tvNewPass.setOnFocusChangeListener((view, b) -> {
            if (b) {
                changePasswordBinding.layoutMsgLogin.setVisibility(View.GONE);
            }
        });
    }

    private void processChangePassword() {
        changePasswordBinding.btnChangePassword.setOnClickListener(view -> {
            String oldPassword = changePasswordBinding.tvOldPass.getText().toString();
            String newPassword = changePasswordBinding.tvNewPass.getText().toString();
            if (oldPassword.isEmpty() && newPassword.isEmpty()) {
                changePasswordBinding.tvOldPass.requestFocus();
                changePasswordBinding.layoutMsgLogin.setVisibility(View.VISIBLE);
                changePasswordBinding.msgChangePassword.setText(R.string.msg_error_changePassword);
            } else if (oldPassword.isEmpty()) {
                changePasswordBinding.tvOldPass.requestFocus();
                changePasswordBinding.layoutMsgLogin.setVisibility(View.VISIBLE);
                changePasswordBinding.msgChangePassword.setText(R.string.msg_error_oldPassword);
            }
            else if (newPassword.length() < 8) {
                changePasswordBinding.tvOldPass.requestFocus();
                changePasswordBinding.layoutMsgLogin.setVisibility(View.VISIBLE);
                changePasswordBinding.msgChangePassword.setText("New password must be greater than 8 characters");
            }else if (newPassword.isEmpty()) {
                changePasswordBinding.tvNewPass.requestFocus();
                changePasswordBinding.layoutMsgLogin.setVisibility(View.VISIBLE);
                changePasswordBinding.msgChangePassword.setText(R.string.msg_error_newPassword);
            } else if (newPassword.equals(oldPassword)) {
                changePasswordBinding.tvNewPass.requestFocus();
                changePasswordBinding.layoutMsgLogin.setVisibility(View.VISIBLE);
                changePasswordBinding.msgChangePassword.setText("Old password cannot same new password !");
            } else {
                materialDialog = new MaterialDialog.Builder(ChangePasswordActivity.this)
                        .setTitle("Change password")
                        .setMessage("Do you wanna change password ?")
                        .setPositiveButton("Yes", R.drawable.done, new MaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                progressDialog.show();
                                accountAPI.changePassword(_token,account.getId(), oldPassword, newPassword).enqueue(new Callback<ResponseModel>() {
                                    @Override
                                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                        if (response.isSuccessful()) {
                                            CookieBar.build(ChangePasswordActivity.this)
                                                    .setTitleColor(R.color.red)
                                                    .setMessage("Change password success")
                                                    .setDuration(5000) // 5 seconds
                                                    .show();
                                            progressDialog.dismiss();
                                            materialDialog.dismiss();
                                            changePasswordBinding.tvOldPass.setText("");
                                            changePasswordBinding.tvNewPass.setText("");
                                        } else if (response.code() == 403) {
                                            progressDialog.dismiss();
                                            materialDialog = new MaterialDialog.Builder(ChangePasswordActivity.this)
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
                                        } else {
                                            progressDialog.dismiss();
                                            dialogInterface.dismiss();
                                            changePasswordBinding.layoutMsgLogin.setVisibility(View.VISIBLE);
                                            changePasswordBinding.msgChangePassword.setText("Old password isn't correct.Try again!");
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                                        CookieBar.build(ChangePasswordActivity.this)
                                                .setTitleColor(R.color.red)
                                                .setMessage(t.getMessage())
                                                .setDuration(5000) // 5 seconds
                                                .show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Không", R.drawable.close, new MaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                            }
                        }).build();
                materialDialog.show();
            }
        });
    }

    @SuppressLint("NewApi")
    private void hideShowNewPassword() {
        changePasswordBinding.hideShowNewPass.setOnClickListener(view -> {
            if (isShowOld) {
                isShowOld = false;
                changePasswordBinding.tvNewPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                changePasswordBinding.tvNewPass.setTypeface(getResources().getFont(R.font.inria_serif_regular));
                changePasswordBinding.hideShowNewPass.setImageResource(R.drawable.visibility_off);

            } else {
                isShowOld = true;
                changePasswordBinding.tvNewPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                changePasswordBinding.tvNewPass.setTypeface(getResources().getFont(R.font.inria_serif_regular));
                changePasswordBinding.hideShowNewPass.setImageResource(R.drawable.visibility);

            }
        });
    }

    @SuppressLint("NewApi")
    private void hideShowOldPassword() {
        changePasswordBinding.hideShowOldPass.setOnClickListener(view -> {
            if (isShowNew) {
                isShowNew = false;
                changePasswordBinding.tvOldPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                changePasswordBinding.tvOldPass.setTypeface(getResources().getFont(R.font.inria_serif_regular));
                changePasswordBinding.hideShowOldPass.setImageResource(R.drawable.visibility_off);

            } else {
                isShowNew = true;
                changePasswordBinding.tvOldPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                changePasswordBinding.tvOldPass.setTypeface(getResources().getFont(R.font.inria_serif_regular));
                changePasswordBinding.hideShowOldPass.setImageResource(R.drawable.visibility);

            }
        });
    }
}