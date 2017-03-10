package com.github.akvast.weartest.ui;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.github.akvast.weartest.R;
import com.github.akvast.weartest.databinding.ActivityMainBinding;

public class MainActivity extends Activity {

    private static final String BACKGROUND = "http://cdn.shopify.com/s/files/1/1065/1408/products/vintage-iron-dark-rusty-muslin-backdrops-144_1024x1024_07bdd2ad-a328-4447-936e-fd1b4fcf398f_grande.jpeg?v=1484211890";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setActivity(this);
    }

    public void openListActivity(@NonNull View view) {
        startActivity(new Intent(this, ListActivity.class));
    }

    public String getBackground() {
        return BACKGROUND;
    }

}