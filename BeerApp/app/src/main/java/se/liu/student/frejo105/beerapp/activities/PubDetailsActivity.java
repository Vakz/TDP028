package se.liu.student.frejo105.beerapp.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import cz.msebera.android.httpclient.client.HttpResponseException;
import se.liu.student.frejo105.beerapp.R;
import se.liu.student.frejo105.beerapp.adapters.PubTabsAdapter;
import se.liu.student.frejo105.beerapp.api.HttpCallback;
import se.liu.student.frejo105.beerapp.api.HttpClient;
import se.liu.student.frejo105.beerapp.api.model.Beer;
import se.liu.student.frejo105.beerapp.api.model.Pub;
import se.liu.student.frejo105.beerapp.fragments.BeerListFragment;
import se.liu.student.frejo105.beerapp.fragments.PubDetailsFragment;

public class PubDetailsActivity extends DrawerMenuActivity
        implements BeerListFragment.ItemSelectedInterface {

    public static final String PUB_ID_PARAM = "PUB_ID_PARAM";
    public static final String FULL_PUB_PARAM = "FULL_PUB_PARAM";
    public static final String PUB_FRAGMENT = "PUB_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_details);

        setupPub();


    }

    private void setupPub() {
        Bundle b = getIntent().getExtras();
        if (b.containsKey(FULL_PUB_PARAM)) {
            Pub p = b.getParcelable(FULL_PUB_PARAM);
            setupDetailsFragment(p);
        }
        else if (b.containsKey(PUB_ID_PARAM)) {
            HttpClient.pub(b.getInt(PUB_ID_PARAM), new HttpCallback<Pub>() {
                @Override
                public void onSuccess(Pub result) {
                    setupDetailsFragment(result);
                }

                @Override
                public void onFailure(HttpResponseException hre) {
                    showError(hre.getMessage());
                }
            });
        }
        else {
            // This should never happen during normal use
            showError(getString(R.string.no_pub_param));
        }
    }

    private void setupDetailsFragment(Pub pub) {
        PubDetailsFragment pdf = new PubDetailsFragment();
        Bundle b = new Bundle();
        b.putParcelable(PubDetailsFragment.PUB_PARAM, pub);
        pdf.setArguments(b);
        FragmentManager t = getFragmentManager();
        t.popBackStack(PUB_FRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        t.beginTransaction().
                replace(R.id.detailed_pub_placeholder, pdf).
                addToBackStack(PUB_FRAGMENT).commit();
        t.executePendingTransactions();
        setupAuxiliaryFragments(pub);
    }

    private void setupAuxiliaryFragments(Pub pub) {
        ViewPager vp = (ViewPager)findViewById(R.id.pub_detail_tab_content);
        vp.setAdapter(new PubTabsAdapter(getFragmentManager(), pub));
        TabLayout tb = (TabLayout) findViewById(R.id.pub_detail_tabs);
        tb.setupWithViewPager(vp);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    protected void showError(String error) {
        final View w = findViewById(R.id.pub_fragments_container);

        Snackbar errorBar = Snackbar.make(w, error, Snackbar.LENGTH_LONG);
        errorBar.setActionTextColor(Color.RED);
        errorBar.show();
    }

    @Override
    public void onClick(Beer beer) {
        Intent intent = new Intent(this, BeerDetailsActivity.class);
        intent.putExtra(BeerDetailsActivity.FULL_BEER_PARAM, beer);
        startActivity(intent);
    }
}
