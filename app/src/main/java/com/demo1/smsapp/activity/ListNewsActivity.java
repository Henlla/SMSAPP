package com.demo1.smsapp.activity;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.demo1.smsapp.R;
import com.demo1.smsapp.adapter.ListNewAdapter;
import com.demo1.smsapp.adapter.NewAdapter;
import com.demo1.smsapp.api.NewsAPI;
import com.demo1.smsapp.api.utils.APIUtils;
import com.demo1.smsapp.databinding.ActivityListNewsBinding;
import com.demo1.smsapp.dto.ResponseModel;
import com.demo1.smsapp.models.News;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListNewsActivity extends AppCompatActivity {
    ActivityListNewsBinding listNewsBinding;
    ListNewAdapter adapter;
    NewsAPI newsAPI;
    Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listNewsBinding = ActivityListNewsBinding.inflate(getLayoutInflater());
        newsAPI = APIUtils.getNews();
        gson = new Gson();
        adapter = new ListNewAdapter();
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.red));
        setListNews();
        setOnClickNews();
        processBtnBack();
        setContentView(listNewsBinding.getRoot());
    }

    private void processBtnBack() {
        listNewsBinding.icBack.setOnClickListener(view->{
            finish();
        });
    }

    private void setOnClickNews() {
        adapter.ItemOnClick(new ListNewAdapter.OnClickItem() {
            @Override
            public void ItemOnclick(News news) {
                Intent intent = new Intent(ListNewsActivity.this, NewDetailsActivity.class);
                String json = gson.toJson(news);
                intent.putExtra("newJson",json);
                startActivity(intent);
            }
        });
    }
    private void setListNews() {
        newsAPI.findAll().enqueue(new Callback<ResponseModel>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                String json = gson.toJson(response.body().getData());
                Type listType = new TypeToken<ArrayList<News>>(){}.getType();
                List<News> listNews = gson.fromJson(json, listType);
                listNewsBinding.rcvListNews.setLayoutManager(new LinearLayoutManager(ListNewsActivity.this));
                adapter.setData(listNews.stream().filter(News::isActive).collect(Collectors.toList()));
                listNewsBinding.rcvListNews.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e("msg",t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}