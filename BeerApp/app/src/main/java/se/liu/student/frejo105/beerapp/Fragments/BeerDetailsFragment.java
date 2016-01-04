package se.liu.student.frejo105.beerapp.Fragments;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cz.msebera.android.httpclient.client.HttpResponseException;
import se.liu.student.frejo105.beerapp.API.RequestCompleteCallback;
import se.liu.student.frejo105.beerapp.Model.Beer;
import se.liu.student.frejo105.beerapp.R;
import se.liu.student.frejo105.beerapp.Utility.ImageHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class BeerDetailsFragment extends Fragment {

    public final static String ITEM_KEY = "ITEM";
    Beer beer;


    public BeerDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View detailsFragmentView = inflater.inflate(R.layout.fragment_detailed_beer, container, false);
        if (savedInstanceState != null) {
            insertBeerInfo(detailsFragmentView, (Beer)savedInstanceState.getParcelable(ITEM_KEY));
        }
        else {
            Bundle b = getArguments();
            Beer beer = b.getParcelable(ITEM_KEY);
            insertBeerInfo(detailsFragmentView, beer);
        }

        return detailsFragmentView;
    }

    public void setBeerInfo(Beer beer) {
        insertBeerInfo(getView(), beer);
    }

    private void insertBeerInfo(final View view, Beer beer) {
        if (beer == null) return;
        this.beer = beer;
        ((TextView)view.findViewById(R.id.item_name)).setText(beer.name);
        ((TextView)view.findViewById(R.id.item_brewery)).setText(beer.brewery.name);
        ((TextView)view.findViewById(R.id.item_type)).setText(beer.beerType.typeName);
        ((TextView)view.findViewById(R.id.item_description)).setText(beer.desc);
        ImageHandler.getImage(getContext(), beer._id, new RequestCompleteCallback<Bitmap>() {
            @Override
            public void onSuccess(Bitmap result) {
                ((ImageView)view.findViewById(R.id.item_image)).setImageBitmap(result);
            }

            @Override
            public void onFailure(HttpResponseException hre) {
                // Do nothing
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ITEM_KEY, beer);
    }


}
