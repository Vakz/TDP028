package se.liu.student.frejo105.beerapp.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import java.util.ArrayList;

import cz.msebera.android.httpclient.client.HttpResponseException;
import se.liu.student.frejo105.beerapp.api.HttpCallback;
import se.liu.student.frejo105.beerapp.api.HttpClient;
import se.liu.student.frejo105.beerapp.api.model.Beer;
import se.liu.student.frejo105.beerapp.fragments.BeerListFragment;
import se.liu.student.frejo105.beerapp.R;

public class SearchActivity extends BeerAppBaseActivity
implements BeerListFragment.ItemSelectedInterface {

    public static final String QUERY_KEY = "QUERY";
    private static final String SEARCH_FRAGMENT = "SEARCH_FRAGMENT";
    BeerListFragment beerListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        beerListFragment = new BeerListFragment();
        FragmentManager t = getFragmentManager();
        t.popBackStack(SEARCH_FRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        t.beginTransaction().
                replace(R.id.search_container, beerListFragment).
                addToBackStack(SEARCH_FRAGMENT).commit();
        t.executePendingTransactions();

        pushList();
    }

    private void pushList() {
        String query = getIntent().getExtras().getString(QUERY_KEY);
        HttpClient.search(query, new HttpCallback<ArrayList<Beer>>() {
            @Override
            public void onSuccess(ArrayList<Beer> result) {
                beerListFragment.setBeerList(result);
            }

            @Override
            public void onFailure(HttpResponseException hre) {
                showSearchError(hre.getMessage());
            }
        });
    }

    protected void showSearchError(String error) {
        final View w = findViewById(R.id.search_layout_container);
        View.OnClickListener retry = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushList();
            }
        };

        Snackbar errorBar = Snackbar.make(w, error, Snackbar.LENGTH_LONG);
        errorBar.setAction(R.string.retry, retry);
        errorBar.setActionTextColor(Color.RED);
        errorBar.show();
    }

    @Override
    public void onClick(Beer beer) {
        Intent intent = new Intent(this, BeerDetailsActivity.class);
        intent.putExtra(BeerDetailsActivity.FULL_BEER_PARAM, beer);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
