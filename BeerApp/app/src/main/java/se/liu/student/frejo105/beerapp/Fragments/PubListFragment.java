package se.liu.student.frejo105.beerapp.Fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cz.msebera.android.httpclient.client.HttpResponseException;
import se.liu.student.frejo105.beerapp.API.RequestCompleteCallback;
import se.liu.student.frejo105.beerapp.Adapters.PubSearchAdapter;
import se.liu.student.frejo105.beerapp.Model.Location;
import se.liu.student.frejo105.beerapp.Model.Pub;
import se.liu.student.frejo105.beerapp.R;
import se.liu.student.frejo105.beerapp.Utility.LocationProvider;

/**
 * A simple {@link Fragment} subclass.
 */
public class PubListFragment extends Fragment {

    public static final String PUB_LIST_KEY = "PUBS";
    LocationProvider locationProvider;


    public PubListFragment() {

    }

    ListView.OnItemClickListener itemSelect = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    };

    private void showError(String message, View.OnClickListener action) {
        Snackbar errorBar = Snackbar.make(getView(), message, Snackbar.LENGTH_LONG);
        errorBar.setAction("Retry", action);
        errorBar.setActionTextColor(Color.RED);
        errorBar.show();
    }

    private void postResults(final List<Pub> pubs, final View w) {
        final View.OnClickListener retry = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postResults(pubs, w);
            }
        };
        final RequestCompleteCallback<Location> locationHandler = new RequestCompleteCallback<Location>() {
            @Override
            public void onSuccess(final Location result) {
                locationProvider.removeListener(this);
                System.out.println("RECIEVED THIS: " + result.toString());
                Collections.sort(pubs, new Comparator<Pub>() {
                    @Override
                    public int compare(Pub lhs, Pub rhs) {
                        double lhsDistance = lhs.loc.distanceTo(result);
                        double rhsDistance = rhs.loc.distanceTo(result);
                        if (lhsDistance < rhsDistance) return -1;
                        return lhsDistance > rhsDistance ? 1 : 0;
                    }
                });
                if (getContext() == null) return;
                PubSearchAdapter adapter = new PubSearchAdapter(getContext(), pubs, result);
                ListView list = (ListView) w.findViewById(R.id.pub_list);
                list.setAdapter(adapter);
                list.setOnItemClickListener(itemSelect);
            }

            @Override
            public void onFailure(HttpResponseException hre) {
                locationProvider.removeListener(this);
                showError(getString(R.string.location_fail), retry);
            }
        };


        if (locationProvider == null) locationProvider = new LocationProvider(getContext());
            locationProvider.addListener(locationHandler);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View w = inflater.inflate(R.layout.fragment_pub_list, container, false);
        ArrayList<Pub> pubs = getArguments().getParcelableArrayList(PUB_LIST_KEY);
        postResults(pubs, w);
        return w;
    }

}
