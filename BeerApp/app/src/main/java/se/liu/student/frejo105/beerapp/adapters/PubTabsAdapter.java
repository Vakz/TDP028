package se.liu.student.frejo105.beerapp.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;

import cz.msebera.android.httpclient.client.HttpResponseException;
import se.liu.student.frejo105.beerapp.api.HttpCallback;
import se.liu.student.frejo105.beerapp.api.HttpClient;
import se.liu.student.frejo105.beerapp.api.model.Beer;
import se.liu.student.frejo105.beerapp.api.model.Pub;
import se.liu.student.frejo105.beerapp.fragments.BeerDetailsFragment;
import se.liu.student.frejo105.beerapp.fragments.BeerListFragment;

/**
 * Created by vakz on 2016-06-28.
 */
public class PubTabsAdapter extends FragmentPagerAdapter {

    Pub pub;
    BeerListFragment bdf;
    private static final String titles[] = new String[] {"Menu"};

    public PubTabsAdapter(FragmentManager f, Pub pub) {
        super(f);
        this.pub = pub;
    }

    @Override
    public Fragment getItem(int position) {
        switch (getPageTitle(position).toString()) {
            case "Menu":
                return bdf == null ? setupMenu() : bdf;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    private Fragment setupMenu() {
        this.bdf = new BeerListFragment();
        HttpClient.menu(pub.id, new HttpCallback<ArrayList<Beer>>() {
            @Override
            public void onSuccess(ArrayList<Beer> result) {
                bdf.setBeerList(result);
            }

            @Override
            public void onFailure(HttpResponseException hre) {

            }
        });
        return bdf;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
