package com.demo1.smsapp.api;

import com.demo1.smsapp.dto.ResponseModel;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApplicationTypeAPI {
    @GET("list")
    Call<ResponseModel> getAll();
}
