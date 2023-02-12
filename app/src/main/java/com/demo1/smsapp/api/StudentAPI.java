package com.demo1.smsapp.api;

import com.demo1.smsapp.models.Profile;
import com.demo1.smsapp.models.Student;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface StudentAPI {
    @GET("getByProfile/{id}")
    Call<Student> getStudentByProfileId(@Header("Authorization")String _token, @Path("id")Integer id);
}
