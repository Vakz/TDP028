package se.liu.student.frejo105.beerapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.client.HttpResponseException;
import se.liu.student.frejo105.beerapp.API.HttpClient;
import se.liu.student.frejo105.beerapp.API.RequestCompleteCallback;
import se.liu.student.frejo105.beerapp.Model.Beer;

import static se.liu.student.frejo105.beerapp.R.layout.fragment_main;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View w = inflater.inflate(fragment_main, container, false);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient.search("heineken", new RequestCompleteCallback<List<Beer>>() {
                    @Override
                    public void onSuccess(List<Beer> result) {
                        System.out.println(result.get(1).name);
                    }

                    @Override
                    public void onFailure(HttpResponseException hre) {
                        System.out.println(hre.getMessage());
                    }
                });
            }
        });
        t.run();
        return w;
    }
}
