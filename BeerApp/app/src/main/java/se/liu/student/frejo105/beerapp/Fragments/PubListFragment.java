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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.client.HttpResponseException;
import se.liu.student.frejo105.beerapp.API.RequestCompleteCallback;
import se.liu.student.frejo105.beerapp.Adapters.PubSearchAdapter;
import se.liu.student.frejo105.beerapp.Model.Location;
import se.liu.student.frejo105.beerapp.Model.Pub;
import se.liu.student.frejo105.beerapp.R;
import se.liu.student.frejo105.beerapp.Utility.Utility;

/**
 * A simple {@link Fragment} subclass.
 */
public class PubListFragment extends Fragment {

    public static final String PUB_LIST_KEY = "PUBS";


    public PubListFragment() {
        // Required empty public constructor
    }

    ListView.OnItemClickListener itemSelect = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    };

    private void postResults(final List<Pub> pubs, final View w) {
        final View.OnClickListener retry = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postResults(pubs, w);
            }
        };

        Utility.getCurrentLocation(getContext(), new RequestCompleteCallback<Location>() {
            @Override
            public void onSuccess(Location result) {
                ((TextView)w.findViewById(R.id.super_simple_text)).setText(String.valueOf(result.longitude));
                /*
                PubSearchAdapter adapter = new PubSearchAdapter(getContext(), pubs, result);
                ListView list = (ListView)w.findViewById(R.id.pub_list);
                list.setAdapter(adapter);
                list.setOnItemClickListener(itemSelect);
                */
            }

            @Override
            public void onFailure(HttpResponseException hre) {
                Snackbar errorBar = Snackbar.make(getView(), getString(R.string.location_fail), Snackbar.LENGTH_LONG);
                errorBar.setAction("Retry", retry);
                errorBar.setActionTextColor(Color.RED);
                errorBar.show();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View w = inflater.inflate(R.layout.fragment_pub_list, container, false);
        ArrayList<Pub> pubs = getArguments().getParcelableArrayList(PUB_LIST_KEY);
        postResults(pubs, w);
        return w;
    }

}
