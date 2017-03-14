package com.github.akvast.weartest.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.akvast.weartest.R;
import com.github.akvast.weartest.databinding.ActivityMainBinding;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.Locale;

public class MainActivity extends BaseActivity implements MessageApi.MessageListener {

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
    public void onConnected(@Nullable Bundle bundle) {
        super.onConnected(bundle);

        Wearable.MessageApi.addListener(mGoogleApiClient, this);
    }

    @Override
    public void onLocationChanged(final Location location) {
        super.onLocationChanged(location);

        new Thread() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
                String nodeId = nodes.getNodes().get(0).getId();
                String message = String.format(Locale.getDefault(), "%f:%f", location.getLatitude(), location.getLongitude());
                Wearable.MessageApi.sendMessage(mGoogleApiClient, nodeId, "/test", message.getBytes());
            }
        }.start();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        mLocation.set(new String(messageEvent.getData()));
    }
}