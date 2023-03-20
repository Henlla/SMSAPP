package com.demo1.smsapp.api;

import com.demo1.smsapp.dto.ResponseModel;
import com.demo1.smsapp.models.Teacher;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface TeacherAPI {

    @GET("getByProfile/{id}")
    Call<Teacher> getTeacherByProfileId(@Header("Authorization") String _token, @Path("id") Integer id);

    @GET("get/{id}")
    Call<ResponseModel> getTeacherByClassId(@Header("Authorization") String _token, @Path("id") Integer id);
}
