package se.liu.student.frejo105.beerapp.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.client.HttpResponseException;
import se.liu.student.frejo105.beerapp.R;
import se.liu.student.frejo105.beerapp.api.HttpCallback;
import se.liu.student.frejo105.beerapp.api.HttpClient;
import se.liu.student.frejo105.beerapp.api.model.Beer;
import se.liu.student.frejo105.beerapp.api.model.Point;
import se.liu.student.frejo105.beerapp.api.model.Pub;
import se.liu.student.frejo105.beerapp.fragments.BeerDetailsFragment;
import se.liu.student.frejo105.beerapp.fragments.PubListFragment;
import se.liu.student.frejo105.beerapp.utility.LocationCallback;
import se.liu.student.frejo105.beerapp.utility.PersistentStorage;

public class BeerDetailsActivity extends DrawerMenuActivity
implements PubListFragment.ItemSelectedInterface{

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
        if (b == null || (!b.containsKey(BEER_ID_PARAM) && !b.containsKey(FULL_BEER_PARAM))) {
            getLocation(new LocationCallback() {
                @Override
                public void onDone(Point point) {
                    if (point == null) return;
                    HttpClient.suggestion(point, null, new HttpCallback<Beer>() {
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
            });
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

    private void setupFragment(final Beer beer) {
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

        getLocation(new LocationCallback() {
            @Override
            public void onDone(Point point) {
                HttpClient.pubsServing(point, beer.id, 1000000000, new HttpCallback<ArrayList<Pub>>() {
                    @Override
                    public void onSuccess(ArrayList<Pub> result) {
                        setupPubFragment(result);
                    }

                    @Override
                    public void onFailure(HttpResponseException hre) {
                        // May fail to find nearby pubs for multiple reasons. Not essential,
                        // so we do no error recovery
                    }
                });
            }
        });
    }

    private void setupPubFragment(List<Pub> list) {
        PubListFragment plf = new PubListFragment();
        FragmentManager t = getFragmentManager();

        t.beginTransaction()
                .replace(R.id.misc_item_info_placeholder, plf)
                .commit();
        t.executePendingTransactions();

        plf.setPubList(list);
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

    @Override
    public void onClick(Pub pub) {
        Intent intent = new Intent(this, PubDetailsActivity.class);
        intent.putExtra(PubDetailsActivity.FULL_PUB_PARAM, pub);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // Actually reusing activity would be rather complex, simply
        // create a new one instead
        finish();
        startActivity(intent);
    }
}
