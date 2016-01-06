package se.liu.student.frejo105.beerapp.Utility;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import cz.msebera.android.httpclient.client.HttpResponseException;
import se.liu.student.frejo105.beerapp.API.RequestCompleteCallback;
import se.liu.student.frejo105.beerapp.Model.Location;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceDetectionApi;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;

/**
 * Created by vakz on 2015-12-29.
 */
public class Utility {
    public static boolean isNullEmptyOrWhitespace(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static void getCurrentLocation(Context context, final RequestCompleteCallback<Location> cb) {
        final GoogleApiClient client = new GoogleApiClient
                .Builder(context)
                .addApi(LocationServices.API)
                .build();

        GoogleApiClient.ConnectionCallbacks callback = new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                android.location.Location lastKnown = LocationServices.FusedLocationApi.getLastLocation(client);
                if (lastKnown !=  null) {
                    Location loc = new Location();
                    loc.longitude = lastKnown.getLongitude();
                    loc.latitude = lastKnown.getLatitude();
                    cb.onSuccess(loc);

                }
                else {
                    cb.onFailure(new HttpResponseException(400, "Unable to retrieve location"));
                }
                client.disconnect();
            }

            @Override
            public void onConnectionSuspended(int i) {
                // Not an issue
            }
        };

        GoogleApiClient.OnConnectionFailedListener failedCb = new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                cb.onFailure(new HttpResponseException(400, connectionResult.getErrorMessage()));
                client.disconnect();
            }
        };


        client.registerConnectionCallbacks(callback);
        client.registerConnectionFailedListener(failedCb);
        client.connect();
    }

    /**
     * Formats a distance to a convenient format. If the distance is longer than 1000,
     * the returned distance will be in kilometers.
     * @param distance The distance to format
     * @return A string, rounded and in a proper unit
     */
    public static String formatDistanceString(double distance) {
        String unit = " m";
        distance = Math.round(distance);
        if (distance > 1000) {
            distance = Math.round(distance / 100) / 10;
            unit = " km";
        }
        return String.valueOf(distance) + unit;
    }
}
