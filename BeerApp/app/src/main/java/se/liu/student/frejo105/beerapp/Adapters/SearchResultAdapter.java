package se.liu.student.frejo105.beerapp.Adapters;

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
import se.liu.student.frejo105.beerapp.API.RequestCompleteCallback;
import se.liu.student.frejo105.beerapp.Model.Beer;
import se.liu.student.frejo105.beerapp.R;
import se.liu.student.frejo105.beerapp.Utility.ImageHandler;

/**
 * Created by vakz on 2016-01-02.
 */
public class SearchResultAdapter extends ArrayAdapter<Beer> {

    public SearchResultAdapter(Context context, List<Beer> results) {
        super(context, 0, results);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Beer beer = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_result_adapter, parent, false);
        }
        ((TextView)convertView.findViewById(R.id.search_result_name)).setText(beer.name);
        ((TextView)convertView.findViewById(R.id.search_result_brewery)).setText(beer.brewery.name);
        ((TextView)convertView.findViewById(R.id.search_result_type)).setText(beer.beerType.typeName);
        final ImageView thumbnail = (ImageView)convertView.findViewById(R.id.search_result_thumbnail);
        ImageHandler.getImage(getContext(), beer._id, new RequestCompleteCallback<Bitmap>() {
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
}
