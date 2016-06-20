package se.liu.student.frejo105.beerapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import cz.msebera.android.httpclient.client.HttpResponseException;
import se.liu.student.frejo105.beerapp.API.HttpCallback;
import se.liu.student.frejo105.beerapp.API.HttpClient;
import se.liu.student.frejo105.beerapp.API.Model.Beer;
import se.liu.student.frejo105.beerapp.API.Model.Point;
import se.liu.student.frejo105.beerapp.API.Model.Pub;
import se.liu.student.frejo105.beerapp.Activities.DrawerMenuActivity;
import se.liu.student.frejo105.beerapp.R;

public class MainActivity extends DrawerMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Point p = new Point();
        p.latitude = 55.4115954;
        p.longitude = 15.6022470000000;
        HttpClient.getClosest(p, new HttpCallback<Pub>() {

            @Override
            public void onSuccess(Pub result) {
                System.out.println(result.name);
            }

            @Override
            public void onFailure(HttpResponseException hre) {
                System.out.println(hre.getMessage());
            }
        });
    }
}
