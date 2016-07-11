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
import se.liu.student.frejo105.beerapp.fragments.BeerListFragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PubTabsAdapter extends FragmentPagerAdapter
implements OnMapReadyCallback {

    Pub pub;
    BeerListFragment bdf;
    MapFragment mf;
    private static final String titles[] = new String[] {"Menu", "Map"};

    public PubTabsAdapter(FragmentManager f, Pub pub) {
        super(f);
        this.pub = pub;
    }

    @Override
    public Fragment getItem(int position) {
        switch (getPageTitle(position).toString()) {
            case "Menu":
                return bdf == null ? setupMenu() : bdf;
            case "Map":
                return mf == null ? setupMap() : mf;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    private Fragment setupMap() {
        mf = new MapFragment();
        mf.getMapAsync(this);
        return mf;
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng loc = new LatLng(pub.location.latitude, pub.location.longitude);

        googleMap.addMarker(new MarkerOptions().position(loc).title(pub.name));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 14f));
    }
}
