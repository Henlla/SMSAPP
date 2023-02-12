package com.demo1.smsapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import com.bumptech.glide.Glide;
import com.demo1.smsapp.R;
import com.demo1.smsapp.activity.HomeActivity;
import com.demo1.smsapp.activity.NewDetailsActivity;
import com.demo1.smsapp.adapter.NewAdapter;
import com.demo1.smsapp.api.NewsAPI;
import com.demo1.smsapp.api.utils.APIUtils;
import com.demo1.smsapp.databinding.FragmentHomeBinding;
import com.demo1.smsapp.dto.ResponseModel;
import com.demo1.smsapp.models.*;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {
    FragmentHomeBinding fragmentHomeBinding;
    Account account;
    Student student;
    Teacher teacher;
    NewAdapter adapter;

    NewsAPI newsAPI;
    Gson gson;
    Context context;
    String jsonAccount;
    HomeActivity homeActivity;
    String data;
    String profileJson;
    private FirebaseStorage firebaseStorage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater);
        firebaseStorage = FirebaseStorage.getInstance();
        newsAPI = APIUtils.getNews();
        adapter = new NewAdapter();
        gson = new Gson();
        context = inflater.getContext();
        homeActivity = (HomeActivity)getActivity();
        jsonAccount = homeActivity.getAccountJson();
        profileJson =homeActivity.getProfileJson();
        data = homeActivity.getDataJson();
        setData();
        setListNews();
        setOnClickNews();
        return fragmentHomeBinding.getRoot();
    }

    private void setData() {
        account = gson.fromJson(jsonAccount,Account.class);
        if(account.getRoleByRoleId().getRoleName().equals("STUDENT")){
            student = gson.fromJson(data,Student.class);
            fragmentHomeBinding.card.setText(student.getStudentCard());
        }else{
            fragmentHomeBinding.card.setVisibility(View.GONE);
            teacher = gson.fromJson(data,Teacher.class);
        }
        Profile profile = gson.fromJson(profileJson,Profile.class);
        StorageReference reference = firebaseStorage.getReference().child(profile.getAvatarPath());
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .centerCrop()
                        .error(R.drawable.image_notavailable)
                        .into(fragmentHomeBinding.profileImage);
            }
        });
        String fullName = profile.getFirstName()+" "+profile.getLastName();
        fragmentHomeBinding.username.setText(fullName);
    }

    private void setOnClickNews() {
        adapter.ItemOnClick(new NewAdapter.OnClickItem() {
            @Override
            public void ItemOnclick(News news) {
                Intent intent = new Intent(context, NewDetailsActivity.class);
                String json = gson.toJson(news);
                intent.putExtra("newJson",json);
                context.startActivity(intent);
            }
        });
    }

    public void setListNews(){
        newsAPI.findAll().enqueue(new Callback<ResponseModel>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                String json = gson.toJson(response.body().getData());
                Type listType = new TypeToken<ArrayList<News>>(){}.getType();
                List<News> listNews = gson.fromJson(json, listType);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2){
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                };
                fragmentHomeBinding.rcvNew.setLayoutManager(gridLayoutManager);
                adapter.SetData(listNews.stream().filter(News::isActive).collect(Collectors.toList()));
                fragmentHomeBinding.rcvNew.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e("msg",t.getMessage());
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}