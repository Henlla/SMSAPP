package com.demo1.smsapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.demo1.smsapp.R;
import com.demo1.smsapp.activity.ChangePasswordActivity;
import com.demo1.smsapp.activity.HomeActivity;
import com.demo1.smsapp.api.ClassAPI;
import com.demo1.smsapp.api.StudentClassAPI;
import com.demo1.smsapp.api.utils.APIUtils;
import com.demo1.smsapp.databinding.FragmentProfileBinding;
import com.demo1.smsapp.dto.ResponseModel;
import com.demo1.smsapp.models.*;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding profileBinding;
    Account account;
    Student student;
    Teacher teacher;
    Profile profile;
    HomeActivity homeActivity;
    String data;
    String profileJson;
    String jsonAccount;
    Context context;
    Gson gson;
    private FirebaseStorage firebaseStorage;

    ClassAPI classAPI;
    StudentClassAPI studentClassAPI;
    String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        profileBinding = FragmentProfileBinding.inflate(inflater);
        firebaseStorage = FirebaseStorage.getInstance();
        homeActivity = (HomeActivity) getActivity();
        gson = new Gson();
        context = getContext();
        classAPI = APIUtils.getClasses();
        token = homeActivity.get_token();
        studentClassAPI = APIUtils.getStudentClass();
        jsonAccount = homeActivity.getAccountJson();
        profileJson = homeActivity.getProfileJson();
        data = homeActivity.getDataJson();
        setDataOnUI();
        clickBtnChangePassword();
        return profileBinding.getRoot();
    }

    private void clickBtnChangePassword() {
        profileBinding.btnChangePassword.setOnClickListener(view ->{
            startActivity(new Intent(context, ChangePasswordActivity.class));
        });
    }

    private void setDataOnUI() {
        account = gson.fromJson(jsonAccount, Account.class);
        if (account.getRoleByRoleId().getRoleName().equals("STUDENT")) {
            profileBinding.lMajor.setVisibility(View.VISIBLE);
            profileBinding.lClass.setVisibility(View.VISIBLE);
            profileBinding.stCard.setVisibility(View.VISIBLE);
            student = gson.fromJson(data, Student.class);
            profileBinding.stCard.setText(student.getStudentCard());
            List<MajorStudent> majorStudentList = student.getMajorStudentsById();
            if(majorStudentList.isEmpty()){
                profileBinding.tvMajor.setText("Chưa có");
            }else{
                StringBuilder major = new StringBuilder();
                for (MajorStudent majorStudent : majorStudentList){
                    if(majorStudentList.get(majorStudentList.size() -1) !=null){
                        major.append(majorStudent.getMajorByMajorId().getMajorCode());
                        profileBinding.tvMajor2.setText(majorStudent.getMajorByMajorId().getApartmentByApartmentId().getApartmentCode());
                    }else{
                        major.append(majorStudent.getMajorByMajorId().getMajorName()).append(", ");
                    }
                }
                profileBinding.tvMajor.setText(major);
            }
           studentClassAPI.getClassIdByStudentId(token, student.getId()).enqueue(new Callback<ResponseModel>() {
               @Override
               public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    String json = gson.toJson(response.body().getData());
                   Type studentClassType = new TypeToken<ArrayList<StudentClass>>(){}.getType();
                    List<StudentClass> studentClasses = gson.fromJson(json, studentClassType);
                    for (StudentClass studentClass : studentClasses){
                        classAPI.getClassById(token,studentClass.getClassId()).enqueue(new Callback<ResponseModel>() {
                            @Override
                            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                String jsonClass = gson.toJson(response.body().getData());
//                                Type classType = new TypeToken<ResponseModel>(){}.getType();
                               Classses classses = gson.fromJson(jsonClass, Classses.class);
                                if(classses==null){
                                    profileBinding.tvClass.setText("Chưa có");
                                }else{
                                    profileBinding.tvClass.setText(classses.getClassCode());
                                }
                            }
                            @Override
                            public void onFailure(Call<ResponseModel> call, Throwable t) {
                                Log.e("msg",t.getMessage());
                            }
                        });
                    }

               }

               @Override
               public void onFailure(Call<ResponseModel> call, Throwable t) {
                   Log.e("msg",t.getMessage());
               }
           });
        } else {
            profileBinding.lMajor.setVisibility(View.GONE);
            profileBinding.lClass.setVisibility(View.GONE);
            profileBinding.stCard.setVisibility(View.GONE);
            teacher = gson.fromJson(data, Teacher.class);
        }
        profile = gson.fromJson(profileJson, Profile.class);
        StorageReference reference = firebaseStorage.getReference().child(profile.getAvatarPath());
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .centerCrop()
                        .error(R.drawable.image_notavailable)
                        .into(profileBinding.profileImage);
            }
        });
        StringBuilder fullName = new StringBuilder();
        fullName.append(profile.getFirstName()).append(" ").append(profile.getLastName());
        profileBinding.tvFullName.setText(fullName);
        profileBinding.tvEmail.setText(profile.getEmail());
        profileBinding.tvDob.setText(profile.getDob());
        profileBinding.tvGender.setText(profile.getSex());
        profileBinding.tvPhone.setText(profile.getPhone());

        StringBuilder address = new StringBuilder();
        if(profile.getAddress() != null && profile.getWardByWardId() !=null && profile.getDistrictByDistrictId()!=null && profile.getProfileProvince()!=null){
            address.append(profile.getAddress()).append(", ")
                    .append(profile.getWardByWardId().getName()).append(", ")
                    .append(profile.getDistrictByDistrictId().getName()).append(", ")
                    .append(profile.getProfileProvince().getName());
        }else{
            address.append("");
        }
        profileBinding.tvAddress.setText(address);




    }


}