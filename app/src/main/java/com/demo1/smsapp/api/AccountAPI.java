package com.demo1.smsapp.api;

import com.demo1.smsapp.dto.LoginResponse;
import com.demo1.smsapp.dto.ResponseModel;
import retrofit2.Call;
import retrofit2.http.*;

public interface AccountAPI {

    @POST("login")
    @FormUrlEncoded
    Call<LoginResponse> login(@Field("username")String username, @Field("password")String password);

    @PUT("changePassword/{id}")
    @FormUrlEncoded
    Call<ResponseModel> changePassword(@Header("Authorization")String _token,
                                       @Path ("id")Integer id,@Field("password")String password,
                                       @Field("newPassword")String newPassword);
}
