package com.demo1.smsapp.api;

import com.demo1.smsapp.dto.ResponseModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface StudentMajorAPI {
    @GET("findMajorStudentByStudentId/{studentId}")
    Call<ResponseModel> getMajorByStudentId(@Header("Authorization") String _token,@Path("studentId")Integer studentId);
}
