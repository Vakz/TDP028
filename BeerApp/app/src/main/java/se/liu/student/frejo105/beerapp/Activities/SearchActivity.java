package se.liu.student.frejo105.beerapp.Activities;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import cz.msebera.android.httpclient.client.HttpResponseException;
import se.liu.student.frejo105.beerapp.API.HttpClient;
import se.liu.student.frejo105.beerapp.API.RequestCompleteCallback;
import se.liu.student.frejo105.beerapp.Fragments.DisplayBeerFragment;
import se.liu.student.frejo105.beerapp.Fragments.SearchFragment;
import se.liu.student.frejo105.beerapp.Model.Beer;
import se.liu.student.frejo105.beerapp.R;

public class SearchActivity extends DrawerMenuActivity  implements BeerDisplayActivity {

    public static final String QUERY_KEY = "QUERY";
    private static final String CURRENT_DETAILS_KEY = "DETAILS";
    private static final String CURRENT_BEER = "BEER";
    private static final String BEER_DETAILS_FRAGMENT = "BEER_FRAGMENT";
    Beer beer;
    ArrayList<Beer> searchResults;

    SearchFragment resultsFragment;

    Runnable searchCompleteCallback = new Runnable() {
        @Override
        public void run() {
            if (resultsFragment != null) {
                resultsFragment.pushNewResults(searchResults);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        resultsFragment = new SearchFragment();
        FragmentManager t = getSupportFragmentManager();
        t.beginTransaction().replace(R.id.search_container, resultsFragment).commit();
        t.executePendingTransactions();
        if (savedInstanceState != null) {
            searchResults = savedInstanceState.getParcelableArrayList(CURRENT_DETAILS_KEY);
            if (savedInstanceState.containsKey(CURRENT_BEER)) beer = savedInstanceState.getParcelable(CURRENT_BEER);
        }
        showResultsList();
        if (beer != null) showItemDetailsFragment(beer);
    }

    private void showResultsList() {
        if (searchResults == null) {
            search(searchCompleteCallback);
        }
        else {
            resultsFragment.pushNewResults(searchResults);
        }
    }

    protected void showSearchError(String error) {
        final View w = findViewById(R.id.search_layout_container);
        View.OnClickListener retry = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(searchCompleteCallback);
            }
        };

        Snackbar errorBar = Snackbar.make(w, error, Snackbar.LENGTH_LONG);
        errorBar.setAction(R.string.retry, retry);
        errorBar.setActionTextColor(Color.RED);
        errorBar.show();
    }

    private void search(final Runnable searchComplete) {
        String query = getIntent().getExtras().getString(QUERY_KEY);
        HttpClient.search(query, new RequestCompleteCallback<ArrayList<Beer>>() {
            @Override
            public void onSuccess(ArrayList<Beer> result) {
                searchResults = result;
                searchComplete.run();
            }

            @Override
            public void onFailure(HttpResponseException hre) {
                showSearchError(hre.getMessage());
            }
        });
    }

    public void showItemDetailsFragment(Beer beer) {
        this.beer = beer;
        android.support.v4.app.FragmentManager t = getSupportFragmentManager();
        DisplayBeerFragment itemDetailFragment = new DisplayBeerFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(DisplayBeerFragment.ITEM_KEY, beer);
        itemDetailFragment.setArguments(arguments);
        int container = inLandscape() ? R.id.item_container : R.id.search_container;
        t.beginTransaction().replace(container, itemDetailFragment).addToBackStack(BEER_DETAILS_FRAGMENT).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (beer != null) outState.putParcelable(CURRENT_BEER, beer);
        outState.putParcelableArrayList(CURRENT_DETAILS_KEY, searchResults);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onBackPressed() {
        // If in landscape, return to whatever user was doing before search
        if (beer == null || inLandscape()) {
            finish();
        }
        // If in portrait and looking at details of a beer, pop beer fragment and show result list
        else {
            beer = null;
            android.support.v4.app.FragmentManager t = getSupportFragmentManager();
            t.popBackStackImmediate(BEER_DETAILS_FRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            t.beginTransaction().commit();
            showResultsList();
        }
    }
}
