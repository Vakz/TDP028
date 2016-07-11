package se.liu.student.frejo105.beerapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import se.liu.student.frejo105.beerapp.R;
import se.liu.student.frejo105.beerapp.api.model.Beer;
import se.liu.student.frejo105.beerapp.api.model.Point;
import se.liu.student.frejo105.beerapp.api.model.Pub;
import se.liu.student.frejo105.beerapp.utility.Utility;

/**
 * Created by vakz on 2016-06-21.
 */
public class PubListAdapter extends ArrayAdapter<Pub> {

    List<Pub> items;

    public PubListAdapter(Context context, List<Pub> results) {
        super(context, 0, results);
        items = results;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Pub pub = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pub_list_adapter, parent, false);
        }

        ((TextView)convertView.findViewById(R.id.pub_item_name)).setText(pub.name);
        ((TextView)convertView.findViewById(R.id.pub_item_distance)).setText(Utility.formatDistanceString(pub.distance));

        return convertView;
    }

    public void setPubList(List<Pub> list) {
        this.items.clear();
        this.items.addAll(list);
        this.notifyDataSetChanged();
    }
}
