package com.demo1.smsapp.api;

import com.demo1.smsapp.dto.ResponseModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AttendanceTrackingApi {
    @POST("saveTracking")
    @FormUrlEncoded
    Call<ResponseModel> saveTracking(@Header("Authorization") String token,
                                     @Field("attendance_tracking")String tracking);
}
