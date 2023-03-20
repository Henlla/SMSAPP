package com.demo1.smsapp.api;

import com.demo1.smsapp.dto.ResponseModel;
import retrofit2.Call;
import retrofit2.http.*;

public interface ScheduleAPI {

    @POST("getScheduleByClassAndSemester")
    @FormUrlEncoded
    Call<ResponseModel> getScheduleByClassAndSemester(@Header("Authorization") String _toke,
                                                      @Field("classId") Integer classId,
                                                      @Field("semester") Integer semester);

    @POST("getScheduleByClass")
    @FormUrlEncoded
    Call<ResponseModel> getScheduleByClass(@Header("Authorization") String _toke,
                                           @Field("classId") Integer classId);

    @GET("findScheduleById/{scheduleId}")
    Call<ResponseModel> getScheduleByScheduleDetail(@Header("Authorization") String token,
                                                    @Path("scheduleId") Integer id);

}
