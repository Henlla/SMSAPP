package com.demo1.smsapp.api;

import com.demo1.smsapp.dto.ResponseModel;
import retrofit2.Call;
import retrofit2.http.*;

public interface StudentSubjectAPI {
    @POST("findStudentSubjectBySubjectIdAndStudentId")
    @FormUrlEncoded
    Call<ResponseModel> findStudentSubjectBySubjectIdAndStudentId(@Header("Authorization") String _token,
                                                                  @Field("subjectId") Integer subjectId,
                                                                  @Field("studentId") Integer studentId);

    @POST("getOne")
    @FormUrlEncoded
    Call<ResponseModel> findStudentSubjectByStudentIdAndSubjectId(@Header("Authorization") String token,
                                                                  @Field("studentId") Integer studentId,
                                                                  @Field("subjectId") Integer subjectId);

    @GET("getByAttendanceId/{id}")
    Call<ResponseModel> findStudentSubjectByAttendance(@Header("Authorization") String token,
                                                       @Path("id") Integer studentSubjectId);

    @GET("getById/{id}")
    Call<ResponseModel> findStudentSubjectById(@Header("Authorization") String token,
                                               @Path("id") Integer id);

    @POST("updateAll")
    @FormUrlEncoded
    Call<ResponseModel> updateSubjectStatus(@Header("Authorization")String token,
                                            @Field("student_subject")String studentSubjectList);
}
