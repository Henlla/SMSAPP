package com.demo1.smsapp.api;

import com.demo1.smsapp.models.Device;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DeviceAPI {

    @POST("/save")
    Call<Void> saveDevice(@Body Device device);
}
