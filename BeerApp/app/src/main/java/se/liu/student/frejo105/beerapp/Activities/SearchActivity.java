package se.liu.student.frejo105.beerapp.Activities;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.WindowManager;

import se.liu.student.frejo105.beerapp.Fragments.DisplayBeerFragment;
import se.liu.student.frejo105.beerapp.Fragments.SearchFragment;
import se.liu.student.frejo105.beerapp.Model.Beer;
import se.liu.student.frejo105.beerapp.R;
import se.liu.student.frejo105.beerapp.Utility.Utility;

public class SearchActivity extends DrawerMenuActivity {

    public static final String QUERY_KEY = SearchFragment.QUERY_KEY;
    private static final String CURRENT_DETAILS_KEY = "DETAILS";

    Beer beer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if (savedInstanceState == null) {
            SearchFragment saf = new SearchFragment();
            saf.setArguments(getIntent().getExtras());
            android.support.v4.app.FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            t.add(R.id.search_container, saf).addToBackStack(null).commit();
        }
    }

    public void showItemDetailsFragment(Beer beer) {
        this.beer = beer;
        DisplayBeerFragment dbf = new DisplayBeerFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(DisplayBeerFragment.ITEM_KEY, beer);
        dbf.setArguments(arguments);
        android.support.v4.app.FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        if (Utility.inLandscapeMode(this)) {
            t.replace(R.id.item_container, dbf).commit();
        }
        else {
            t.replace(R.id.search_container, dbf).addToBackStack(null).commit();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        if(beer != null) outState.putParcelable(CURRENT_DETAILS_KEY, beer);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(CURRENT_DETAILS_KEY)) {
            showItemDetailsFragment((Beer)savedInstanceState.getParcelable(CURRENT_DETAILS_KEY));
        }
    }
}
