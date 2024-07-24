package com.example.programmingipaddresstest.api;

import android.util.Log;

import com.example.programmingipaddresstest.models.IpAddress;
import com.example.programmingipaddresstest.models.ServerNatResponse;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiService {
    private final ApiInterface apiInterface;

    public ApiService(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public void submitIpAddress(String ipAddress, final OnResultListener onResultListener) {
        Call<ServerNatResponse> call = apiInterface.submitIpAddress(new IpAddress(ipAddress));
        call.enqueue(new Callback<ServerNatResponse>() {
            @Override
            public void onResponse(Call<ServerNatResponse> call, Response<ServerNatResponse> response) {
                if (response.isSuccessful()) {
                    onResultListener.onSuccess(response.body().isNat());
                    return;
                }
                String errorMessage = "";
                try {
                    ResponseBody errorBody = response.errorBody();
                    if (errorBody != null) {
                        errorMessage = errorBody.string();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                onResultListener.onError(errorMessage);
            }

            @Override
            public void onFailure(Call<ServerNatResponse> call, Throwable t) {
                onResultListener.onError(t.getMessage());
            }
        });
    }

    public interface OnResultListener {
        void onSuccess(boolean nat);
        void onError(String errorMessage);
    }
}