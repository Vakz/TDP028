package se.liu.student.frejo105.beerapp.Activities;

import android.app.FragmentTransaction;
import android.os.Bundle;

import se.liu.student.frejo105.beerapp.Fragments.SearchActivityFragment;
import se.liu.student.frejo105.beerapp.R;

public class SearchActivity extends DrawerMenuActivity {

    public static final String QUERY_KEY = SearchActivityFragment.QUERY_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchActivityFragment saf = new SearchActivityFragment();
        saf.setArguments(getIntent().getExtras());
        android.support.v4.app.FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.contentframe, saf).commit();

    }
}
