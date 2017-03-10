package com.github.akvast.weartest.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.github.akvast.weartest.R;
import com.github.akvast.weartest.databinding.ActivityMainBinding;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends BaseActivity {

    private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();

    private static final String BACKGROUND = "http://cdn.shopify.com/s/files/1/1065/1408/products/vintage-iron-dark-rusty-muslin-backdrops-144_1024x1024_07bdd2ad-a328-4447-936e-fd1b4fcf398f_grande.jpeg?v=1484211890";

    private final ObservableField<String> mLocation = new ObservableField<>("");

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

    public ObservableField<String> getLocation() {
        return mLocation;
    }

    @Override
    public void onLocationChanged(final Location location) {
        super.onLocationChanged(location);

        EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Geocoder geocoder = new Geocoder(MainActivity.this);
                    List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    mLocation.set(addressList.get(0).getAddressLine(0));
                } catch (Exception ex) {
                    mLocation.set(location.getLatitude() + "\n" + location.getLongitude());
                }
            }
        });
    }

}