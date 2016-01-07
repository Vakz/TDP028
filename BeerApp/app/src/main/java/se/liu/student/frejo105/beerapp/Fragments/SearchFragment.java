package se.liu.student.frejo105.beerapp.Fragments;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.client.HttpResponseException;
import se.liu.student.frejo105.beerapp.API.HttpClient;
import se.liu.student.frejo105.beerapp.API.RequestCompleteCallback;
import se.liu.student.frejo105.beerapp.Activities.SearchActivity;
import se.liu.student.frejo105.beerapp.Adapters.BeerResultAdapter;
import se.liu.student.frejo105.beerapp.Model.Beer;
import se.liu.student.frejo105.beerapp.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchFragment extends Fragment {

    public static final String QUERY_KEY = "QUERY";
    List<Beer> results;

    public SearchFragment() {
    }

    ListView.OnItemClickListener itemSelect = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ((SearchActivity)getActivity()).showItemDetailsFragment((Beer)parent.getAdapter().getItem(position));
        }
    };

    protected void doSearch(final View w) {
        String query = getArguments().getString(QUERY_KEY);
        HttpClient.search(query, new RequestCompleteCallback<ArrayList<Beer>>() {
            @Override
            public void onSuccess(ArrayList<Beer> result) {
                results = result;
                postResults(results, w);
            }

            @Override
            public void onFailure(HttpResponseException hre) {
                showSearchError(hre.getMessage(), w);
            }
        });
    }

    private void postResults(List<Beer> results, final View w) {
        BeerResultAdapter items = new BeerResultAdapter(getContext(), results);
        ListView lw = ((ListView)w.findViewById(R.id.search_results));
        lw.setAdapter(items);
        lw.setOnItemClickListener(itemSelect);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View w = inflater.inflate(R.layout.fragment_search, container, false);
        if (results != null) {
            postResults(results, w);
        }
        else {
            doSearch(w);
        }
        return w;
    }

    protected void showSearchError(String error, final View w) {
        View.OnClickListener retry = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearch(w);
            }
        };

        Snackbar errorBar = Snackbar.make(w, error, Snackbar.LENGTH_LONG);
        errorBar.setAction(R.string.retry, retry);
        errorBar.setActionTextColor(Color.RED);
        errorBar.show();
    }


}
