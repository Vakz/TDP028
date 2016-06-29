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
import java.util.List;

import se.liu.student.frejo105.beerapp.api.model.Beer;
import se.liu.student.frejo105.beerapp.adapters.BeerListAdapter;
import se.liu.student.frejo105.beerapp.R;

public class BeerListFragment extends Fragment {

    public static final String OPTIONAL_BEER_LIST ="beer_list";

    BeerListAdapter items;
    ItemSelectedInterface mListener;
    ListView.OnItemClickListener itemSelect = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mListener.onClick((Beer)parent.getAdapter().getItem(position));
        }
    };

    public BeerListFragment() {    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_beer_list, container, false);
    }

    public void setBeerList(final List<Beer> list) {
        items.setBeerList(list);
    }

    public interface ItemSelectedInterface {
        void onClick(Beer beer);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        List<Beer> items;
        Bundle args = getArguments();
        if (args != null && args.containsKey(OPTIONAL_BEER_LIST)) {
            items = args.getParcelableArrayList(OPTIONAL_BEER_LIST);
        }
        else {
            items = new ArrayList<>();
        }
        this.items = new BeerListAdapter(getContext(), items);
        ListView lw = (ListView)view.findViewById(R.id.beer_list);
        lw.setAdapter(this.items);
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
