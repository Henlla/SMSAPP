package com.demo1.smsapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.demo1.smsapp.R;
import com.demo1.smsapp.api.utils.APIUtils;
import com.demo1.smsapp.api.utils.FileHelper;
import com.demo1.smsapp.api.utils.FormatHelper;
import com.demo1.smsapp.databinding.ActivityApplicationBinding;
import com.demo1.smsapp.dto.ResponseModel;
import com.demo1.smsapp.models.Application;
import com.demo1.smsapp.models.ApplicationType;
import com.demo1.smsapp.models.Student;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ApplicationActivity extends AppCompatActivity {
    public ActivityApplicationBinding binding;
    public String json;
    public List<ApplicationType> listAppType;
    public static final int PICKFILE_RESULT_CODE = 1;
    MaterialDialog materialDialog;
    public Uri fileUri;
    public String note, applicationFile, extension, dataJson, sendDate, status, token, fileName;
    public Integer applicationTypeId;
    public Application app;
    public ApplicationType appType;
    public Student student;
    public SharedPreferences data;
    public boolean isValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityApplicationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.red));
        data = getSharedPreferences("informationAccount", MODE_PRIVATE);
        init();
    }

    public void init() {
        OnBindingDropdown();
        OnSelectedAppType();
        OnFileChoose();
        OnBack();
        OnCreateAppType();
    }

    public void OnBindingDropdown() {
        APIUtils.getApplicationType().getAll().enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Gson gson = new Gson();
                json = gson.toJson(response.body().getData());
                Type type = new TypeToken<ArrayList<ApplicationType>>(){}.getType();
                listAppType = gson.fromJson(json,type);
                binding.cbxAppType.setItem(listAppType);
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(ApplicationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void OnSelectedAppType() {
        binding.cbxAppType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                appType = (ApplicationType) adapterView.getItemAtPosition(i);
                binding.btnDownload.setVisibility(View.VISIBLE);
                applicationTypeId = appType.getId();
                binding.btnDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FileHelper.saveDocx(appType.getFile(), appType.getName());
                        Toast.makeText(ApplicationActivity.this, "Download success", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(ApplicationActivity.this, "Not selected anything", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == -1) {
                    fileUri = data.getData();
                    fileName = FileHelper.getName(this, fileUri);
                    extension = fileName.substring(fileName.indexOf(".") + 1);
                    binding.fileName.setText(fileName);
                    binding.fileName.setVisibility(View.VISIBLE);
                    applicationFile = FileHelper.fileUriToBase64(fileUri, this.getContentResolver());
                    binding.errorFile.setError(null);
                }
                break;
        }
    }

    public void OnCreateAppType() {
        binding.sendApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app = new Application();
                Gson gson = new Gson();
                dataJson = data.getString("data", null);
                token = data.getString("token", null);
                Student student = gson.fromJson(dataJson, Student.class);
                note = binding.note.getEditText().getText().toString();
                sendDate = FormatHelper.Format("date", new Date().toString());
                status = "PENDING";
                isValid = CheckValidate();
                if (isValid) {
                    app.setStatus(status);
                    app.setSendDate(sendDate);
                    app.setNote(note);
                    app.setStudentId(student.getId());
                    app.setFile(applicationFile);
                    app.setApplicationTypeId(applicationTypeId);
                    app.setResponseDate("");
                    app.setResponseNote("");
                    String applicationJson = gson.toJson(app);
                    APIUtils.getApplicationApi().post_app(token, applicationJson).enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            if (response.code() == 403) {
                                materialDialog = new MaterialDialog.Builder(ApplicationActivity.this)
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
                            } else if (response.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Send application success", Toast.LENGTH_SHORT).show();
                            } else{
                                Toast.makeText(getApplicationContext(),"Send application fail",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            Toast.makeText(ApplicationActivity.this, "Send application fail", Toast.LENGTH_SHORT).show();
                            Log.d("error", t.getMessage());
                        }
                    });
                }
            }
        });
    }

    public void OnFileChoose() {
        binding.fileChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(Intent.createChooser(intent, "Choose file"), PICKFILE_RESULT_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ApplicationActivity.this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void OnBack() {
        binding.icBack.setOnClickListener(view -> {
            finish();
        });
    }

    public boolean CheckValidate() {
        if (binding.cbxAppType.getSelectedItemPosition() == -1) {
            binding.cbxAppType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    binding.btnDownload.setVisibility(View.VISIBLE);
                    binding.cbxAppType.setErrorText(null);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            binding.cbxAppType.setErrorText("Please choose application template");
            return false;
        }
        if (binding.note.getEditText().getText().length() == 0) {
            binding.note.setError("Please enter note");
            binding.note.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (binding.note.getEditText().getText().length() == 0) {
                        binding.note.setError("Please enter note");
                    } else {
                        binding.note.setError(null);
                    }
                }
            });
        }
        if (binding.fileName.length() == 0) {
            binding.errorFile.setError("Please choose file");
            return false;
        }
        return true;
    }
}
