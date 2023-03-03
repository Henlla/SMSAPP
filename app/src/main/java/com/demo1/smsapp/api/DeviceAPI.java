package com.demo1.smsapp.api;

import com.demo1.smsapp.models.Device;
import retrofit2.Call;
import retrofit2.http.*;

public interface DeviceAPI {

    @POST("save")
    @FormUrlEncoded
    Call<Device> saveDevice(@Field("device") String device);
    @POST("put")
    @FormUrlEncoded
    Call<Device> putDevice(@Field("device") String device);
    @GET("findByAccountId/{accountId}")
    Call<Device> findByAccountId(@Path("accountId")Integer accountId);
}
