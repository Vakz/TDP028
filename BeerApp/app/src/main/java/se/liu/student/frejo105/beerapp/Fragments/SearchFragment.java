package se.liu.student.frejo105.beerapp.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import cz.msebera.android.httpclient.client.HttpResponseException;
import se.liu.student.frejo105.beerapp.API.HttpClient;
import se.liu.student.frejo105.beerapp.API.RequestCompleteCallback;
import se.liu.student.frejo105.beerapp.Activities.SearchActivity;
import se.liu.student.frejo105.beerapp.Adapters.SearchResultAdapter;
import se.liu.student.frejo105.beerapp.Model.Beer;
import se.liu.student.frejo105.beerapp.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchFragment extends Fragment {

    public static final String QUERY_KEY = "QUERY";

    public SearchFragment() {
    }

    ListView.OnItemClickListener itemSelect = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ((SearchActivity)getActivity()).showItemDetailsFragment((Beer)parent.getAdapter().getItem(position));
        }
    };

    protected void doSearch(final View w) {
        final Context _this = getContext();
        String query = getArguments().getString(QUERY_KEY);
        HttpClient.search(query, new RequestCompleteCallback<List<Beer>>() {
            @Override
            public void onSuccess(List<Beer> result) {
                SearchResultAdapter adapter = new SearchResultAdapter(_this, result);
                ListView lw = ((ListView)w.findViewById(R.id.search_results));
                lw.setAdapter(adapter);
                lw.setOnItemClickListener(itemSelect);
            }

            @Override
            public void onFailure(HttpResponseException hre) {
                showSearchError(hre.getMessage(), w);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View w = inflater.inflate(R.layout.fragment_search, container, false);
        doSearch(w);
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
