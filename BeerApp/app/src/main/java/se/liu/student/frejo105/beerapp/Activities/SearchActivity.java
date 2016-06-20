package se.liu.student.frejo105.beerapp.Activities;

import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import java.util.ArrayList;

import cz.msebera.android.httpclient.client.HttpResponseException;
import se.liu.student.frejo105.beerapp.API.HttpCallback;
import se.liu.student.frejo105.beerapp.API.HttpClient;
import se.liu.student.frejo105.beerapp.API.Model.Beer;
import se.liu.student.frejo105.beerapp.Fragments.BeerListFragment;
import se.liu.student.frejo105.beerapp.R;

public class SearchActivity extends DrawerMenuActivity
implements BeerListFragment.ItemSelectedInterface {

    public static final String QUERY_KEY = "QUERY";
    BeerListFragment beerListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        beerListFragment = new BeerListFragment();
        FragmentManager t = getFragmentManager();
        t.beginTransaction().replace(R.id.search_container, beerListFragment).commit();

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
        System.out.println(beer.name);
    }
}
