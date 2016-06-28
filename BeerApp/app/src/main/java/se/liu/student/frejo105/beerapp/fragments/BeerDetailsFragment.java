package se.liu.student.frejo105.beerapp.fragments;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import cz.msebera.android.httpclient.client.HttpResponseException;
import se.liu.student.frejo105.beerapp.R;
import se.liu.student.frejo105.beerapp.api.HttpCallback;
import se.liu.student.frejo105.beerapp.api.model.Beer;
import se.liu.student.frejo105.beerapp.utility.ImageHandler;
import se.liu.student.frejo105.beerapp.utility.PersistentStorage;

public class BeerDetailsFragment extends Fragment
implements Button.OnClickListener {

    public static final String BEER_PARAM = "BEER_PARAM";
    private static final String TESTED_COLOR = "#2196f3";
    private static final String UNTESTED_COLOR = "#ff4a4a";
    Beer beer;

    public BeerDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onClick(View v) {
        swapButtonStyle((Button)v);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_beer_details, container, false);
        beer = getArguments().getParcelable(BEER_PARAM);
        setupBeerInfo(v);

        Button button = (Button)v.findViewById(R.id.tested_button);
        button.setOnClickListener(this);

        return v;
    }

    private void setupBeerInfo(final View view) {
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

        setButtonStyle((Button)view.findViewById(R.id.tested_button));
    }

    private void setButtonStyle(Button button) {
        PersistentStorage ps = new PersistentStorage(getContext());
        boolean isTested = ps.isTested(beer.id);
        if (isTested) {
            button.setText(getString(R.string.tested));
            button.setBackgroundColor(Color.parseColor(TESTED_COLOR));
        }
        else {
            button.setText(getString(R.string.untested));
            button.setBackgroundColor(Color.parseColor(UNTESTED_COLOR));
        }
    }

    private void swapButtonStyle(Button button) {
        PersistentStorage ps = new PersistentStorage(getContext());
        boolean isTested = ps.isTested(beer.id);
        if (isTested) {
            if (ps.deleteId(beer.id)) {
                setButtonStyle(button);
            }
        }
        else {
            if (ps.writeId(beer.id)) {
                setButtonStyle(button);
            }
        }

    }

}
