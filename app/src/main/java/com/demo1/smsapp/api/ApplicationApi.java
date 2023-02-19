package com.demo1.smsapp.api;

import com.demo1.smsapp.dto.ResponseModel;
import com.demo1.smsapp.models.Application;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApplicationApi {
    @GET("get_one/{id}")
    Call<Application> findOne(@Path("id") int id);
    @POST("save")
    @FormUrlEncoded
    Call<ResponseModel> post_app(@Header("Authorization") String _token, @Field("application") String application);
}
