package com.example.programmingipaddresstest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.programmingipaddresstest.databinding.ActivityMainBinding;
import com.example.programmingipaddresstest.viewmodels.IpAddressViewModel;

public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("address_ip");
    }

    private native String getAddressIP();
    private ActivityMainBinding binding;
    private IpAddressViewModel ipAddressViewModel;
    private final Handler handler = new Handler();
    private boolean loadingShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ipAddressViewModel = new ViewModelProvider(this).get(IpAddressViewModel.class);

        String ipAddress = getAddressIP();
        ipAddressViewModel.submitIpAddress(ipAddress);
        showLoadingAfterDelay();

        ipAddressViewModel.getNatResponse().observe(this, nat -> {
            if (!loadingShown) {
                showLoadingImmediately();
            }
            hideLoading();
            if (nat != null) {
                binding.resultIcon.setVisibility(View.VISIBLE);
                if (nat) {
                    binding.resultIcon.setImageResource(R.drawable.green_ok_icon);
                    binding.ipTextView.setText(ipAddress);
                } else {
                    binding.resultIcon.setImageResource(R.drawable.red_not_ok_icon);
                    binding.ipTextView.setText(ipAddress);
                }
            }
        });

        ipAddressViewModel.getErrorResponse().observe(this, errorMessage -> {
            if (!loadingShown) {
                showLoadingImmediately();
            }
            hideLoading();
            if (errorMessage != null) {
                binding.ipTextView.setText(errorMessage);
            }
        });

    }

    private void showLoadingAfterDelay() {
        handler.postDelayed(() -> {
            if (!loadingShown) {
                showLoadingImmediately();
            }
        }, 3000);
    }

    private void showLoadingImmediately() {
        binding.resultIcon.setVisibility(View.GONE);
        binding.ipTextView.setText("");
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.pleaseWaitText.setVisibility(View.VISIBLE);
        loadingShown = true;
    }

    private void hideLoading() {
        if (binding.pleaseWaitText.getVisibility() == View.VISIBLE) {
            binding.pleaseWaitText.setVisibility(View.GONE);
        }
        if (binding.progressBar.getVisibility() == View.VISIBLE) {
            binding.progressBar.setVisibility(View.GONE);
        }
    }

    public native String stringFromJNI();
}