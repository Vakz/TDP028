package se.liu.student.frejo105.beerapp.Utility;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import cz.msebera.android.httpclient.client.HttpResponseException;
import se.liu.student.frejo105.beerapp.API.RequestCompleteCallback;
import se.liu.student.frejo105.beerapp.Model.Location;

/**
 * Created by vakz on 2016-01-07.
 */
public class LocationProvider {

    GoogleApiClient client;

    public LocationProvider(Context context, final BasicCallback cb) {
        client = new GoogleApiClient
                .Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        cb.onSuccess();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        cb.onFailure(new Exception("Unable to connect to google api"));
                    }
                })
                .build();
        client.connect();
    }

    public void getCurrentLocation(final RequestCompleteCallback<Location> cb) {
        android.location.Location lastKnown = LocationServices.FusedLocationApi.getLastLocation(client);
        if (!client.isConnected()) cb.onFailure(new HttpResponseException(400, "LocationServices not connected"));
        else if (lastKnown !=  null) {
            Location loc = new Location();
            loc.longitude = lastKnown.getLongitude();
            loc.latitude = lastKnown.getLatitude();
            cb.onSuccess(loc);

        }
        else {
            cb.onFailure(new HttpResponseException(400, "Unable to retrieve location"));
        }
    }

    @Override
    protected void finalize() throws Throwable {
        client.disconnect();
        super.finalize();
    }
}
