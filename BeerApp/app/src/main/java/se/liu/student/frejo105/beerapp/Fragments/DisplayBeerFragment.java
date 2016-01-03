package se.liu.student.frejo105.beerapp.Fragments;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cz.msebera.android.httpclient.client.HttpResponseException;
import se.liu.student.frejo105.beerapp.API.HttpClient;
import se.liu.student.frejo105.beerapp.API.RequestCompleteCallback;
import se.liu.student.frejo105.beerapp.Model.Beer;
import se.liu.student.frejo105.beerapp.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class DisplayBeerFragment extends Fragment {

    public static final String ID_KEY = "ID";
    public final static String ITEM_KEY = BeerDetailsFragment.ITEM_KEY;

    Beer beer;

    View.OnClickListener retryHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getBeerByNetwork();
        }
    };

    RequestCompleteCallback<Beer> networkHandler = new RequestCompleteCallback<Beer>() {
        @Override
        public void onSuccess(Beer result) {
            displayBeer(result);
        }

        @Override
        public void onFailure(HttpResponseException hre) {
            Snackbar errorBar = Snackbar.make(getView(), hre.getMessage(), Snackbar.LENGTH_LONG);
            errorBar.setAction(R.string.retry, retryHandler);
            errorBar.setActionTextColor(Color.RED);
            errorBar.show();
        }
    };

    public DisplayBeerFragment() {
    }


    protected void getBeerByNetwork() {
        Bundle b = getArguments();
        HttpClient.getBeer(b.getString(ID_KEY), networkHandler);
    }

    protected void displayBeer(Beer beer) {
        this.beer = beer;
        BeerDetailsFragment bdf = new BeerDetailsFragment();
        Bundle b = new Bundle();
        b.putParcelable(BeerDetailsFragment.ITEM_KEY, beer);
        bdf.setArguments(b);
        FragmentTransaction t = getChildFragmentManager().beginTransaction();
        t.replace(R.id.detailed_item_placeholder, bdf);
        t.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View container_fragment = inflater.inflate(R.layout.fragment_display_beer, container, false);
        Bundle b = getArguments();
        if (savedInstanceState != null) {
            displayBeer((Beer)savedInstanceState.getParcelable(ITEM_KEY));
        }
        else if(b.containsKey(ID_KEY)) {
            getBeerByNetwork();
        }
        else if (b.containsKey(ITEM_KEY)) {
            displayBeer((Beer)b.getParcelable(ITEM_KEY));
        }
        else {
            Snackbar errorBar = Snackbar.make(getView(), "Attempting to show item, but no item provided", Snackbar.LENGTH_LONG);
            errorBar.setAction(R.string.retry, retryHandler);
            errorBar.setActionTextColor(Color.RED);
            errorBar.show();
        }

        return container_fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ITEM_KEY, beer);
    }
}
