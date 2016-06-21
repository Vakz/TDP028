package se.liu.student.frejo105.beerapp.activities;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import cz.msebera.android.httpclient.client.HttpResponseException;
import se.liu.student.frejo105.beerapp.api.HttpCallback;
import se.liu.student.frejo105.beerapp.api.HttpClient;
import se.liu.student.frejo105.beerapp.api.model.Point;
import se.liu.student.frejo105.beerapp.api.model.Pub;
import se.liu.student.frejo105.beerapp.R;
import se.liu.student.frejo105.beerapp.utility.LocationCallback;

public class MainActivity extends DrawerMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLocation(new LocationCallback() {
            @Override
            public void onDone(Point point) {
                if (point == null) {
                    System.out.println("Was null");
                }
                else {
                    System.out.println(point.longitude);
                }

            }
        });
    }


}
