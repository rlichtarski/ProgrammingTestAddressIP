package com.example.programmingipaddresstest.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.programmingipaddresstest.api.ApiInterface;
import com.example.programmingipaddresstest.api.ApiService;
import com.example.programmingipaddresstest.api.RetrofitClient;

public class IpAddressViewModel extends ViewModel {
    private final ApiService apiService;
    private final MutableLiveData<Boolean> natResponse;
    private final MutableLiveData<String> errorResponse;

    public IpAddressViewModel() {
        ApiInterface apiInterface = RetrofitClient.getInstance().create(ApiInterface.class);
        apiService = new ApiService(apiInterface);
        natResponse = new MutableLiveData<>();
        errorResponse = new MutableLiveData<>();
    }

    public LiveData<Boolean> getNatResponse() {
        return natResponse;
    }

    public LiveData<String> getErrorResponse() {
        return errorResponse;
    }

    public void submitIpAddress(String ipAddress) {
        apiService.submitIpAddress(ipAddress, new ApiService.OnResultListener() {
            @Override
            public void onSuccess(boolean nat) {
                natResponse.postValue(nat);
                errorResponse.postValue(null);
            }

            @Override
            public void onError(String errorMessage) {
                natResponse.postValue(null);
                errorResponse.postValue(errorMessage);
            }
        });
    }
}