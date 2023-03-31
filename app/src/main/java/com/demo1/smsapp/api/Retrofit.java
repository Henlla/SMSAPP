package com.demo1.smsapp.api;

import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit {
    public static retrofit2.Retrofit retrofit = null;
    public static retrofit2.Retrofit getRetrofitClient(String URL){
        if(retrofit ==null){
            retrofit  = new retrofit2.Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }else {
            if (!retrofit.baseUrl().equals(URL)) {
                retrofit = new retrofit2.Retrofit.Builder()
                        .baseUrl(URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
        }
        return retrofit;
    }
}
