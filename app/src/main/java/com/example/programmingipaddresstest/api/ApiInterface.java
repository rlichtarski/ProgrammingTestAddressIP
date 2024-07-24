package com.example.programmingipaddresstest.api;

import com.example.programmingipaddresstest.models.IpAddress;
import com.example.programmingipaddresstest.models.ServerNatResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {
    @Headers("Content-Type: application/json")
    @POST(".")
    Call<ServerNatResponse> submitIpAddress(@Body IpAddress ipAddress);
}