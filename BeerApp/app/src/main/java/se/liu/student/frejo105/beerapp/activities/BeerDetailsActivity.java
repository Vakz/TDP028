package se.liu.student.frejo105.beerapp.activities;

import android.app.FragmentManager;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cz.msebera.android.httpclient.client.HttpResponseException;
import se.liu.student.frejo105.beerapp.R;
import se.liu.student.frejo105.beerapp.api.HttpCallback;
import se.liu.student.frejo105.beerapp.api.HttpClient;
import se.liu.student.frejo105.beerapp.api.model.Beer;
import se.liu.student.frejo105.beerapp.fragments.BeerDetailsFragment;

public class BeerDetailsActivity extends DrawerMenuActivity {

    public static final String BEER_ID_PARAM = "BEER_ID_PARAM";
    public static final String FULL_BEER_PARAM = "FULL_BEER_PARAM";
    public static final String BEER_FRAGMENT = "BEER_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_details);

        setupBeer();
    }

    private void setupBeer() {
        Bundle b = getIntent().getExtras();
        if (!b.containsKey(BEER_ID_PARAM) && !b.containsKey(FULL_BEER_PARAM)) {
            // Get suggestion
        }
        else if (b.containsKey(FULL_BEER_PARAM)) {
            setupFragment((Beer)b.getParcelable(FULL_BEER_PARAM));
        }
        else {
            int id = b.getInt(BEER_ID_PARAM);
            HttpClient.getBeer(id, new HttpCallback<Beer>() {
                @Override
                public void onSuccess(Beer result) {
                    setupFragment(result);
                }

                @Override
                public void onFailure(HttpResponseException hre) {
                    showSearchError(hre.getMessage());
                }
            });
        }
    }

    private void setupFragment(Beer beer) {
        BeerDetailsFragment bdf = new BeerDetailsFragment();
        Bundle b = new Bundle();
        b.putParcelable(BeerDetailsFragment.BEER_PARAM, beer);
        bdf.setArguments(b);
        FragmentManager t = getFragmentManager();
        t.popBackStack(BEER_FRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        t.beginTransaction().
                replace(R.id.detailed_item_placeholder, bdf).
                addToBackStack(BEER_FRAGMENT).commit();
        t.executePendingTransactions();
    }

    protected void showSearchError(String error) {
        final View w = findViewById(R.id.beer_fragments_container);
        View.OnClickListener retry = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupBeer();
            }
        };

        Snackbar errorBar = Snackbar.make(w, error, Snackbar.LENGTH_LONG);
        errorBar.setAction(R.string.retry, retry);
        errorBar.setActionTextColor(Color.RED);
        errorBar.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
