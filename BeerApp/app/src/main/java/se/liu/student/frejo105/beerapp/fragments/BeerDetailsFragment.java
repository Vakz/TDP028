package se.liu.student.frejo105.beerapp.fragments;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import cz.msebera.android.httpclient.client.HttpResponseException;
import se.liu.student.frejo105.beerapp.R;
import se.liu.student.frejo105.beerapp.api.HttpCallback;
import se.liu.student.frejo105.beerapp.api.model.Beer;
import se.liu.student.frejo105.beerapp.utility.ImageHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class BeerDetailsFragment extends Fragment {

    public static final String BEER_PARAM = "BEER_PARAM";

    public BeerDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_beer_details, container, false);
        Beer beer = getArguments().getParcelable(BEER_PARAM);
        setupBeerInfo(v, beer);
        return v;
    }

    private void setupBeerInfo(final View view, Beer beer) {
        ((TextView)view.findViewById(R.id.item_name)).setText(beer.name);
        ((TextView)view.findViewById(R.id.item_brewery)).setText(beer.brewery);
        ((TextView)view.findViewById(R.id.item_type)).setText(beer.beerType);
        ((TextView)view.findViewById(R.id.item_description)).setText(beer.description);
        ImageHandler.getImage(getContext(), beer.id, new HttpCallback<Bitmap>() {
            @Override
            public void onSuccess(Bitmap result) {
                ImageView imageView = ((ImageView)view.findViewById(R.id.item_image));
                imageView.setImageBitmap(result);
            }

            @Override
            public void onFailure(HttpResponseException hre) {
                // Do nothing, image not important enough to do error recovery
            }
        });
    }

}
