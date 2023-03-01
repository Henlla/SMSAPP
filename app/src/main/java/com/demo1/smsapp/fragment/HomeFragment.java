package com.demo1.smsapp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.demo1.smsapp.activity.*;
import com.demo1.smsapp.adapter.ListFunctionAdapter;
import com.demo1.smsapp.adapter.NewAdapter;
import com.demo1.smsapp.adapter.SlideNewAdapter;
import com.demo1.smsapp.api.NewsAPI;
import com.demo1.smsapp.api.utils.APIUtils;
import com.demo1.smsapp.databinding.FragmentHomeBinding;
import com.demo1.smsapp.dto.FunctionModel;
import com.demo1.smsapp.dto.ResponseModel;
import com.demo1.smsapp.enums.ERole;
import com.demo1.smsapp.models.*;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static android.content.Context.MODE_PRIVATE;
import static com.demo1.smsapp.enums.EFunctionName.*;

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
    MaterialDialog mDialog;
    SlideNewAdapter slideNewAdapter;

    ListFunctionAdapter listFunctionAdapter;
    String role;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater);
        firebaseStorage = FirebaseStorage.getInstance();
        newsAPI = APIUtils.getNews();
        adapter = new NewAdapter();
        gson = new Gson();
        slideNewAdapter = new SlideNewAdapter();
        listFunctionAdapter = new ListFunctionAdapter();
        context = inflater.getContext();
        homeActivity = (HomeActivity)getActivity();
        jsonAccount = homeActivity.getAccountJson();
        profileJson =homeActivity.getProfileJson();
        data = homeActivity.getDataJson();
        setUpSlideAuto();
        setData();
        setListNews();
        setOnClickNews();
        processLogout();
        processClickSearch();
        clickReadMore();
        setListFunc();
        setOnClickFunction();
        return fragmentHomeBinding.getRoot();
    }

    public void setOnClickFunction() {
        listFunctionAdapter.ClickFunction(new ListFunctionAdapter.IClickItem() {
            @Override
            public void ClickFunction(String functionName) {
                if(functionName.equals(SCHEDULES.toString())){
                    startActivity(new Intent(context, TimetableActivity.class));
                }
            }
        });
    }

    @SuppressLint("NewApi")
    public void setListFunc() {
        List<FunctionModel> listByRole = listF().stream().filter(functionModel -> functionModel.getRole().equals(role)).collect(Collectors.toList());
        listFunctionAdapter.setData(listByRole);
        fragmentHomeBinding.listFunc.setAdapter(listFunctionAdapter);
        fragmentHomeBinding.listFunc.setLayoutManager(new GridLayoutManager(getContext(),3));
    }


    public List<FunctionModel> listF(){
        List<FunctionModel> functionModels= new ArrayList<>();
        FunctionModel attendance = new FunctionModel(ERole.Teacher.toString(), "Điểm danh", ATTENDANCE.toString(),R.drawable.logout_2);
        FunctionModel mark = new FunctionModel(ERole.Teacher.toString(), "Xem điểm", MARK.toString(),R.drawable.logout_2);
        FunctionModel schedule = new FunctionModel(ERole.Student.toString(), "Thời khóa biểu", SCHEDULES.toString(),R.drawable.logout_2);
        FunctionModel application = new FunctionModel(ERole.Student.toString(), "Nợp đơn", APPLICATION.toString(),R.drawable.logout_2);
        FunctionModel schedule1 = new FunctionModel(ERole.Teacher.toString(), "Thời khóa biểu1", SCHEDULES.toString(),R.drawable.logout_2);
        FunctionModel schedule2 = new FunctionModel(ERole.Teacher.toString(), "Thời khóa biểu2", SCHEDULES.toString(),R.drawable.logout_2);
        functionModels.add(attendance);
        functionModels.add(mark);
        functionModels.add(schedule);
        functionModels.add(application);
        functionModels.add(schedule1);
        functionModels.add(schedule2);
        return functionModels;
    }


    private void setUpSlideAuto() {
        newsAPI.findAll().enqueue(new Callback<ResponseModel>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                String json = gson.toJson(response.body().getData());
                Type listType = new TypeToken<ArrayList<News>>(){}.getType();
                List<News> listNews = gson.fromJson(json, listType);
                slideNewAdapter.setData(listNews.stream().filter(news -> news.isActive()).limit(4).collect(Collectors.toList()));
                fragmentHomeBinding.imageSlider.setSliderAdapter(slideNewAdapter);
                fragmentHomeBinding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                fragmentHomeBinding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                fragmentHomeBinding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                fragmentHomeBinding.imageSlider.setIndicatorSelectedColor(Color.WHITE);
                fragmentHomeBinding.imageSlider.setIndicatorUnselectedColor(Color.GRAY);
                fragmentHomeBinding.imageSlider.setScrollTimeInSec(4); //set scroll delay in seconds :
                fragmentHomeBinding.imageSlider.startAutoCycle();

            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e("msg",t.getMessage());
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void clickReadMore() {
        fragmentHomeBinding.readMore.setOnClickListener(view ->{
            startActivity(new Intent(context, ListNewsActivity.class));
        });
    }

    private void processClickSearch() {
        fragmentHomeBinding.lSearch.setOnClickListener(view -> {
            SearchBottomSheetModel bottomSheetFragment = new SearchBottomSheetModel();
            bottomSheetFragment.show(getActivity().getSupportFragmentManager(), bottomSheetFragment.getTag());
        });
    }

    private void processLogout() {
        fragmentHomeBinding.btnLogout.setOnClickListener(view -> {
            mDialog = new MaterialDialog.Builder(homeActivity)
                    .setTitle("Đăng xuất")
                    .setMessage("Bạn có muốn đăng xuất !")
                    .setCancelable(false)
                    .setPositiveButton("", R.drawable.logout_2, new MaterialDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            SharedPreferences sharedPreferences = context.getSharedPreferences("informationAccount",MODE_PRIVATE);
                            sharedPreferences.edit().clear().apply();
                            startActivity(new Intent(context, SplashActivity.class));
                            mDialog.dismiss();
                        }
                    })
                    .setNegativeButton("Cancel", R.drawable.close, new MaterialDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            dialogInterface.dismiss();
                        }
                    })
                    .build();
            mDialog.show();

        });
    }

    private void setData() {
        account = gson.fromJson(jsonAccount,Account.class);
        if(account.getRoleByRoleId().getRoleName().equals(ERole.Student.toString())){
            student = gson.fromJson(data,Student.class);
            fragmentHomeBinding.card.setText(student.getStudentCard());
            role = account.getRoleByRoleId().getRoleName();
        }else{
            fragmentHomeBinding.card.setVisibility(View.GONE);
            teacher = gson.fromJson(data,Teacher.class);
            role = account.getRoleByRoleId().getRoleName();
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
