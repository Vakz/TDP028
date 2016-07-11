package se.liu.student.frejo105.beerapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cz.msebera.android.httpclient.client.HttpResponseException;
import se.liu.student.frejo105.beerapp.BeerApp;
import se.liu.student.frejo105.beerapp.api.HttpCallback;
import se.liu.student.frejo105.beerapp.api.model.Beer;
import se.liu.student.frejo105.beerapp.R;
import se.liu.student.frejo105.beerapp.utility.ImageHandler;
import se.liu.student.frejo105.beerapp.utility.PersistentStorage;

public class BeerListAdapter extends ArrayAdapter<Beer> {
    List<Beer> list;

    public BeerListAdapter(Context context, List<Beer> l) {
        super(context, 0, l);
        this.list = l;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final Beer beer = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.beer_list_adapter, parent, false);
        }

        ((TextView)convertView.findViewById(R.id.beer_item_name)).setText(beer.name);
        ((TextView)convertView.findViewById(R.id.beer_item_brewery)).setText(beer.brewery);
        ((TextView)convertView.findViewById(R.id.beer_item_type)).setText(beer.beerType);
        final ImageView thumbnail = (ImageView)convertView.findViewById(R.id.beer_item_thumbnail);
        ImageHandler.getImage(getContext(), beer.id, new HttpCallback<Bitmap>() {
            @Override
            public void onSuccess(Bitmap result) {
                thumbnail.setImageBitmap(result);
            }

            @Override
            public void onFailure(HttpResponseException hre) {
                // Do nothing
            }
        });
        final Button b = ((Button)convertView.findViewById(R.id.tested_button));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button)v;
                swapButtonStyle(b, beer);

            }
        });
        setButtonStyle(b, beer);
        return convertView;
    }

    private void setButtonStyle(Button button, Beer beer) {
        PersistentStorage ps = new PersistentStorage(getContext());
        boolean isTested = ps.isTested(beer.id);
        if (isTested) {
            button.setText(getContext().getResources().getString(R.string.tested));

            button.setBackgroundColor(Color.parseColor(BeerApp.TESTED_COLOR));
        }
        else {
            button.setText(getContext().getResources().getString(R.string.untested));
            button.setBackgroundColor(Color.parseColor(BeerApp.UNTESTED_COLOR));
        }
    }

    private void swapButtonStyle(Button button, Beer beer) {
        PersistentStorage ps = new PersistentStorage(getContext());
        boolean isTested = ps.isTested(beer.id);
        if (isTested) {
            if (ps.deleteId(beer.id)) {
                setButtonStyle(button, beer);
            }
        }
        else {
            if (ps.writeId(beer.id)) {
                setButtonStyle(button, beer);
            }
        }

    }

    public void setBeerList(List<Beer> list) {
        this.list.clear();
        this.list.addAll(list);
        this.notifyDataSetChanged();
    }
}
