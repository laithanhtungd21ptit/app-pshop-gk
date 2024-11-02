package com.example.pshop;

import android.os.Bundle;
import android.view.View;
import com.example.pshop.databinding.ActivityMainBinding;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        initBanner();
    }

    private void initBanner() {
        DatabaseReference myRef=database.getReference("Banner");
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<>
    }
}
