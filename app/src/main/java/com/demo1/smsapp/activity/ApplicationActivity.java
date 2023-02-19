package com.demo1.smsapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.demo1.smsapp.api.utils.APIUtils;
import com.demo1.smsapp.api.utils.FileHelper;
import com.demo1.smsapp.api.utils.FormatHelper;
import com.demo1.smsapp.databinding.ActivityApplicationBinding;
import com.demo1.smsapp.dto.ResponseModel;
import com.demo1.smsapp.models.Application;
import com.demo1.smsapp.models.ApplicationType;
import com.demo1.smsapp.models.Student;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ApplicationActivity extends AppCompatActivity {
    public ActivityApplicationBinding binding;
    public JsonElement json;
    public List<ApplicationType> listAppType;
    public static final int PICKFILE_RESULT_CODE = 1;
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
        APIUtils.ApplicationType().getAll().enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Gson gson = new Gson();
                json = gson.toJsonTree(response.body().getData());
                listAppType = Arrays.asList(gson.fromJson(json, ApplicationType[].class));
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
                        Toast.makeText(ApplicationActivity.this, "Tải về thành công", Toast.LENGTH_SHORT).show();
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
                status = "pending";
                isValid = CheckValidate();
                if(isValid){
                    app.setStatus(status);
                    app.setSendDate(sendDate);
                    app.setNote(note);
                    app.setStudentId(student.getId());
                    app.setFile(applicationFile);
                    app.setApplicationTypeId(applicationTypeId);
                    String applicationJson = gson.toJson(app);
                    APIUtils.Application().post_app(token, applicationJson).enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            if (response.code() == 403) {
                                Toast.makeText(ApplicationActivity.this, "Hết phiên đăng nhập", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ApplicationActivity.this, "Gửi đơn thành công", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            Toast.makeText(ApplicationActivity.this, "Gửi đơn thất bại", Toast.LENGTH_SHORT).show();
                            Log.d("error", t.getMessage());
                        }
                    });
                }
            }
        });
    }

    //Chọn hình ảnh
    public void OnFileChoose() {
        binding.fileChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(Intent.createChooser(intent, "Chọn file"), PICKFILE_RESULT_CODE);
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
                    binding.cbxAppType.setErrorText(null);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            binding.cbxAppType.setErrorText("Vui lòng chọn mẫu đơn");
            return false;
        }
        if (binding.note.getEditText().getText().length() == 0) {
            binding.note.setError("Vui lòng nhập ghi chú");
            binding.note.getEditText().addTextChangedListener(new TextWatcher() {
               @Override
               public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

               }

               @Override
               public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

               }

               @Override
               public void afterTextChanged(Editable editable) {
                  if(binding.note.getEditText().getText().length() == 0){
                      binding.note.setError("Vui lòng nhập ghi chú");
                  }else{
                      binding.note.setError(null);
                  }
               }
           });
        }
        if (binding.fileName.length() == 0) {
            binding.errorFile.setError("Vui lòng chọn tệp đính kèm");
            return false;
        }
        return true;
    }
}
