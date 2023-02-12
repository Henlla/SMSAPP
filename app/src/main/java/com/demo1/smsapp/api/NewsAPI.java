package com.demo1.smsapp.api;

import com.demo1.smsapp.dto.ResponseModel;
import com.demo1.smsapp.models.News;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface NewsAPI {

    @GET("list")
    Call<ResponseModel> findAll();

    @GET("get/{id}")
    Call<ResponseModel> findNewById(@Path("id")Integer id);
}
