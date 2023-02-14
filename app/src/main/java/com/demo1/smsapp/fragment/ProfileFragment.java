package com.demo1.smsapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.demo1.smsapp.R;
import com.demo1.smsapp.activity.ChangePasswordActivity;
import com.demo1.smsapp.activity.HomeActivity;
import com.demo1.smsapp.databinding.FragmentProfileBinding;
import com.demo1.smsapp.models.*;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        profileBinding = FragmentProfileBinding.inflate(inflater);
        firebaseStorage = FirebaseStorage.getInstance();
        homeActivity = (HomeActivity) getActivity();
        gson = new Gson();
        context = getContext();
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
            List<StudentClass> studentClasses = student.getStudentClassById();
            if(majorStudentList.isEmpty()){
                profileBinding.tvMajor.setText("Chưa có");
            }else{
                StringBuilder major = new StringBuilder();
                for (MajorStudent majorStudent : majorStudentList){
                    if(majorStudentList.get(majorStudentList.size() -1) !=null){
                        major.append(majorStudent.getMajorByMajorId().getMajorName());
                    }else{
                        major.append(majorStudent.getMajorByMajorId().getMajorName()).append(", ");
                    }
                }
                profileBinding.tvMajor.setText(major);
            }
            if(studentClasses.isEmpty()){
                profileBinding.tvClass.setText("Chưa có");
            }else{
                StringBuilder classes = new StringBuilder();
                for(StudentClass studentClass : studentClasses){
                    if(studentClasses.get(studentClasses.size()-1)!=null){
                        classes.append(studentClass.getClassStudentByClass().getClassCode());
                    }else{
                        classes.append(studentClass.getClassStudentByClass().getClassCode()).append(", ");
                    }
                }
                profileBinding.tvClass.setText(classes);
            }
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

        StringBuilder address = new StringBuilder();
        address.append(profile.getAddress()).append(", ")
                .append(profile.getWardByWardId().getName()).append(", ")
                .append(profile.getDistrictByDistrictId().getName()).append(", ")
                .append(profile.getProfileProvince().getName());
        profileBinding.tvAddress.setText(address);




    }


}