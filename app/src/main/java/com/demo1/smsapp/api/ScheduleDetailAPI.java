package com.demo1.smsapp.api;

import com.demo1.smsapp.dto.ResponseModel;
import retrofit2.Call;
import retrofit2.http.*;

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

    @POST("findScheduleDetailsByDateBetweenAndTeacherId")
    @FormUrlEncoded
    Call<ResponseModel> findScheduleDetailByDateBetweenAndTeacherId(@Header("Authorization") String token,
                                                                    @Field("fromDate") String fromDate,
                                                                    @Field("toDate") String toDate,
                                                                    @Field("teacherId") Integer teacherId);

    @POST("findScheduleDetailByDateBetweenAndScheduleId")
    @FormUrlEncoded
    Call<ResponseModel> findScheduleByDateBetweenAndScheduleId(@Header("Authorization") String token,
                                                               @Field("fromDate") String fromDate,
                                                               @Field("toDate") String toDate,
                                                               @Field("scheduleId") Integer scheduleId);

    @POST("findScheduleDetailsByDateBetweenAndScheduleIdAndTeacherId")
    @FormUrlEncoded
    Call<ResponseModel> findScheduleDetailsByDateBetweenAndScheduleIdAndTeacherId(@Header("Authorization") String token,
                                                                                  @Field("fromDate") String fromDate,
                                                                                  @Field("toDate") String toDate,
                                                                                  @Field("scheduleId") Integer scheduleId,
                                                                                  @Field("teacherId") Integer teacherId);

    @POST("findScheduleDetailsByDateAndTeacherId")
    @FormUrlEncoded
    Call<ResponseModel> findScheduleDetailsByDateAndTeacherId(@Header("Authorization") String token,
                                                              @Field("date") String date,
                                                              @Field("teacherId") Integer teacherId);

    @POST("findScheduleDetailsByDateAndScheduleIdAndTeacherId")
    @FormUrlEncoded
    Call<ResponseModel> findScheduleDetailsByDateAndScheduleIdAndTeacherId(@Header("Authorization") String token,
                                                                           @Field("date") String date,
                                                                           @Field("scheduleId") Integer scheduleId,
                                                                           @Field("teacherId") Integer teacherId);

    @GET("findScheduleByTeacher/{teacherId}")
    Call<ResponseModel> findScheduleByTeacher(@Header("Authorization") String token, @Path("teacherId") Integer teacherId);
}
