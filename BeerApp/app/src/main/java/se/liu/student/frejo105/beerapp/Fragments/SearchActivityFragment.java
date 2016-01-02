package se.liu.student.frejo105.beerapp.Fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import cz.msebera.android.httpclient.client.HttpResponseException;
import se.liu.student.frejo105.beerapp.API.HttpClient;
import se.liu.student.frejo105.beerapp.API.RequestCompleteCallback;
import se.liu.student.frejo105.beerapp.Adapters.SearchResultAdapter;
import se.liu.student.frejo105.beerapp.Model.Beer;
import se.liu.student.frejo105.beerapp.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchActivityFragment extends Fragment {

    public static final String QUERY_KEY = "QUERY";

    public SearchActivityFragment() {
    }

    protected void doSearch() {
        final Context _this = getContext();
        String query = getArguments().getString(QUERY_KEY);
        HttpClient.search(query, new RequestCompleteCallback<List<Beer>>() {
            @Override
            public void onSuccess(List<Beer> result) {
                SearchResultAdapter adapter = new SearchResultAdapter(_this, result);
                ((ListView)getView().findViewById(R.id.search_results)).setAdapter(adapter);
            }

            @Override
            public void onFailure(HttpResponseException hre) {
                showSearchError(hre.getMessage());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        doSearch();
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    protected void showSearchError(String error) {
        View.OnClickListener retry = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearch();
            }
        };

        Snackbar errorBar = Snackbar.make(getView().findViewById(R.id.contentframe), error, Snackbar.LENGTH_LONG);
        errorBar.setAction(R.string.retry, retry);
        errorBar.setActionTextColor(Color.RED);
        errorBar.show();
    }
}
