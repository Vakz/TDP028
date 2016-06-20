package se.liu.student.frejo105.beerapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.client.HttpResponseException;
import se.liu.student.frejo105.beerapp.api.HttpCallback;
import se.liu.student.frejo105.beerapp.api.model.Beer;
import se.liu.student.frejo105.beerapp.R;
import se.liu.student.frejo105.beerapp.utility.ImageHandler;

/**
 * Created by vakz on 2016-06-20.
 */
public class BeerListAdapter extends ArrayAdapter<Beer> {
    List<Beer> list;

    public BeerListAdapter(Context context, List<Beer> l) {
        super(context, 0, l);
        this.list = l;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Beer beer = getItem(position);

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
        return convertView;
    }

    public void setBeerList(ArrayList<Beer> list) {
        this.list.clear();
        this.list.addAll(list);
    }
}
