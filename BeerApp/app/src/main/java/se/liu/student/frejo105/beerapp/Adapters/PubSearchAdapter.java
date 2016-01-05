package se.liu.student.frejo105.beerapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import se.liu.student.frejo105.beerapp.Model.Location;
import se.liu.student.frejo105.beerapp.Model.Pub;
import se.liu.student.frejo105.beerapp.R;

/**
 * Created by vakz on 2016-01-04.
 */
public class PubSearchAdapter extends ArrayAdapter<Pub> {

    Location location;

    public PubSearchAdapter(Context context, List<Pub> results, Location location) {
        super(context, 0, results);
        this.location = location;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Pub pub = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pub_results_adapter, parent, false);
        }

        ((TextView)convertView.findViewById(R.id.pub_list_name)).setText(pub.name);
        double distance = location.distanceTo(pub.loc);
        ((TextView)convertView.findViewById(R.id.pub_list_distance)).setText(String.valueOf(distance));

        return convertView;
    }
}
