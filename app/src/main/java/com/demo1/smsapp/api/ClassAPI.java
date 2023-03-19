package com.demo1.smsapp.api;

import com.demo1.smsapp.dto.ResponseModel;
import retrofit2.Call;
import retrofit2.http.*;

public interface ClassAPI {

    @GET("getClass/{classId}")
    Call<ResponseModel> getClassById(@Header("Authorization")String _token, @Path("classId")Integer classId);

    @GET("findClassByTeacher/{id}")
    Call<ResponseModel> findClassByTeacher(@Header("Authorization")String _token, @Path("id")Integer teacherId);

    @POST("findClassCode")
    @FormUrlEncoded
    Call<ResponseModel> findClassCode(@Header("Authorization")String _token, @Field("classCode") String classCode);



}
