package com.demo1.smsapp.api;

import com.demo1.smsapp.dto.ResponseModel;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApplicationAPI {
    @POST("save")
    @FormUrlEncoded
    Call<ResponseModel> post_app(@Header("Authorization") String _token, @Field("application") String application);

    @GET("finApplicationByStudentId/{id}")
    Call<ResponseModel> getByStudentId(@Header("Authorization") String _token, @Path("id") Integer studentId);
}
