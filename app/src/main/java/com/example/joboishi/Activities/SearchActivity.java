package com.example.joboishi.Activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.joboishi.R;
import com.example.joboishi.databinding.SearchLayoutBinding;

public class SearchActivity extends AppCompatActivity {

    private SearchLayoutBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SearchLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}