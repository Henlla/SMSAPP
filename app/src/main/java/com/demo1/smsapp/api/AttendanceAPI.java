package com.demo1.smsapp.api;

import com.demo1.smsapp.dto.ResponseModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AttendanceAPI {
    @POST("findAttendanceByDateAndSlotAndStudentSubjectAndShift")
    @FormUrlEncoded
    Call<ResponseModel> findAttendanceByDateAndSlotAndStudentSubjectAndShift(@Header("Authorization") String _token,
                                                                             @Field("date") String date,
                                                                             @Field("slot") Integer slot,
                                                                             @Field("studentSubjectId") Integer studentSubjectId,
                                                                             @Field("shift") String shift);

    @POST("saveAll")
    @FormUrlEncoded
    Call<ResponseModel> saveAll(@Header("Authorization") String _token,
                                @Field("list_attendance") String listAttendance);

    @POST("findAttendanceByDateSlotAndShift")
    @FormUrlEncoded
    Call<ResponseModel> findAttendanceByDateSlotAndShift(@Header("Authorization") String token,
                                                         @Field("date") String date,
                                                         @Field("slot") Integer slot,
                                                         @Field("shift") String shift);


    @POST("findAttendanceByStudentSubjectId")
    @FormUrlEncoded
    Call<ResponseModel> findAttendanceByStudentSubjectId(@Header("Authorization") String token,
                                                         @Field("student_subject_id") Integer studentSubjectId);
}
