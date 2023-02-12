package com.demo1.smsapp.api;

import com.demo1.smsapp.dto.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AccountAPI {

    @POST("login")
    @FormUrlEncoded
    Call<LoginResponse> login(@Field("username")String username, @Field("password")String password);
}
