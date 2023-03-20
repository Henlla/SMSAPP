package com.demo1.smsapp.api;

import com.demo1.smsapp.dto.ResponseModel;
import retrofit2.Call;
import retrofit2.http.*;

public interface SubjectAPI {

    @POST("findSubjectByMajorIdSemester")
    @FormUrlEncoded
    Call<ResponseModel> findSubjectByMajorIdSemester(@Header("Authorization") String _token,
                                                     @Field("majorId") Integer majorId,
                                                     @Field("semester") Integer semester);

    @POST("findSubjectBySemesterIdAndMajorId")
    @FormUrlEncoded
    Call<ResponseModel> findSubjectBySemesterIdAndMajorId(@Header("Authorization") String _token,
                                                          @Field("fromSemester") Integer fromSemester,
                                                          @Field("toSemester") Integer toSemester,
                                                          @Field("majorId") Integer majorId);

    @GET("findOne/{id}")
    Call<ResponseModel> findSubjectById(@Header("Authorization") String token,
                                        @Path("id") Integer subjectId);

}
