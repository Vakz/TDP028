package se.liu.student.frejo105.beerapp.Activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;

import se.liu.student.frejo105.beerapp.Fragments.DisplayBeerFragment;
import se.liu.student.frejo105.beerapp.Fragments.SearchFragment;
import se.liu.student.frejo105.beerapp.Model.Beer;
import se.liu.student.frejo105.beerapp.R;

public class SearchActivity extends DrawerMenuActivity  implements BeerDisplayActivity {

    public static final String QUERY_KEY = SearchFragment.QUERY_KEY;
    private static final String CURRENT_DETAILS_KEY = "DETAILS";
    private static final String SECOND_PARTITION_POP_ID = "POP";

    Beer beer;
    DisplayBeerFragment itemDetailFragment;
    SearchFragment resultsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        // Returning as the result of, what is likely to be, an orientation change?
        if (savedInstanceState == null) {
            SearchFragment saf = new SearchFragment();
            resultsFragment = saf;
            saf.setArguments(getIntent().getExtras());
            android.support.v4.app.FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            t.add(R.id.search_container, saf).addToBackStack(null).commit();
        }
    }

    public void showItemDetailsFragment(Beer beer) {
        this.beer = beer;
        android.support.v4.app.FragmentManager t = getSupportFragmentManager();

        /* Remove the old fragment and create a new one. While this does destory and create
         * a new fragment for what seems like no reason, a bug exists where if a fragment in turn
          * has nested child fragment, these child fragments will end up detached from the activity
          * when the parent fragment is moved */
        if (itemDetailFragment != null) {
            t.beginTransaction().remove(itemDetailFragment).commit();
            t.executePendingTransactions();
        }
        itemDetailFragment = new DisplayBeerFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(DisplayBeerFragment.ITEM_KEY, beer);
        itemDetailFragment.setArguments(arguments);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            t.popBackStackImmediate(SECOND_PARTITION_POP_ID, 1);
            t.beginTransaction().replace(R.id.item_container, itemDetailFragment).commit();
        }
        else {
            t.beginTransaction().replace(R.id.search_container, itemDetailFragment).addToBackStack(SECOND_PARTITION_POP_ID).commit();
        }
        t.executePendingTransactions();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_search);
        // If we had details for a beer up when the orientation change happened, recreate this view
        if (itemDetailFragment != null) showItemDetailsFragment(beer);

    }
}
