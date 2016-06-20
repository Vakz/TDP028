package se.liu.student.frejo105.beerapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import se.liu.student.frejo105.beerapp.api.model.Beer;
import se.liu.student.frejo105.beerapp.adapters.BeerListAdapter;
import se.liu.student.frejo105.beerapp.R;

public class BeerListFragment extends Fragment {

    BeerListAdapter items;
    ItemSelectedInterface mListener;
    ListView.OnItemClickListener itemSelect = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mListener.onClick((Beer)parent.getAdapter().getItem(position));
        }
    };

    public BeerListFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_beer_list, container, false);
    }

    public void setBeerList(final ArrayList<Beer> list) {
        items.setBeerList(list);
    }

    public interface ItemSelectedInterface {
        void onClick(Beer beer);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        items = new BeerListAdapter(getContext(), new ArrayList<Beer>());
        ListView lw = ((ListView)view.findViewById(R.id.beer_list));
        lw.setAdapter(items);
        lw.setOnItemClickListener(itemSelect);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
           mListener = (ItemSelectedInterface) context;
        }
    }
}
