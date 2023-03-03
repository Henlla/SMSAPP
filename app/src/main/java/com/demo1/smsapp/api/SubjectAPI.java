package com.demo1.smsapp.api;

import com.demo1.smsapp.dto.ResponseModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface SubjectAPI {

    @POST("findSubjectByMajorIdSemester")
    @FormUrlEncoded
    Call<ResponseModel> findSubjectByMajorIdSemester(@Header("Authorization")String _token,
                                                     @Field("majorId")Integer majorId,
                                                     @Field("semester")Integer semester);
}
