package se.liu.student.frejo105.beerapp.Activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import se.liu.student.frejo105.beerapp.Fragments.BeerDetailsFragment;
import se.liu.student.frejo105.beerapp.Fragments.DisplayBeerFragment;
import se.liu.student.frejo105.beerapp.Fragments.PubDetailsFragment;
import se.liu.student.frejo105.beerapp.Model.Beer;
import se.liu.student.frejo105.beerapp.Model.Pub;
import se.liu.student.frejo105.beerapp.R;

public class PubDisplayActivity extends DrawerMenuActivity implements BeerDisplayActivity {

    public static final String PUB_KEY = PubDetailsFragment.PUB_KEY;
    private static final String SECOND_PARTITION_POP_ID = "BEER_PARTITION";
    Beer beer = null;
    DisplayBeerFragment bdf = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_display);

        Bundle b = getIntent().getExtras();
        if (!b.containsKey(PUB_KEY)) throw new RuntimeException(getString(R.string.no_item_error));
        if (savedInstanceState == null) {
            PubDetailsFragment pdf = new PubDetailsFragment();
            pdf.setArguments(b);
            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            t.replace(R.id.pub_info_panel, pdf).commit();
        }

    }

    @Override
    public void showItemDetailsFragment(Beer beer) {
        this.beer = beer;
        FragmentManager t = getSupportFragmentManager();
        if(bdf != null){
            t.beginTransaction().remove(bdf).commit();
            t.executePendingTransactions();
        }
        bdf = new DisplayBeerFragment();
        Bundle b = new Bundle();
        b.putParcelable(DisplayBeerFragment.ITEM_KEY, beer);
        bdf.setArguments(b);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            t.popBackStackImmediate(SECOND_PARTITION_POP_ID, 1);
            t.beginTransaction().replace(R.id.pub_misc_info_panel, bdf).commit();
        }
        else {
            t.beginTransaction().replace(R.id.pub_info_panel, bdf).addToBackStack(SECOND_PARTITION_POP_ID).commit();
        }
        // Need to make sure transactions are done immediately
        t.executePendingTransactions();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_search);
        // If we had details for a beer up when the orientation change happened, recreate this view
        if (beer != null) showItemDetailsFragment(beer);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
