package com.github.akvast.weartest;

import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.List;
import java.util.concurrent.TimeUnit;


public final class WearService extends WearableListenerService {

    private static final String TAG = "WearService";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);

        GoogleApiClient client = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();

        if (!client.blockingConnect(30, TimeUnit.SECONDS).isSuccess()) {
            Log.e(TAG, "Failed to connect to GoogleApiClient.");
            return;
        }

        String nodeId = messageEvent.getSourceNodeId();
        String message = new String(messageEvent.getData());

        String[] split = message.split(":");
        double lat = Double.parseDouble(split[0].replace(',', '.'));
        double lon = Double.parseDouble(split[1].replace(',', '.'));

        Wearable.MessageApi.sendMessage(client, nodeId, "/test", "Resolving address...".getBytes());

        try {
            Geocoder geocoder = new Geocoder(this);
            List<Address> addressList = geocoder.getFromLocation(lat, lon, 1);
            message = addressList.get(0).getAddressLine(0);
        } catch (Exception ex) {
            message = lat + "\n" + lon;
        }

        Wearable.MessageApi.sendMessage(client, nodeId, "/test", message.getBytes());

        client.disconnect();
    }

}
