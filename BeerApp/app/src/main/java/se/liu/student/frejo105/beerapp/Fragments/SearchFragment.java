package se.liu.student.frejo105.beerapp.Fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import se.liu.student.frejo105.beerapp.Activities.BeerDisplayActivity;
import se.liu.student.frejo105.beerapp.Adapters.BeerResultAdapter;
import se.liu.student.frejo105.beerapp.Model.Beer;
import se.liu.student.frejo105.beerapp.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchFragment extends Fragment {

    BeerResultAdapter items;
    Runnable onCreationComplete;

    public SearchFragment() {

    }

    public void pushNewResults(final ArrayList<Beer> resultList) {
        if (items != null) items.pushNewResultList(resultList);
        else {
            onCreationComplete = new Runnable() {
                @Override
                public void run() {
                    items.pushNewResultList(resultList);
                }
            };
        }
    }

    ListView.OnItemClickListener itemSelect = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ((BeerDisplayActivity)getActivity()).showItemDetailsFragment((Beer)parent.getAdapter().getItem(position));
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View w = inflater.inflate(R.layout.fragment_search, container, false);
        return w;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        items = new BeerResultAdapter(getContext(), new ArrayList<Beer>());
        ListView lw = ((ListView)view.findViewById(R.id.search_results));
        lw.setAdapter(items);
        lw.setOnItemClickListener(itemSelect);
        if (onCreationComplete != null) {
            onCreationComplete.run();
            onCreationComplete = null;
        }
        super.onViewCreated(view, savedInstanceState);
    }
}
