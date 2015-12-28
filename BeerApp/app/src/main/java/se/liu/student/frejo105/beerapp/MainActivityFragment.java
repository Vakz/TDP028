package se.liu.student.frejo105.beerapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import se.liu.student.frejo105.beerapp.API.APIHandler;
import se.liu.student.frejo105.beerapp.API.RequestCompleteCallback;
import se.liu.student.frejo105.beerapp.Model.Beer;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View w = inflater.inflate(R.layout.fragment_main, container, false);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                APIHandler a = new APIHandler();
                a.getBeer("567927ccf77ad05613f91d47", new RequestCompleteCallback<Beer>() {
                    @Override
                    public void onSuccess(Beer result) {
                        System.out.println(result.name);
                    }

                    @Override
                    public void onFailure(String message) {
                        System.out.println(message);
                    }
                });
            }
        });
        t.run();
        return w;
    }
}
