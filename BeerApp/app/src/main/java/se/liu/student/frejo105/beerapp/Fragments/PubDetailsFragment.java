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

import cz.msebera.android.httpclient.client.HttpResponseException;
import se.liu.student.frejo105.beerapp.API.HttpClient;
import se.liu.student.frejo105.beerapp.API.RequestCompleteCallback;
import se.liu.student.frejo105.beerapp.Activities.BeerDisplayActivity;
import se.liu.student.frejo105.beerapp.Adapters.BeerResultAdapter;
import se.liu.student.frejo105.beerapp.Model.Beer;
import se.liu.student.frejo105.beerapp.Model.Pub;
import se.liu.student.frejo105.beerapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PubDetailsFragment extends Fragment {

    public static final String PUB_KEY = "PUB";

    Pub pub = null;

    public PubDetailsFragment() {
        // Required empty public constructor
    }

    View.OnClickListener retry = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            displayMenu();
        }
    };

    private RequestCompleteCallback<Pub> menuRequestHandler = new RequestCompleteCallback<Pub>() {
        @Override
        public void onSuccess(Pub result) {
            pub = result;
            displayMenu();
        }

        @Override
        public void onFailure(HttpResponseException hre) {
            Snackbar errorBar = Snackbar.make(getView(), hre.getMessage(), Snackbar.LENGTH_LONG);
            errorBar.setAction(getString(R.string.retry), retry);
            errorBar.setActionTextColor(Color.RED);
            errorBar.show();
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle b = getArguments();
        if (!b.containsKey(PUB_KEY)) throw new RuntimeException(getString(R.string.no_item_error));
        this.pub = b.getParcelable(PUB_KEY);
        View w = inflater.inflate(R.layout.fragment_pub_details, container, false);
        ((TextView)w.findViewById(R.id.pub_details_name)).setText(pub.name);
        System.out.println(pub.name);
        System.out.println(pub.desc);
        ((TextView)w.findViewById(R.id.pub_details_description)).setText(pub.desc);
        displayMenu();
        return w;
    }

    private void displayMenu() {
        // Did we receive a pub with a populated menu?
        if(pub.serves.get(pub.serves.size() - 1).name != null) {
            setMenu(pub.serves);
        }
        else {
            HttpClient.getMenu(pub._id, menuRequestHandler);
        }

    }

    private void setMenu(ArrayList<Beer> menu) {
        BeerResultAdapter menuAdapter = new BeerResultAdapter(getContext(), menu);
        ListView menuView = (ListView)getView().findViewById(R.id.pub_details_menu);
        menuView.setAdapter(menuAdapter);
        menuView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BeerDisplayActivity activity = (BeerDisplayActivity)getActivity();
                activity.showItemDetailsFragment((Beer)parent.getAdapter().getItem(position));
            }
        });
    }

}
