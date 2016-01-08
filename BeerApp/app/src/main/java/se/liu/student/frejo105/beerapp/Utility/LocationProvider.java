package se.liu.student.frejo105.beerapp.Utility;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.client.HttpResponseException;
import se.liu.student.frejo105.beerapp.API.RequestCompleteCallback;
import se.liu.student.frejo105.beerapp.Model.Location;

/**
 * geo fix 15.6426103 58.3914988
 */
public class LocationProvider implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    GoogleApiClient client;
    private List<RequestCompleteCallback<Location>> waiting = new ArrayList<>();
    private android.location.Location lastKnown = null;
    private LocationRequest locationRequest = null;

    public LocationProvider(Context context) {
        client = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        client.connect();
        locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setNumUpdates(1);
    }

    public void addListener(RequestCompleteCallback<Location> listener) {
        waiting.add(listener);
        // In case updates have at some point been turned off, turn them back on
        if (waiting.size() == 1 && client.isConnected()) LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void removeListener(RequestCompleteCallback<Location> listener) {
        waiting.remove(listener);
        if (waiting.size() == 0 && client.isConnected()) LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        for (RequestCompleteCallback<Location> listener : waiting) {
            listener.onFailure(new HttpResponseException(400, "Unable to get position"));
        }
        waiting.clear();
    }

    private void notifyListeners() {
        Location location = new Location();
        location.latitude = lastKnown.getLatitude();
        location.longitude = lastKnown.getLongitude();
        for (RequestCompleteCallback<Location> listener : waiting) {
            listener.onSuccess(location);
        }
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        System.out.println(location.toString());
        lastKnown = location;
        notifyListeners();
    }
}
