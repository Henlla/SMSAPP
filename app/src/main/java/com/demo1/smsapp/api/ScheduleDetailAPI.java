package com.demo1.smsapp.api;

import com.demo1.smsapp.dto.ResponseModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ScheduleDetailAPI {
    @POST("findScheduleDetailByScheduleIdAndSubjectId")
    @FormUrlEncoded
    Call<ResponseModel> getScheduleDetailByScheduleIdAndSubjectId(@Header("Authorization") String _token,
                                                                  @Field("scheduleId") Integer scheduleId,
                                                                  @Field("subjectId") Integer subjectId);

    @POST("findScheduleDetailsByDate")
    @FormUrlEncoded
    Call<ResponseModel> getScheduleDetailByDate(@Header("Authorization") String _token,
                                                @Field("date") String date);


    @POST("findScheduleDetail")
    @FormUrlEncoded
    Call<ResponseModel> findScheduleDetailByDateAndScheduleId(@Header("Authorization") String _token,
                                                              @Field("date") String date,
                                                              @Field("scheduleId") Integer scheduleId);

    @POST("findScheduleDetailBySlot")
    @FormUrlEncoded
    Call<ResponseModel> findScheduleDetailByDateScheduleSlot(@Header("Authorization") String _token,
                                                             @Field("date") String date,
                                                             @Field("scheduleId") Integer scheduleId,
                                                             @Field("slot") Integer slot);

    @POST("findScheduleDetailByDateBetween")
    @FormUrlEncoded
    Call<ResponseModel> findScheduleDetailByDateBetween(@Header("Authorization") String _token,
                                                        @Field("fromDate") String fromDate,
                                                        @Field("toDate") String toDate);

    @POST("findScheduleDetailByDateBetweenAndScheduleId")
    @FormUrlEncoded
    Call<ResponseModel> findScheduleByDateBetweenAndScheduleId(@Header("Authorization") String token,
                                                               @Field("fromDate") String fromDate,
                                                               @Field("toDate") String toDate,
                                                               @Field("scheduleId") Integer scheduleId);
}
