package com.demo1.smsapp.fragment;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bumptech.glide.Glide;
import com.demo1.smsapp.R;
import com.demo1.smsapp.activity.*;
import com.demo1.smsapp.adapter.ListFunctionAdapter;
import com.demo1.smsapp.adapter.NewAdapter;
import com.demo1.smsapp.adapter.SlideNewAdapter;
import com.demo1.smsapp.api.ClassAPI;
import com.demo1.smsapp.api.NewsAPI;
import com.demo1.smsapp.api.ScheduleAPI;
import com.demo1.smsapp.api.StudentClassAPI;
import com.demo1.smsapp.api.utils.APIUtils;
import com.demo1.smsapp.databinding.FragmentHomeBinding;
import com.demo1.smsapp.dto.FunctionModel;
import com.demo1.smsapp.dto.ResponseModel;
import com.demo1.smsapp.enums.ERole;
import com.demo1.smsapp.models.*;
import com.demo1.smsapp.utils.ConvertDayOfWeek;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
    String _token;
    String role;
    private StudentClassAPI studentClassAPI;
    private ScheduleAPI scheduleAPI;
    private ClassAPI classAPI;
    private Classses classses;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater);
        firebaseStorage = FirebaseStorage.getInstance();
        newsAPI = APIUtils.getNews();
        adapter = new NewAdapter();
        gson = new Gson();
        studentClassAPI = APIUtils.getStudentClass();
        scheduleAPI = APIUtils.getScheduleAPI();
        classAPI = APIUtils.getClasses();
        classses = new Classses();
        slideNewAdapter = new SlideNewAdapter();
        listFunctionAdapter = new ListFunctionAdapter();
        context = inflater.getContext();
        homeActivity = (HomeActivity) getActivity();
        jsonAccount = homeActivity.getAccountJson();
        profileJson = homeActivity.getProfileJson();
        _token = homeActivity.get_token();
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
        setNotificationSchedule();
        processBtnCloseNotification();
        return fragmentHomeBinding.getRoot();
    }

    private void processBtnCloseNotification() {
        fragmentHomeBinding.btnCloseNotice.setOnClickListener(view -> {
            fragmentHomeBinding.layoutNotification.setVisibility(View.GONE);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setNotificationSchedule() {
        String role = homeActivity.getRole();
        if (role.equals(ERole.Student.toString())) {
            student = gson.fromJson(data, Student.class);
            LocalDate currenDate = LocalDate.now();
            studentClassAPI.getClassIdByStudentId(_token, student.getId()).enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    String json = gson.toJson(response.body().getData());
                    Type studentClassType = new TypeToken<ArrayList<StudentClass>>() {
                    }.getType();
                    List<StudentClass> studentClasses = gson.fromJson(json, studentClassType);
                    for (StudentClass studentClass : studentClasses) {
                        classAPI.getClassById(_token, studentClass.getClassId()).enqueue(new Callback<ResponseModel>() {
                            @Override
                            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                String jsonClass = gson.toJson(response.body().getData());
//                                Type classType = new TypeToken<ResponseModel>(){}.getType();
                                classses = gson.fromJson(jsonClass, Classses.class);
                                scheduleAPI.getScheduleByClass(_token, classses.getId()).enqueue(new Callback<ResponseModel>() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                        if (response.isSuccessful()) {
                                            String jsonResponse = gson.toJson(response.body().getData());
                                            Type scheduleType = new TypeToken<ArrayList<Schedule>>() {
                                            }.getType();
                                            List<Schedule> scheduleList = gson.fromJson(jsonResponse, scheduleType);
                                            for (Schedule schedule : scheduleList) {
                                                for (ScheduleDetail scheduleDetail : schedule.getScheduleDetailsById()) {
                                                    if (LocalDate.parse(scheduleDetail.getDate()).equals(currenDate)) {
                                                        fragmentHomeBinding.layoutNotification.setVisibility(View.VISIBLE);
                                                        fragmentHomeBinding.subName.setText("Môn: " + scheduleDetail.getSubjectBySubjectId().getSubjectName());
                                                        fragmentHomeBinding.time.setText("Vào lúc: " + ConvertDayOfWeek.convertShift(classses.getShift()));
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseModel> call, Throwable t) {

                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<ResponseModel> call, Throwable t) {
                                Log.e("msg", t.getMessage());
                            }
                        });
                    }

                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {

                }
            });

        }
    }

    public void setOnClickFunction() {
        listFunctionAdapter.ClickFunction(new ListFunctionAdapter.IClickItem() {
            @Override
            public void ClickFunction(String functionName) {
                if (functionName.equals(SCHEDULES.toString())) {
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
        fragmentHomeBinding.listFunc.setLayoutManager(new GridLayoutManager(getContext(), 3));
    }


    public List<FunctionModel> listF() {
        List<FunctionModel> functionModels = new ArrayList<>();
        FunctionModel attendance = new FunctionModel(ERole.Teacher.toString(), "Attendance", ATTENDANCE.toString(), R.drawable.attendance);
        FunctionModel attendanceStudent = new FunctionModel(ERole.Student.toString(), "Attendance", ATTENDANCE.toString(), R.drawable.attendance);
        FunctionModel mark = new FunctionModel(ERole.Teacher.toString(), "Mark", MARK.toString(), R.drawable.logout_2);
        FunctionModel schedule = new FunctionModel(ERole.Student.toString(), "Timetable", SCHEDULES.toString(), R.drawable.schedule);
        FunctionModel application = new FunctionModel(ERole.Student.toString(), "Application", APPLICATION.toString(), R.drawable.resume);
        FunctionModel checkMark = new FunctionModel(ERole.Student.toString(), "Mark", MARK.toString(), R.drawable.check_mark);
        functionModels.add(attendance);
        functionModels.add(mark);
        functionModels.add(schedule);
        functionModels.add(application);
        functionModels.add(attendanceStudent);
        functionModels.add(checkMark);
        return functionModels;
    }


    private void setUpSlideAuto() {
        newsAPI.findAll().enqueue(new Callback<ResponseModel>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                String json = gson.toJson(response.body().getData());
                Type listType = new TypeToken<ArrayList<News>>() {
                }.getType();
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
                Log.e("msg", t.getMessage());
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void clickReadMore() {
        fragmentHomeBinding.readMore.setOnClickListener(view -> {
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
                    .setTitle("Logout")
                    .setMessage("Do you wanna logout !")
                    .setCancelable(false)
                    .setPositiveButton("", R.drawable.logout_2, new MaterialDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            SharedPreferences sharedPreferences = context.getSharedPreferences("informationAccount", MODE_PRIVATE);
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
        account = gson.fromJson(jsonAccount, Account.class);
        if (account.getRoleByRoleId().getRoleName().equals(ERole.Student.toString())) {
            student = gson.fromJson(data, Student.class);
            fragmentHomeBinding.card.setText(student.getStudentCard());
            role = account.getRoleByRoleId().getRoleName();
        } else {
            fragmentHomeBinding.card.setVisibility(View.GONE);
            teacher = gson.fromJson(data, Teacher.class);
            role = account.getRoleByRoleId().getRoleName();
        }
        Profile profile = gson.fromJson(profileJson, Profile.class);
        StorageReference reference = firebaseStorage.getReference().child(profile.getAvatarPath());
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e("uri",uri.toString());
                Glide.with(context)
                        .load(uri)
                        .error(R.drawable.image_notavailable)
                        .centerCrop()
                        .into(fragmentHomeBinding.profileImage);
            }
        });
        String fullName = profile.getFirstName() + " " + profile.getLastName();
        fragmentHomeBinding.username.setText(fullName);
    }

    private void setOnClickNews() {
        adapter.ItemOnClick(new NewAdapter.OnClickItem() {
            @Override
            public void ItemOnclick(News news) {
                Intent intent = new Intent(context, NewDetailsActivity.class);
                String json = gson.toJson(news);
                intent.putExtra("newJson", json);
                context.startActivity(intent);
            }
        });
    }

    public void setListNews() {
        newsAPI.findAll().enqueue(new Callback<ResponseModel>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                String json = gson.toJson(response.body().getData());
                Type listType = new TypeToken<ArrayList<News>>() {
                }.getType();
                List<News> listNews = gson.fromJson(json, listType);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2) {
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
                Log.e("msg", t.getMessage());
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
